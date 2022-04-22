import {Address} from "../address";
import {OpeningHour} from "../opening-hour";
import {Table} from "../table";

export interface Restaurant {
  restaurantId: number;
  address: Address;
  email: string;
  phoneNr: string;
  deliveryFee: number;
  minimalDeliveryPrice: number;
  openingHours: OpeningHour[];
  tables: Table[];
}
