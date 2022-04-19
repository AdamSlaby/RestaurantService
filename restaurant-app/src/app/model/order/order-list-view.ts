import {OrderShortInfo} from "./order-short-info";

export interface OrderListView {
  maxPage: number;
  totalElements: number;
  orders: OrderShortInfo[];
}
