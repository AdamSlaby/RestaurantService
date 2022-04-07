import {EmployeeShortInfo} from "./employee-short-info";

export interface EmployeeList {
  maxPage: number;
  employees: EmployeeShortInfo[];
}
