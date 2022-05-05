package pl.restaurant.employeeservice.business.service;

import pl.restaurant.employeeservice.api.request.Credentials;
import pl.restaurant.employeeservice.api.request.Employee;
import pl.restaurant.employeeservice.api.request.Filter;
import pl.restaurant.employeeservice.api.request.Schedule;
import pl.restaurant.employeeservice.api.response.EmployeeInfo;
import pl.restaurant.employeeservice.api.response.EmployeeListView;
import pl.restaurant.employeeservice.api.response.LoginResponse;
import pl.restaurant.employeeservice.api.response.ScheduleInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public interface EmployeeService {
    EmployeeListView getEmployees(Filter filter);

    EmployeeInfo getEmployeeInfo(Long employeeId);

    LoginResponse logIn(Credentials credentials);

    void logOut(HttpServletRequest request) throws ServletException;

    ScheduleInfo addScheduleForEmployee(Schedule schedule);

    Credentials addEmployee(Employee employee);

    void updateEmployeeSchedule(ScheduleInfo scheduleInfo);

    void updateEmployee(Employee employee, Long employeeId);

    void dismissEmployee(Long employeeId);

    void removeEmployeeSchedule(Long scheduleId);
}
