import {Ingredient} from "./ingredient";

export interface MealInfo {
  id: number;
  name: string;
  typeId: number;
  price: number;
  imageUrl: string;
  ingredients: Ingredient[];
}
