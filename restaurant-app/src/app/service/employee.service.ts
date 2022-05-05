import {Injectable} from '@angular/core';
import {GeneralService} from "./general.service";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {EmployeeFilters} from "../model/employee/employee-filters";
import {EmployeeListView} from "../model/employee/employee-list-view";
import {EmployeeInfo} from "../model/employee/employee-info";
import {Schedule} from "../model/schedule/schedule";
import {ScheduleInfo} from "../model/schedule/schedule-info";
import {Credentials} from "../model/credentials";
import {Employee} from "../model/employee/employee";

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private COMMON_URL = `${GeneralService.BASE_URL}/employee`;
  private GET_EMPLOYEES = `${this.COMMON_URL}/list`;
  private GET_EMPLOYEE_INFO = `${this.COMMON_URL}/info/`;
  private ADD_SCHEDULE_FOR_EMPLOYEE = `${this.COMMON_URL}/schedule/new`;
  private LOGIN = `${this.COMMON_URL}/login`;
  private LOGOUT = `${this.COMMON_URL}/logout`;
  private ADD_EMPLOYEE = `${this.COMMON_URL}/`;
  private UPDATE_EMPLOYEE_SCHEDULE = `${this.COMMON_URL}/schedule`;
  private UPDATE_EMPLOYEE = `${this.COMMON_URL}/`;
  private DISMISS_EMPLOYEE = `${this.COMMON_URL}/dsmiss/`;
  private REMOVE_EMPLOYEE_SCHEDULE = `${this.COMMON_URL}/schedule/`;

  constructor(private http: HttpClient) { }

  getEmployees(filter: EmployeeFilters): Observable<EmployeeListView> {
    // const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('token')});
    return this.http.post<EmployeeListView>(this.GET_EMPLOYEES, filter);
  }

  getEmployeeInfo(employeeId: any): Observable<EmployeeInfo> {
    return this.http.get<EmployeeInfo>(this.GET_EMPLOYEE_INFO + employeeId);
  }

  addScheduleForEmployee(schedule: Schedule): Observable<ScheduleInfo> {
    // const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('token')});
    return this.http.post<ScheduleInfo>(this.ADD_SCHEDULE_FOR_EMPLOYEE, schedule);
  }

  login(credentials: Credentials): Observable<string> {
    // const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('token')});
    return this.http.post<string>(this.LOGIN, credentials);
  }

  logout(): Observable<any> {
    // const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('token')});
    return this.http.get<any>(this.LOGOUT);
  }

  addEmployee(employee: Employee): Observable<Credentials> {
    // const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('token')});
    return this.http.post<Credentials>(this.ADD_EMPLOYEE, employee);
  }

  updateEmployeeSchedule(schedule: ScheduleInfo): Observable<any> {
    // const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('token')});
    return this.http.put<any>(this.UPDATE_EMPLOYEE_SCHEDULE, schedule);
  }

  updateEmployee(employee: Employee, employeeId: number): Observable<any> {
    // const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('token')});
    return this.http.put<any>(this.UPDATE_EMPLOYEE + employeeId, employee);
  }

  dismissEmployee(employeeId: number): Observable<any> {
    // const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('token')});
    return this.http
      .delete<any>(this.DISMISS_EMPLOYEE + employeeId);
  }

  removeEmployeeSchedule(scheduleId: number): Observable<any> {
    // const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('token')});
    return this.http
      .delete<any>(this.REMOVE_EMPLOYEE_SCHEDULE + scheduleId);
  }
}
