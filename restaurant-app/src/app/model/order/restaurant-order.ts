import {Order} from "./order";

export interface RestaurantOrder {
  restaurantId: any;
  tableId: number;
  orders: Order[];
}
