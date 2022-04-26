import {Address} from "../address";
import {Good} from "./good";
import {RestaurantShortInfo} from "../restaurant/restaurant-short-info";

export interface Invoice {
  nr: string;
  restaurantId: any;
  restaurantInfo: RestaurantShortInfo | null;
  date: Date;
  sellerName: string;
  buyerName: string;
  sellerAddress: Address;
  buyerAddress: Address;
  sellerNip: string;
  buyerNip: string;
  completionDate: Date;
  price: number;
  goods: Good[];
}
