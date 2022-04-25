import {Unit} from "../unit";
import {TaxType} from "./tax-type";

export interface Good {
  id: number;
  ingredientId: number;
  ingredient: string;
  quantity: number;
  unit: Unit;
  unitNetPrice: number;
  discount: number;
  netPrice: number;
  taxType: TaxType;
  taxPrice: number;
}
