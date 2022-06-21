package pl.restaurant.employeeservice.integration;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import pl.restaurant.employeeservice.api.mapper.AddressMapper;
import pl.restaurant.employeeservice.api.mapper.EmployeeMapper;
import pl.restaurant.employeeservice.api.request.Address;
import pl.restaurant.employeeservice.api.request.Credentials;
import pl.restaurant.employeeservice.api.request.Employee;
import pl.restaurant.employeeservice.api.request.Schedule;
import pl.restaurant.employeeservice.business.exception.EmployeeNotFoundException;
import pl.restaurant.employeeservice.business.exception.InvalidPeselException;
import pl.restaurant.employeeservice.business.service.EmployeeService;
import pl.restaurant.employeeservice.business.service.KeycloakService;
import pl.restaurant.employeeservice.business.service.LoginService;
import pl.restaurant.employeeservice.business.service.RestaurantServiceClient;
import pl.restaurant.employeeservice.data.entity.AddressEntity;
import pl.restaurant.employeeservice.data.entity.EmployeeEntity;
import pl.restaurant.employeeservice.data.entity.ScheduleEntity;
import pl.restaurant.employeeservice.data.entity.WorkstationEntity;
import pl.restaurant.employeeservice.data.repository.AddressRepo;
import pl.restaurant.employeeservice.data.repository.EmployeeRepo;
import pl.restaurant.employeeservice.data.repository.ScheduleRepo;
import pl.restaurant.employeeservice.data.repository.WorkstationRepo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class EmployeeServiceTest {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private WorkstationRepo workstationRepo;

    @MockBean
    private KeycloakService keycloakService;

    @MockBean
    private LoginService loginService;

    @MockBean
    private RestaurantServiceClient client;

    @MockBean
    private RestTemplate restTemplate;

    @AfterEach
    public void clear() {
        employeeRepo.deleteAll();
    }

    @Test
    public void addOrdinaryEmployeeSuccess() {
        //given
        Employee employee = getEmployee("Kucharz");
        //when
        when(client.isRestaurantExist(any(Long.class))).thenReturn(true);
        Credentials credentials = employeeService.addEmployee(employee);
        List<EmployeeEntity> all = employeeRepo.findAll();
        //then
        assertNull(credentials.getUsername());
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getPesel()).isEqualTo("06222157267");
    }

    @Test
    public void addOrdinaryEmployeeFailure() {
        //given
        Employee employee = getEmployee("Administrator");
        employee.setPesel("06222157266");
        //when
        when(client.isRestaurantExist(any(Long.class))).thenReturn(true);
        //then
        assertThrows(InvalidPeselException.class, () -> employeeService.addEmployee(employee));
    }

    @Test
    public void dismissEmployeeSuccess() {
        //given
        Employee employee = getEmployee("Kucharz");
        AddressEntity address = AddressMapper.mapObjectToData(employee.getAddress());
        WorkstationEntity workstation = workstationRepo.findById(employee.getWorkstationId()).get();
        Long employeeId = employeeRepo.save(EmployeeMapper.mapObjectToData(employee, address, workstation)).getEmployeeId();
        //when
        employeeService.dismissEmployee(employeeId);
        EmployeeEntity updatedEmployee = employeeRepo.findById(employeeId).get();
        //then
        assertNotNull(updatedEmployee);
        assertThat(updatedEmployee.getDismissalDate()).isEqualTo(LocalDate.now());
        assertFalse(updatedEmployee.isActive());
    }

    @Test
    public void dismissEmployeeFailure() {
        //given
        //when
        //then
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.dismissEmployee(-1L));
    }

    @Test
    public void addScheduleForEmployeeSuccess() {
        //given
        Employee employee = getEmployee("Kucharz");
        AddressEntity address = AddressMapper.mapObjectToData(employee.getAddress());
        WorkstationEntity workstation = workstationRepo.findById(employee.getWorkstationId()).get();
        Long employeeId = employeeRepo.save(EmployeeMapper.mapObjectToData(employee, address, workstation)).getEmployeeId();
        Schedule schedule = getSchedule(employeeId);
        //when
        employeeService.addScheduleForEmployee(schedule);
        Optional<ScheduleEntity> employeeSchedule = scheduleRepo
                .getScheduleByDateAndEmployeeId(schedule.getStartShift(), schedule.getEndShift(), schedule.getEmployeeId());
        //then
        assertTrue(employeeSchedule.isPresent());
        assertNotNull(employeeSchedule.get());
        assertEquals(schedule.getStartShift(), employeeSchedule.get().getStartShift());
    }

    @Test
    public void addScheduleForEmployeeFailure() {
        //given
        Schedule schedule = getSchedule(-1L);
        //when
        //then
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.addScheduleForEmployee(schedule));
    }

    private Schedule getSchedule(Long employeeId) {
        return new Schedule().builder()
                .employeeId(employeeId)
                .startShift(LocalDateTime.now().withHour(8).withMinute(0).withSecond(0).withNano(0))
                .endShift(LocalDateTime.now().withHour(16).withMinute(0).withSecond(0).withNano(0))
                .build();
    }

    private Employee getEmployee(String role) {
        return new Employee().builder()
                .restaurantId(1L)
                .name("Marek")
                .surname("Kowalski")
                .workstationId(getWorkstationId(role))
                .phoneNr("300 300 300")
                .pesel("06222157267")
                .accountNr("72109024025463152737292491")
                .salary(BigDecimal.valueOf(3000))
                .active(true)
                .employmentDate(LocalDate.now())
                .dismissalDate(null)
                .address(getAddress())
                .build();
    }

    private Integer getWorkstationId(String role) {
        EasyRandom easyRandom = new EasyRandom(getParameters());
        WorkstationEntity workstationEntity = easyRandom.nextObject(WorkstationEntity.class);
        workstationEntity.setName(role);
        workstationEntity.setEmployees(null);
        return workstationRepo.save(workstationEntity).getWorkstationId();
    }
    
    private Address getAddress() {
        EasyRandom easyRandom = new EasyRandom(getParameters());
        return easyRandom.nextObject(Address.class);
    }

    private EasyRandomParameters getParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(3, 3);
        parameters.collectionSizeRange(1, 1);
        return parameters;
    }
}
