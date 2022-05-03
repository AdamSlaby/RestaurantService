package pl.restaurant.employeeservice.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.employeeservice.api.request.Employee;
import pl.restaurant.employeeservice.api.request.Filter;
import pl.restaurant.employeeservice.api.response.EmployeeListView;
import pl.restaurant.employeeservice.business.service.EmployeeService;

import javax.validation.Valid;

@RestController
@CrossOrigin
@Log4j2
@AllArgsConstructor
public class EmployeeController {
    private EmployeeService employeeService;

    @PostMapping()
    public EmployeeListView getEmployees(@RequestBody @Valid Filter filter) {
        return employeeService.getEmployees(filter);
    }


}
