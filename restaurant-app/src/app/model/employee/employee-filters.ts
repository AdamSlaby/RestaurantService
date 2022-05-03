import {SortEvent} from "../sort-event";

export interface EmployeeFilters {
  restaurantId: any;
  employeeId: any;
  active: any;
  surname: any;
  workstation: any;
  sortEvent: SortEvent | null;
  pageNr: number;
}
