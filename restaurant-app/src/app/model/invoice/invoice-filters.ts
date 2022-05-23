import { SortEvent } from "../sort-event";

export interface InvoiceFilters {
    restaurantId: any;
    nr: any;
    date: any;
    sellerName: any;
    sortEvent: SortEvent | null;
    pageNr: number;
}