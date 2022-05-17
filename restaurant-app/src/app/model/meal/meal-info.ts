import {IngredientView} from "./ingredient-view";

export interface MealInfo {
  id: number;
  name: string;
  typeId: number;
  price: number;
  imageUrl: string;
  ingredients: IngredientView[];
}
