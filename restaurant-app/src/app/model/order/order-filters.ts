import { SortEvent } from "../sort-event";

export interface OrderFilters {
    restaurantId: any;
    orderId: any;
    type: string;
    orderDate: any;
    isCompleted: any;
    sortEvent: SortEvent | null;
    pageNr: number;
}