import {DishShortInfo} from "./dish-short-info";

export interface ActiveOrder {
  id: number;
  dishInfo: DishShortInfo[];
  orderDate: Date;
}
