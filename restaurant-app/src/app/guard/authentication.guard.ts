import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {EmployeeService} from "../service/employee.service";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationGuard implements CanActivate {
  constructor(private employeeService: EmployeeService) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.employeeService.isLoggedIn()) {
      const role = localStorage.getItem('role');
      if (route.data && !route.data['role'].includes(role)) {
        location.replace('/login');
        return false;
      } else
        return true;
    } else {
      location.replace('/login');
      return false;
    }
  }
}
