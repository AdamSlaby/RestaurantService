import {Season} from "./season";
import {MealShortView} from "./meal/meal-short-view";

export interface MenuView {
  id: number;
  season: Season,
  mealMap: Map<string, MealShortView[]>;
}
