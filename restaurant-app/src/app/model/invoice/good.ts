import {Unit} from "../meal/unit";
import {TaxType} from "./tax-type";

export interface Good {
  id: number;
  ingredientId: number;
  ingredient: string;
  quantity: number;
  unit: Unit | any;
  unitNetPrice: number;
  discount: number;
  netPrice: number;
  taxType: TaxType | any;
  taxPrice: number;
}
