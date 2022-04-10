import {EmployeeShortInfo} from "./employee-short-info";
import {Address} from "./address";
import {RestaurantShortInfo} from "./restaurant-short-info";
import {ScheduleInfo} from "./schedule-info";

export interface EmployeeInfo {
  shortInfo: EmployeeShortInfo;
  phoneNr: string;
  accountNr: string;
  salary: number;
  active: boolean;
  employmentDate: Date | any;
  dismissalDate: any;
  address: Address;
  restaurantInfo: RestaurantShortInfo;
  scheduleInfo: ScheduleInfo[];
}
