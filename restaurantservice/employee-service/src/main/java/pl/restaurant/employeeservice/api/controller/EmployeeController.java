package pl.restaurant.employeeservice.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import pl.restaurant.employeeservice.business.service.EmployeeService;

@RestController
@CrossOrigin
@Log4j2
@AllArgsConstructor
public class EmployeeController {
    private EmployeeService employeeService;
}
