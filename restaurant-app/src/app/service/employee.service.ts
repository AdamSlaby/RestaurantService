import {Injectable} from '@angular/core';
import {GeneralService} from "./general.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {EmployeeFilters} from "../model/employee/employee-filters";
import {EmployeeListView} from "../model/employee/employee-list-view";
import {EmployeeInfo} from "../model/employee/employee-info";
import {Schedule} from "../model/schedule/schedule";
import {ScheduleInfo} from "../model/schedule/schedule-info";
import {Credentials} from "../model/credentials";
import {Employee} from "../model/employee/employee";
import {LoginResponse} from "../model/login-response";
import {WorkstationListView} from "../model/workstation/workstation-list-view";
import { LogoutRequest } from '../model/logout-request';

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
  private DISMISS_EMPLOYEE = `${this.COMMON_URL}/dismiss/`;
  private REMOVE_EMPLOYEE_SCHEDULE = `${this.COMMON_URL}/schedule/`;
  private GET_WORKSTATIONS = `${this.COMMON_URL}/workstation`;

  constructor(private http: HttpClient) { }

  getEmployees(filter: EmployeeFilters): Observable<EmployeeListView> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<EmployeeListView>(this.GET_EMPLOYEES, filter, {headers});
  }

  getEmployeeInfo(employeeId: any): Observable<EmployeeInfo> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<EmployeeInfo>(this.GET_EMPLOYEE_INFO + employeeId, {headers});
  }

  addScheduleForEmployee(schedule: Schedule): Observable<ScheduleInfo> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<ScheduleInfo>(this.ADD_SCHEDULE_FOR_EMPLOYEE, schedule, {headers});
  }

  login(credentials: Credentials): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.LOGIN, credentials);
  }

  logout(): Observable<any> {
    let logoutRequest: LogoutRequest = {
      refreshToken: localStorage.getItem('refreshToken'),
    }
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<any>(this.LOGOUT, logoutRequest, {headers});
  }

  addEmployee(employee: Employee): Observable<Credentials> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<Credentials>(this.ADD_EMPLOYEE, employee, {headers});
  }

  updateEmployeeSchedule(schedule: ScheduleInfo): Observable<ScheduleInfo> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.put<ScheduleInfo>(this.UPDATE_EMPLOYEE_SCHEDULE, schedule, {headers});
  }

  updateEmployee(employee: Employee, employeeId: number): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.put<any>(this.UPDATE_EMPLOYEE + employeeId, employee, {headers});
  }

  dismissEmployee(employeeId: number): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.delete<any>(this.DISMISS_EMPLOYEE + employeeId, {headers});
  }

  removeEmployeeSchedule(scheduleId: number): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.delete<any>(this.REMOVE_EMPLOYEE_SCHEDULE + scheduleId, {headers});
  }

  getWorkstations(): Observable<WorkstationListView[]> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<WorkstationListView[]>(this.GET_WORKSTATIONS, {headers});
  }

  isLoggedIn() {
    return localStorage.getItem('accessToken') !== null;
  }
}
