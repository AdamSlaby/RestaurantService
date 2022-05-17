import { Unit } from "./unit";

export interface IngredientView {
  id: number;
  name: string;
  amount: number;
  unit: Unit | any;
}
