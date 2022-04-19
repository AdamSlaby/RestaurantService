import {DishShortInfo} from "../dish/dish-short-info";

export interface ActiveOrder {
  id: number;
  dishInfo: DishShortInfo[];
  orderDate: Date;
}
