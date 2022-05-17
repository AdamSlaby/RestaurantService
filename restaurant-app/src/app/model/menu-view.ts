import {MealShortView} from "./meal/meal-short-view";

export interface MenuView {
  id: number;
  season: string,
  mealMap: Map<string, MealShortView[]>;
}
