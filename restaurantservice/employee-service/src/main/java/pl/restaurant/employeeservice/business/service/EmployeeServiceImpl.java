package pl.restaurant.employeeservice.business.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.restaurant.employeeservice.api.mapper.AddressMapper;
import pl.restaurant.employeeservice.api.mapper.EmployeeMapper;
import pl.restaurant.employeeservice.api.mapper.ScheduleMapper;
import pl.restaurant.employeeservice.api.request.*;
import pl.restaurant.employeeservice.api.response.*;
import pl.restaurant.employeeservice.business.exception.*;
import pl.restaurant.employeeservice.business.utility.CredentialsGenerator;
import pl.restaurant.employeeservice.business.utility.PasswordMapper;
import pl.restaurant.employeeservice.business.utility.ValidationUtility;
import pl.restaurant.employeeservice.data.entity.AddressEntity;
import pl.restaurant.employeeservice.data.entity.EmployeeEntity;
import pl.restaurant.employeeservice.data.entity.ScheduleEntity;
import pl.restaurant.employeeservice.data.entity.WorkstationEntity;
import pl.restaurant.employeeservice.data.repository.AddressRepo;
import pl.restaurant.employeeservice.data.repository.EmployeeRepo;
import pl.restaurant.employeeservice.data.repository.ScheduleRepo;
import pl.restaurant.employeeservice.data.repository.WorkstationRepo;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Log4j2
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final static int AMOUNT = 10;
    private final static String ADMIN = "Administrator";
    private final static String MANAGER = "Menedżer";
    private EmployeeRepo employeeRepo;
    private ScheduleRepo scheduleRepo;
    private RestaurantServiceClient restaurantServiceClient;
    private AddressRepo addressRepo;
    private WorkstationRepo workstationRepo;
    private LoginService loginService;
    private KeycloakService keycloakService;

    @Override
    public EmployeeListView getEmployees(EmployeeFilters filter) {
        Pageable pageable = mapSortEventToPageable(filter);
        Page<EmployeeShortInfo> page = employeeRepo.getEmployees(filter.getRestaurantId(), filter.getEmployeeId(),
                filter.getActive(), filter.getSurname(), filter.getWorkstationId(),
                pageable);
        return new EmployeeListView().builder()
                .totalElements(page.getTotalElements())
                .employees(page.getContent())
                .build();
    }

    @Override
    @Transactional
    public EmployeeInfo getEmployeeInfo(Long employeeId) {
        EmployeeEntity employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException());
        LocalDateTime dateTime = LocalDateTime.now().withHour(1);
        List<ScheduleInfo> schedules = scheduleRepo.getSchedulesFromDate(dateTime);
        RestaurantShortInfo restaurantInfo = restaurantServiceClient
                .getRestaurantShortInfo(employee.getRestaurantId());

        return EmployeeMapper.mapDataToInfo(employee, restaurantInfo, schedules);
    }

    @Override
    public LoginResponse logIn(Credentials credentials) {
        KeycloakResponse response = loginService.login(credentials);
        UserResource user = keycloakService.getUser(credentials.getUsername());
        Long employeeId = Long.parseLong(credentials.getUsername().split("-")[1]);
        Long restaurantId = null;
        Optional<Long> restaurantIdOpt = employeeRepo.getEmployeeRestaurantId(employeeId);
        if (restaurantIdOpt.isPresent())
            restaurantId = restaurantIdOpt.get();

        return new LoginResponse().builder()
                .accessToken(response.getAccess_token())
                .refreshToken(response.getRefresh_token())
                .employeeId(employeeId)
                .fullName(user.toRepresentation().getFirstName() + " " + user.toRepresentation().getLastName())
                .role(user.roles().clientLevel(keycloakService.getClientRep().getId())
                        .listAll().get(0).toString())
                .restaurantId(restaurantId)
                .build();
    }

    @Override
    public void logOut(LogoutRequest logoutRequest) {
        loginService.logout(logoutRequest);
    }

    @Override
    public ScheduleInfo addScheduleForEmployee(Schedule schedule) {
        if (scheduleRepo.getScheduleByDateAndEmployeeId(schedule.getStartShift(),
                schedule.getEndShift(), schedule.getEmployeeId()).isPresent()) {
            throw new ScheduleAlreadyExistsException();
        }
        EmployeeEntity employee = employeeRepo.findById(schedule.getEmployeeId())
                .orElseThrow(EmployeeNotFoundException::new);
        ScheduleEntity savedSchedule = scheduleRepo.save(new ScheduleEntity().builder()
                .employee(employee)
                .startShift(schedule.getStartShift())
                .endShift(schedule.getEndShift())
                .build());
        return ScheduleMapper.mapDataToInfo(savedSchedule);
    }

    @Override
    @Transactional(rollbackOn = NoSuchMethodError.class)
    public Credentials addEmployee(Employee employee) {
        validateEmployee(employee);
        if (!restaurantServiceClient.isRestaurantExist(employee.getRestaurantId()))
            throw new RestaurantNotFoundException();
        AddressEntity address = addressRepo.save(AddressMapper.mapObjectToData(employee.getAddress()));
        WorkstationEntity workstation = workstationRepo.findById(employee.getWorkstationId())
                .orElseThrow(WorkstationNotFoundException::new);
        EmployeeEntity savedEmployee = employeeRepo
                .save(EmployeeMapper.mapObjectToData(employee, address, workstation));
        if (isKeycloakUser(workstation)) {
            Credentials credentials = createKeycloakUser(savedEmployee, workstation);
            return credentials;
        }
        return new Credentials();
    }

    @Override
    @Transactional
    public ScheduleInfo updateEmployeeSchedule(ScheduleInfo scheduleInfo) {
        ScheduleEntity schedule = scheduleRepo.findById(scheduleInfo.getId())
                .orElseThrow(ScheduleNotFoundException::new);
        Optional<ScheduleEntity> savedSchedule = scheduleRepo.getScheduleByDateAndEmployeeId(scheduleInfo.getStartShift(),
                scheduleInfo.getEndShift(), schedule.getEmployee().getEmployeeId());
        if (savedSchedule.isPresent() && !Objects.equals(savedSchedule.get().getScheduleId(), scheduleInfo.getId())) {
            throw new ScheduleAlreadyExistsException();
        }
        schedule.setStartShift(scheduleInfo.getStartShift());
        schedule.setEndShift(scheduleInfo.getEndShift());
        return ScheduleMapper.mapDataToInfo(scheduleRepo.save(schedule));
    }

    @Override
    @Transactional
    public void updateEmployee(Employee employee, Long employeeId) {
        validateEmployee(employee);
        if (!restaurantServiceClient.isRestaurantExist(employee.getRestaurantId()))
            throw new RestaurantNotFoundException();
        EmployeeEntity employeeEntity = employeeRepo.findById(employeeId)
                .orElseThrow(EmployeeNotFoundException::new);
        String oldName = employeeEntity.getName();
        employeeEntity.setName(employee.getName());
        employeeEntity.setSurname(employee.getSurname());
        employeeEntity.setWorkstation(workstationRepo.findById(employee.getWorkstationId())
                .orElseThrow(WorkstationNotFoundException::new));
        employeeEntity.setPhoneNr(employee.getPhoneNr());
        employeeEntity.setPesel(employee.getPesel());
        employeeEntity.setAccountNr(employee.getAccountNr());
        employeeEntity.setSalary(employee.getSalary());
        employeeEntity.setActive(employee.getActive());
        employeeEntity.setEmploymentDate(employee.getEmploymentDate());
        employeeEntity.setDismissalDate(employee.getDismissalDate());
        employeeEntity.getAddress().setCity(employee.getAddress().getCity());
        employeeEntity.getAddress().setStreet(employee.getAddress().getStreet());
        employeeEntity.getAddress().setHouseNr(employee.getAddress().getHouseNr());
        employeeEntity.getAddress().setFlatNr(employee.getAddress().getFlatNr());
        employeeEntity.getAddress().setPostcode(employee.getAddress().getPostcode());
        employeeRepo.save(employeeEntity);
        if (isKeycloakUser(employeeEntity.getWorkstation())) {
            UserRepresentation user = new UserRepresentation();
            user.setFirstName(employee.getName());
            user.setLastName(employee.getSurname());
            user.setUsername(CredentialsGenerator.generateUsername(employee.getName(), employeeId));
            keycloakService.getUser(CredentialsGenerator.generateUsername(oldName, employeeId)).
                    update(user);
        }
    }

    @Override
    @Transactional
    public void dismissEmployee(Long employeeId) {
        EmployeeEntity employee = employeeRepo.findById(employeeId)
                .orElseThrow(EmployeeNotFoundException::new);
        employee.setActive(false);
        employee.setDismissalDate(LocalDate.now());
        employeeRepo.save(employee);
        if (isKeycloakUser(employee.getWorkstation())) {
            keycloakService.getUser(CredentialsGenerator
                            .generateUsername(employee.getName(), employee.getEmployeeId()))
                    .remove();
        }
    }

    @Override
    @Transactional
    public void removeEmployeeSchedule(Long scheduleId) {
        scheduleRepo.deleteById(scheduleId);
    }

    @Override
    public boolean isAdminEmployeeExist(Long employeeId) {
        return employeeRepo.existsByEmployeeIdAndWorkstationNameAndActive(employeeId, ADMIN, true);
    }

    @Override
    public String getEmployeeFullName(Long employeeId) {
        return employeeRepo.getEmployeeFullNameById(employeeId).orElseThrow(EmployeeNotFoundException::new);
    }

    private Pageable mapSortEventToPageable(EmployeeFilters filter) {
        if (filter.getSortEvent() == null) {
            return PageRequest.of(filter.getPageNr(), AMOUNT);
        } else {
            try {
                String column = filter.getSortEvent().getColumn();
                EmployeeEntity.class.getDeclaredField(column);
                if (filter.getSortEvent().getDirection().equals(SortDirection.ASC))
                    return PageRequest.of(filter.getPageNr(), AMOUNT, Sort.by(Sort.Direction.ASC, column));
                else if (filter.getSortEvent().getDirection().equals(SortDirection.DESC)) {
                    return PageRequest.of(filter.getPageNr(), AMOUNT, Sort.by(Sort.Direction.DESC, column));
                } else
                    return PageRequest.of(filter.getPageNr(), AMOUNT, Sort.by(column));
            } catch (NoSuchFieldException e) {
                throw new ColumnNotFoundException();
            }
        }
    }

    private void validateEmployee(Employee employee) {
        ValidationUtility.validatePesel(employee.getPesel());
        ValidationUtility.validateAccountNr(employee.getAccountNr());
    }

    private UsersResource getInstance() {
        return keycloakService.getInstance().realm(keycloakService.getRealm()).users();
    }

    private boolean isKeycloakUser(WorkstationEntity workstation) {
        return Objects.equals(workstation.getName(), ADMIN) || Objects.equals(workstation.getName(), MANAGER);
    }

    private Credentials createKeycloakUser(EmployeeEntity employee, WorkstationEntity workstation) {
        String password = CredentialsGenerator.generatePassword();
        String username = CredentialsGenerator.generateUsername(employee.getName(), employee.getEmployeeId());
        CredentialRepresentation credential = PasswordMapper.mapPasswordToCredentials(password);
        UserRepresentation user = new UserRepresentation();
        user.setId(employee.getEmployeeId().toString());
        user.setFirstName(employee.getName());
        user.setLastName(employee.getSurname());
        user.setUsername(username);
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        UsersResource instance = getInstance();
        instance.create(user);

        addRoles(username, workstation);
        return new Credentials(username, password);
    }

    private void addRoles(String username, WorkstationEntity workstation) {
        UserResource userResource = keycloakService.getUser(username);
        List<RoleRepresentation> roles;
        for (String clientId : keycloakService.getClients()) {
            ClientRepresentation clientRepresentation = keycloakService.getClientRep(clientId);
            if (Objects.equals(workstation.getName(), ADMIN)) {
                roles = keycloakService.getRoles(new String[]{"admin"}, clientRepresentation.getId());
            } else {
                roles = keycloakService.getRoles(new String[]{"manager"}, clientRepresentation.getId());
            }
            userResource.roles().clientLevel(clientRepresentation.getId()).add(roles);
        }
    }
}
