import { Address } from "../address";
import { Good } from "./good";

export interface Invoice {
  nr: string;
  restaurantId: any;
  date: Date;
  sellerName: string;
  buyerName: string;
  sellerAddress: Address;
  buyerAddress: Address;
  sellerNip: string;
  buyerNip: string;
  completionDate: Date;
  price: number;
  goods: Good[];
}