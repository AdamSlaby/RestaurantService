import { SortEvent } from "../sort-event";

export interface MealFilters {
    typeId: any;
    mealName: any;
    sortEvent: SortEvent | null;
    pageNr: number;
}