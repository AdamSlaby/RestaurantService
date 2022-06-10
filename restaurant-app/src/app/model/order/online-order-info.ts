import {Address} from "../address";
import {PaymentMethod} from "../payment-method";
import {OrderInfo} from "./order-info";
import {RestaurantShortInfo} from "../restaurant/restaurant-short-info";

export interface OnlineOrderInfo {
  id: number;
  restaurantInfo: RestaurantShortInfo
  name: string;
  surname: string;
  email: string;
  phoneNr: string;
  address: Address;
  floor: number | any;
  orderDate: Date;
  paid: boolean;
  deliveryDate: Date | any;
  paymentMethod: PaymentMethod;
  orders: OrderInfo[];
  price: number;
}
