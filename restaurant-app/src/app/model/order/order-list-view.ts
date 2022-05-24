import {OrderShortInfo} from "./order-short-info";

export interface OrderListView {
  totalElements: number;
  orders: OrderShortInfo[];
}
