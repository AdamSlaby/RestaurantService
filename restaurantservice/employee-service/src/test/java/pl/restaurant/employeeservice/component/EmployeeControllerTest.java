package pl.restaurant.employeeservice.component;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import pl.restaurant.employeeservice.api.mapper.AddressMapper;
import pl.restaurant.employeeservice.api.request.Employee;
import pl.restaurant.employeeservice.api.response.EmployeeInfo;
import pl.restaurant.employeeservice.api.response.RestaurantShortInfo;
import pl.restaurant.employeeservice.business.service.RestaurantServiceClient;
import pl.restaurant.employeeservice.data.entity.AddressEntity;
import pl.restaurant.employeeservice.data.entity.EmployeeEntity;
import pl.restaurant.employeeservice.data.entity.WorkstationEntity;
import pl.restaurant.employeeservice.data.repository.EmployeeRepo;
import pl.restaurant.employeeservice.data.repository.WorkstationRepo;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EmployeeControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private WorkstationRepo workstationRepo;

    @MockBean
    private RestaurantServiceClient client;

    @LocalServerPort
    private int port;

    private URL url;
    private Long employeeId;
    private Integer workstationId;

    @BeforeEach
    public void fillDatabase() throws Exception {
        EmployeeEntity employee = employeeRepo.save(getEmployee("Kucharz"));
        this.employeeId = employee.getEmployeeId();
        this.workstationId = employee.getWorkstation().getWorkstationId();
        this.url = new URL("http://localhost:" + port + "/");
    }

    @AfterEach
    public void clear() {
        employeeRepo.deleteAll();
    }

    @Test
    public void getEmployeeInfoSuccess() {
        //given
        RestaurantShortInfo shortInfo = new EasyRandom(getParameters()).nextObject(RestaurantShortInfo.class);
        //when
        when(client.getRestaurantShortInfo(any(Long.class))).thenReturn(shortInfo);
        ResponseEntity<EmployeeInfo> response = restTemplate
                .getForEntity(url + "info/" + this.employeeId, EmployeeInfo.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getPesel()).isEqualTo("06222157267");
    }

    @Test
    public void getEmployeeInfoFailure() {
        //given
        //when
        ResponseEntity<EmployeeInfo> response = restTemplate
                .getForEntity(url + "info/" + 0, EmployeeInfo.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateEmployeeSuccess() {
        //given
        Employee employee = mapDataToObject(getEmployee("Kelner"));
        String pesel = "75072666566";
        employee.setPesel(pesel);
        employee.setWorkstationId(this.workstationId);
        HttpEntity<Employee> httpEntity = new HttpEntity<>(employee);
        //when
        when(client.isRestaurantExist(any(Long.class))).thenReturn(true);
        ResponseEntity<String> response = restTemplate
                .exchange(url + this.employeeId.toString(), HttpMethod.PUT, httpEntity, String.class);
        EmployeeEntity updatedEmployee = employeeRepo.findById(this.employeeId).get();
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedEmployee.getPesel()).isEqualTo(pesel);
    }

    @Test
    public void updateEmployeeFailureWorkstationNotFound() {
        //given
        Employee employee = mapDataToObject(getEmployee("Kelner"));
        employee.setWorkstationId(0);
        HttpEntity<Employee> httpEntity = new HttpEntity<>(employee);
        //when
        when(client.isRestaurantExist(any(Long.class))).thenReturn(true);
        ResponseEntity<String> response = restTemplate
                .exchange(url + this.employeeId.toString(), HttpMethod.PUT, httpEntity, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private EmployeeEntity getEmployee(String role) {
        return new EmployeeEntity().builder()
                .restaurantId(1L)
                .name("Marek")
                .surname("Kowalski")
                .workstation(getWorkstation(role))
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

    private Employee mapDataToObject(EmployeeEntity employee) {
        return new Employee().builder()
                .restaurantId(employee.getRestaurantId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .workstationId(employee.getWorkstation().getWorkstationId())
                .phoneNr(employee.getPhoneNr())
                .pesel(employee.getPesel())
                .accountNr(employee.getAccountNr())
                .salary(employee.getSalary())
                .active(employee.isActive())
                .employmentDate(employee.getEmploymentDate())
                .dismissalDate(employee.getDismissalDate())
                .address(AddressMapper.mapDataToObject(employee.getAddress()))
                .build();
    }

    private WorkstationEntity getWorkstation(String role) {
        EasyRandom easyRandom = new EasyRandom(getParameters());
        WorkstationEntity workstationEntity = easyRandom.nextObject(WorkstationEntity.class);
        workstationEntity.setName(role);
        workstationEntity.setEmployees(null);
        workstationEntity.setWorkstationId(null);
        return workstationRepo.save(workstationEntity);
    }

    private AddressEntity getAddress() {
        EasyRandom easyRandom = new EasyRandom(getParameters());
        AddressEntity address = easyRandom.nextObject(AddressEntity.class);
        address.setAddressId(null);
        address.setEmployee(null);
        address.setCity("Kielce");
        address.setStreet("Warszawska");
        address.setHouseNr("102");
        address.setFlatNr("10");
        address.setPostcode("25-100");
        return address;
    }

    private EasyRandomParameters getParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(3, 3);
        parameters.collectionSizeRange(1, 1);
        return parameters;
    }
}
