import {ChartType} from "./chart-type";
import {OrderType} from "./order-type";

export interface ChartGenerateData {
  period: string;
  placeId: any;
  chartType: ChartType;
  orderType: OrderType;
}
