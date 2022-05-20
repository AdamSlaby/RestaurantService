import {Address} from "../address";
import {GoodView} from "./good-view";
import {RestaurantShortInfo} from "../restaurant/restaurant-short-info";

export interface InvoiceView {
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
  goods: GoodView[];
}
