import {ChartData} from "./chart-data";

export interface Chart {
  Xlabel: string;
  Ylabel: string;
  name: string;
  series: ChartData[];
}
