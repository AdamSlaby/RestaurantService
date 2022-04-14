import {EmployeeShortInfo} from "./employee-short-info";

export interface EmployeeListView {
  maxPage: number;
  totalElements: number;
  employees: EmployeeShortInfo[];
}
