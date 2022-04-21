import {Address} from "../address";

export interface Restaurant {
  restaurantId: number;
  address: Address;
  email: string;
  phoneNr: string;
  deliveryFee: number;
  minimalDeliveryPrice: number;
}
