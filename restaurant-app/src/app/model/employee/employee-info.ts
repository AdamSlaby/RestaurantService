import {EmployeeShortInfo} from "./employee-short-info";
import {Address} from "../address";
import {RestaurantShortInfo} from "../restaurant/restaurant-short-info";
import {ScheduleInfo} from "../schedule/schedule-info";

export interface EmployeeInfo {
  shortInfo: EmployeeShortInfo;
  phoneNr: string;
  pesel: string;
  accountNr: string;
  salary: number;
  active: boolean;
  employmentDate: Date | any;
  dismissalDate: any;
  address: Address;
  restaurantInfo: RestaurantShortInfo;
  schedulesInfo: ScheduleInfo[];
}
