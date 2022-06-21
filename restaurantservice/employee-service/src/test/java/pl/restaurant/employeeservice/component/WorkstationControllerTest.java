package pl.restaurant.employeeservice.component;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import pl.restaurant.employeeservice.api.response.WorkstationListView;
import pl.restaurant.employeeservice.data.entity.WorkstationEntity;
import pl.restaurant.employeeservice.data.repository.WorkstationRepo;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class WorkstationControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private WorkstationRepo workstationRepo;

    private URL url;

    @BeforeEach
    public void fillDatabase() throws Exception{
        EasyRandom easyRandom = new EasyRandom(getParameters());
        WorkstationEntity workstation = easyRandom.nextObject(WorkstationEntity.class);
        workstation.setEmployees(null);
        workstationRepo.save(workstation);
        WorkstationEntity workstation2 = easyRandom.nextObject(WorkstationEntity.class);
        workstation2.setEmployees(null);
        workstationRepo.save(workstation2);
        this.url = new URL("http://localhost:" + port + "/");
    }

    @AfterEach
    public void clear() {
        workstationRepo.deleteAll();
    }

    @Test
    public void getAllWorkstationsSuccess() {
        //given
        //when
        ResponseEntity<WorkstationListView[]> response = restTemplate
                .getForEntity(this.url + "workstation", WorkstationListView[].class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    private EasyRandomParameters getParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(3, 3);
        parameters.collectionSizeRange(1, 1);
        return parameters;
    }
}
