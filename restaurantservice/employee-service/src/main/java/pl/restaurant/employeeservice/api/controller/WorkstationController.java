package pl.restaurant.employeeservice.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.restaurant.employeeservice.api.response.WorkstationListView;
import pl.restaurant.employeeservice.business.service.WorkstationService;

import java.util.List;

@RestController
@CrossOrigin
@Log4j2
@AllArgsConstructor
public class WorkstationController {
    private WorkstationService workstationService;

    @GetMapping()
    public List<WorkstationListView> getAllWorkstations() {
        log.log(Level.INFO, "Returning all workstations");
        return workstationService.getAllWorkstation();
    }
}
