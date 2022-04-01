import {Address} from "./address";
import {PaymentMethod} from "./payment-method";

export interface OrderInfo {
  name: string;
  surname: string;
  email: string;
  phoneNr: string;
  address: Address;
  floor: number;
  paymentMethod: PaymentMethod;
}
