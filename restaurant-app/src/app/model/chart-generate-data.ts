import {ChartType} from "./chart-type";
import {OrderType} from "./order-type";
import {PeriodType} from "./period-type";

export interface ChartGenerateData {
  periodType: PeriodType;
  period: any;
  placeId: any;
  chartType: ChartType;
  orderType: OrderType;
}
