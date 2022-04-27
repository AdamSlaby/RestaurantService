import {Unit} from "../unit";

export interface SupplyInfo {
  restaurantId: any;
  ingredientId: number;
  ingredientName: string;
  quantity: number;
  unit: Unit;
}
