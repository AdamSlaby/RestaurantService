import { SortEvent } from "../sort-event";

export interface OrderFilters {
    restaurantId: any;
    orderId: any;
    orderDate: any;
    isCompleted: any;
    sortEvent: SortEvent | null;
    pageNr: number;
}