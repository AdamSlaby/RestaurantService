import {EmployeeShortInfo} from "./employee-short-info";

export interface EmployeeListView {
  totalElements: number;
  employees: EmployeeShortInfo[];
}
