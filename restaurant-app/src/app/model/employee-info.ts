import {EmployeeShortInfo} from "./employee-short-info";
import {Address} from "./address";
import {AppraisalInfo} from "./appraisal-info";
import {RestaurantShortInfo} from "./restaurant-short-info";
import {ScheduleInfo} from "./schedule-info";

export interface EmployeeInfo {
  shortInfo: EmployeeShortInfo;
  phoneNr: string;
  accountNr: string;
  salary: number;
  active: boolean;
  employmentDate: Date;
  dismissalDate: any;
  address: Address;
  appraisalInfo: AppraisalInfo[];
  restaurantInfo: RestaurantShortInfo;
  scheduleInfo: ScheduleInfo[];
}
