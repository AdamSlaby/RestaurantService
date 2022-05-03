package pl.restaurant.employeeservice.business.service;

import lombok.AllArgsConstructor;
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
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final static int AMOUNT = 10;
    private EmployeeRepo employeeRepo;
    private ScheduleRepo scheduleRepo;
    private RestaurantServiceClient restaurantServiceClient;
    private AddressRepo addressRepo;
    private WorkstationRepo workstationRepo;
    @Override
    public EmployeeListView getEmployees(Filter filter) {
        Pageable pageable = mapSortEventToPageable(filter);
        Page<EmployeeShortInfo> page = employeeRepo.getEmployees(filter.getRestaurantId(), filter.getEmployeeId(),
                filter.getActive(), filter.getSurname(), filter.getWorkstation().toString(),
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
    public String logIn(Credentials credentials) {
        //todo
        return null;
    }

    @Override
    public void logOut() {
        //todo
    }

    @Override
    public ScheduleInfo addScheduleForEmployee(Schedule schedule) {
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
    public Credentials addEmployee(Employee employee) {
        validateEmployee(employee);
        if (!restaurantServiceClient.isRestaurantExist(employee.getRestaurantId()))
            throw new RestaurantNotFoundException();
        AddressEntity address = addressRepo.save(AddressMapper.mapObjectToData(employee.getAddress()));
        WorkstationEntity workstation = workstationRepo.findById(employee.getWorkstationId())
                .orElseThrow(WorkstationNotFoundException::new);
        EmployeeEntity savedEmployee = employeeRepo
                .save(EmployeeMapper.mapObjectToData(employee, address, workstation));
        return null;
    }

    @Override
    public void updateEmployeeSchedule(ScheduleInfo scheduleInfo) {
        ScheduleEntity schedule = scheduleRepo.findById(scheduleInfo.getId())
                .orElseThrow(ScheduleNotFoundException::new);
        schedule.setStartShift(scheduleInfo.getStartShift());
        schedule.setEndShift(scheduleInfo.getEndShift());
        scheduleRepo.save(schedule);
    }

    @Override
    public void updateEmployee(Employee employee, Long employeeId) {
        validateEmployee(employee);
        if (!restaurantServiceClient.isRestaurantExist(employee.getRestaurantId()))
            throw new RestaurantNotFoundException();
        EmployeeEntity employeeEntity = employeeRepo.findById(employeeId)
                .orElseThrow(EmployeeNotFoundException::new);
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
    }

    @Override
    public void dismissEmployee(Long employeeId) {
        EmployeeEntity employee = employeeRepo.findById(employeeId)
                .orElseThrow(EmployeeNotFoundException::new);
        employee.setActive(false);
        employee.setDismissalDate(LocalDate.now());
        employeeRepo.save(employee);
    }

    @Override
    @Transactional
    public void removeEmployeeSchedule(Long scheduleId) {
        scheduleRepo.deleteById(scheduleId);
    }

    private Pageable mapSortEventToPageable(Filter filter) {
        if (filter.getSortEvent() == null) {
            return PageRequest.of(filter.getPageNr(), AMOUNT);
        } else {
            try {
                String column = filter.getSortEvent().getColumn();
                Employee.class.getField(column);
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
}
