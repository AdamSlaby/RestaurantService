import {Address} from "../address";
import {PaymentMethod} from "../payment-method";
import {Order} from "./order";

export interface OnlineOrder {
  restaurantId: any;
  name: string;
  surname: string;
  email: string;
  phoneNr: string;
  address: Address;
  floor: number | any;
  paymentMethod: PaymentMethod;
  orders: Order[];
}
