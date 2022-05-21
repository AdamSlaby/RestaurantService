import {TaxType} from "./tax-type";

export interface GoodView {
  id: number;
  ingredientId: number;
  quantity: number;
  unitId: number | any;
  unitNetPrice: number;
  discount: number;
  netPrice: number;
  taxType: TaxType | any;
  taxPrice: number;
}
