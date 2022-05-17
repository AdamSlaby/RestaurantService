import {Unit} from "../meal/unit";

export interface SupplyInfo {
  restaurantId: any;
  ingredientId: number;
  ingredientName: string;
  quantity: number;
  unit: Unit;
}
