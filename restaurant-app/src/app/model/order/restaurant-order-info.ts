import {OrderInfo} from "./order-info";

export interface RestaurantOrderInfo {
  id: number;
  orderDate: Date;
  deliveryDate: Date | any;
  orders: OrderInfo[];
  price: number;
}
