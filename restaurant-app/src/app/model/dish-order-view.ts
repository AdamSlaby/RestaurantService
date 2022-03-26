import {DishType} from "./type";

export interface DishOrderView {
  id: number;
  imageUrl: string;
  name: string;
  type: DishType;
  ingredients: string;
  price: number;
}