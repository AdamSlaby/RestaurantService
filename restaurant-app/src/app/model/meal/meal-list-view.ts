import {MealShortInfo} from "./meal-short-info";

export interface MealListView {
  maxPage: number;
  totalElements: number;
  meals: MealShortInfo[];
}
