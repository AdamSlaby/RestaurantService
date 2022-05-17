import {Unit} from "../meal/unit";

export interface Supply {
  restaurantId: any;
  ingredientName: string;
  quantity: number;
  unit: Unit;
}
