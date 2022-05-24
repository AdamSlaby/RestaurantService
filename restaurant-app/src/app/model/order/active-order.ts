import {DishShortInfo} from "../dish/dish-short-info";
import {OrderType} from "../order-type";

export interface ActiveOrder {
  id: number;
  dishInfo: DishShortInfo[];
  orderType: String;
  orderDate: Date;
}
