import { OrderType } from "../order-type";
import { PeriodType } from "../period-type";
import { ChartName } from "./chart-name";

export interface GenerateChartOptions {
  periodType: PeriodType;
  period: any;
  placeId: any;
  chartName: ChartName;
  orderType: OrderType;
}