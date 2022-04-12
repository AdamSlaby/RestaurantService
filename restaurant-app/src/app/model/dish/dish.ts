import {DishType} from "./dish-type";

export interface Dish {
  id: number;
  name: string;
  type: DishType;
  ingredients: string;
  price: number;
  isBest: boolean;
}
