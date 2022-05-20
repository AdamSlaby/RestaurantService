import { TaxType } from "./tax-type";

export interface Good {
  ingredientId: number;
  quantity: number;
  unitId: number;
  unitNetPrice: number;
  discount: number;
  netPrice: number;
  taxType: TaxType;
  taxPrice: number;
}