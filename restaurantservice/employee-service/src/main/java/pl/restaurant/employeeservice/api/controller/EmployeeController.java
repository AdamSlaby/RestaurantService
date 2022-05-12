package pl.restaurant.employeeservice.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.employeeservice.api.request.*;
import pl.restaurant.employeeservice.api.response.EmployeeInfo;
import pl.restaurant.employeeservice.api.response.EmployeeListView;
import pl.restaurant.employeeservice.api.response.LoginResponse;
import pl.restaurant.employeeservice.api.response.ScheduleInfo;
import pl.restaurant.employeeservice.business.service.EmployeeService;

import javax.servlet.ServletException;
import javax.validation.Valid;

@RestController
@CrossOrigin
@Log4j2
@AllArgsConstructor
public class EmployeeController {
    private EmployeeService employeeService;

    @PostMapping("/list")
    public EmployeeListView getEmployees(@RequestBody @Valid EmployeeFilters filter) {
        return employeeService.getEmployees(filter);
    }

    @GetMapping("/info/{id}")
    public EmployeeInfo getEmployeeInfo(@PathVariable("id") Long employeeId) {
        return employeeService.getEmployeeInfo(employeeId);
    }

    @GetMapping("/exist/{id}")
    public boolean isAdminEmployeeExist(@PathVariable("id") Long employeeId) {
        return employeeService.isAdminEmployeeExist(employeeId);
    }

    @GetMapping("/name/{id}")
    public String getEmployeeFullName(@PathVariable("id") Long employeeId) {
        return employeeService.getEmployeeFullName(employeeId);
    }

    @PostMapping("/schedule/new")
    public ScheduleInfo addScheduleForEmployee(@RequestBody @Valid Schedule schedule) {
        return employeeService.addScheduleForEmployee(schedule);
    }

    @PostMapping("/login")
    public LoginResponse logIn(@RequestBody @Valid Credentials credentials) {
        return employeeService.logIn(credentials);
    }

    @PostMapping("/logout")
    public void logOut(@RequestBody @Valid LogoutRequest logoutRequest) throws ServletException {
        employeeService.logOut(logoutRequest);
    }

    @PostMapping()
    public Credentials addEmployee(@RequestBody @Valid Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @PutMapping("/schedule")
    public ScheduleInfo updateEmployeeSchedule(@RequestBody @Valid ScheduleInfo scheduleInfo) {
        return employeeService.updateEmployeeSchedule(scheduleInfo);
    }

    @PutMapping("/{id}")
    public void updateEmployee(@RequestBody @Valid Employee employee,
                               @PathVariable("id") Long employeeId) {
        employeeService.updateEmployee(employee, employeeId);
    }

    @DeleteMapping("/dismiss/{id}")
    public void dismissEmployee(@PathVariable("id") Long employeeId) {
        employeeService.dismissEmployee(employeeId);
    }

    @DeleteMapping("/schedule/{id}")
    public void removeEmployeeSchedule(@PathVariable("id") Long scheduleId) {
        employeeService.removeEmployeeSchedule(scheduleId);
    }
}
