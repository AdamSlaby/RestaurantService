import {Address} from "../address";
import {OpeningHour} from "../opening-hour";

export interface RestaurantInfo {
  email: string;
  phoneNr: string;
  address : Address;
  openingHours: OpeningHour[];
}
