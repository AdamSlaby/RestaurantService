package pl.restaurant.employeeservice.integration;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;
import pl.restaurant.employeeservice.api.response.EmployeeShortInfo;
import pl.restaurant.employeeservice.data.entity.EmployeeEntity;
import pl.restaurant.employeeservice.data.entity.WorkstationEntity;
import pl.restaurant.employeeservice.data.repository.EmployeeRepo;
import pl.restaurant.employeeservice.data.repository.WorkstationRepo;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepoTest {
    @Autowired
    private EmployeeRepo repo;

    @Autowired
    private WorkstationRepo workstationRepo;

    @MockBean
    private RestTemplate restTemplate;

    private Long employeeId;
    private Integer workstationId;


    @BeforeEach
    public void setUp() {
        EmployeeEntity employee = repo.save(getEmployee(1, true, "Kowalski"));
        this.employeeId = employee.getEmployeeId();
        this.workstationId = employee.getWorkstation().getWorkstationId();
        repo.save(getEmployee(1, false, "Marciniak"));
        repo.save(getEmployee(2, true, "Kowalski"));
    }

    @Test
    public void getEmployeesByEmployeeIdSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<EmployeeShortInfo> page = repo
                .getEmployees(1L, employeeId, true, null, null, pageable);
        //then
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).getSurname()).isEqualTo("Kowalski");
    }

    @Test
    public void getEmployeesByRestaurantIdSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<EmployeeShortInfo> page = repo
                .getEmployees(1L, null, null, null, null, pageable);
        //then
        assertThat(page.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void getEmployeesByActiveSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<EmployeeShortInfo> page = repo
                .getEmployees(null, null, false, null, null, pageable);
        //then
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).getSurname()).isEqualTo("Marciniak");
    }

    @Test
    public void getEmployeesByWorkstationIdSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<EmployeeShortInfo> page = repo
                .getEmployees(null, null, null, null, this.workstationId, pageable);
        //then
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).getSurname()).isEqualTo("Kowalski");
    }

    @Test
    public void getEmployeesByAllPropertiesSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<EmployeeShortInfo> page = repo
                .getEmployees(1L, employeeId, true, "Kowalski", this.workstationId, pageable);
        //then
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).getSurname()).isEqualTo("Kowalski");
    }

    @Test
    public void getEmptyListOfEmployeesByAllPropertiesSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<EmployeeShortInfo> page = repo
                .getEmployees(1L, employeeId, true, "Mirkowiak", this.workstationId, pageable);
        //then
        assertThat(page.getTotalElements()).isEqualTo(0);
    }

    private EmployeeEntity getEmployee(long restaurantId, boolean active, String surname) {
        EasyRandom generator = new EasyRandom(getParameters());
        EmployeeEntity employee = generator.nextObject(EmployeeEntity.class);
        employee.setRestaurantId(restaurantId);
        employee.setActive(active);
        employee.setSurname(surname);
        employee.setSalary(BigDecimal.valueOf(100.00));
        employee.getWorkstation().setEmployees(null);
        WorkstationEntity workstation = workstationRepo.save(employee.getWorkstation());
        employee.setWorkstation(workstation);
        employee.getAddress().setEmployee(null);
        return employee;
    }

    private EasyRandomParameters getParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(3, 3);
        parameters.collectionSizeRange(1, 1);
        return parameters;
    }
}
