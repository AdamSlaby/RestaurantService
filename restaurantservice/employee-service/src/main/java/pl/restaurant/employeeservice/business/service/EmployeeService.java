package pl.restaurant.employeeservice.business.service;

import pl.restaurant.employeeservice.api.request.Credentials;
import pl.restaurant.employeeservice.api.request.Employee;
import pl.restaurant.employeeservice.api.request.Filter;
import pl.restaurant.employeeservice.api.request.Schedule;
import pl.restaurant.employeeservice.api.response.EmployeeInfo;
import pl.restaurant.employeeservice.api.response.EmployeeListView;
import pl.restaurant.employeeservice.api.response.ScheduleInfo;

public interface EmployeeService {
    EmployeeListView getEmployees(Filter filter);

    EmployeeInfo getEmployeeInfo(Long employeeId);

    String logIn(Credentials credentials);

    void logOut();

    ScheduleInfo addScheduleForEmployee(Schedule schedule);

    Credentials addEmployee(Employee employee);

    void updateEmployeeSchedule(ScheduleInfo scheduleInfo, Long employeeId);

    void updateEmployee(Employee employee);

    void dismissEmployee(Long employeeId);
}
