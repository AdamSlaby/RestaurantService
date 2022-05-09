package pl.restaurant.employeeservice.business.service;

import pl.restaurant.employeeservice.api.request.*;
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

    void logOut(LogoutRequest request) throws ServletException;

    ScheduleInfo addScheduleForEmployee(Schedule schedule);

    Credentials addEmployee(Employee employee);

    ScheduleInfo updateEmployeeSchedule(ScheduleInfo scheduleInfo);

    void updateEmployee(Employee employee, Long employeeId);

    void dismissEmployee(Long employeeId);

    void removeEmployeeSchedule(Long scheduleId);
}
