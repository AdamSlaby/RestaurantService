import {Address} from "../address";
import {PaymentMethod} from "../payment-method";
import {OrderInfo} from "./order-info";

export interface OnlineOrderInfo {
  id: number;
  name: string;
  surname: string;
  email: string;
  phoneNr: string;
  address: Address;
  floor: number | any;
  orderDate: Date;
  deliveryDate: Date | any;
  paymentMethod: PaymentMethod;
  orders: OrderInfo[];
  price: number;
}
