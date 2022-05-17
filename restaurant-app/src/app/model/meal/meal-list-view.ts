import {MealShortInfo} from "./meal-short-info";

export interface MealListView {
  totalElements: number;
  meals: MealShortInfo[];
}
