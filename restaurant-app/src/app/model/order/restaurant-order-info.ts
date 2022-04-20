import {OrderInfo} from "./order-info";
import {RestaurantShortInfo} from "../restaurant/restaurant-short-info";

export interface RestaurantOrderInfo {
  id: number;
  tableId: number;
  restaurantInfo: RestaurantShortInfo,
  orderDate: Date;
  deliveryDate: Date | any;
  orders: OrderInfo[];
  price: number;
}
