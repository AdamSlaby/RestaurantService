import {Unit} from "../unit";

export interface SupplyInfo {
  id: number;
  restaurantId: number;
  ingredientId: number;
  ingredientName: string;
  quantity: number;
  unit: Unit;
  supplyDate: Date;
}
