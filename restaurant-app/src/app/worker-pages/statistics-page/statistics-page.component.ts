import {Component, OnInit} from '@angular/core';
import {ChartType} from "../../model/chart/chart-type";
import {ChartName} from "../../model/chart/chart-name";
import {ChartOptions} from "../../model/chart/chart-options";

@Component({
  selector: 'app-statistics-page',
  templateUrl: './statistics-page.component.html',
  styleUrls: ['./statistics-page.component.scss'],
})
export class StatisticsPageComponent implements OnInit {
  chartOptions!: ChartOptions[];
  chartId: number = 0;
  charts = Object.values(ChartName);

  constructor() {
  }

  ngOnInit(): void {
    this.chartOptions = [
      {
        defaultChartType: ChartType.PIE_CHART,
        periodOption: true,
        restaurantIdOption: true,
        orderTypeOption: false,
        chartTypeOption: true,
      },
      {
        defaultChartType: ChartType.PIE_CHART,
        periodOption: true,
        restaurantIdOption: true,
        orderTypeOption: false,
        chartTypeOption: true,
      },
      {
        defaultChartType: ChartType.VERTICAL_BAR_CHART,
        periodOption: true,
        restaurantIdOption: true,
        orderTypeOption: true,
        chartTypeOption: true,
      },
      {
        defaultChartType: ChartType.AREA_CHART,
        periodOption: true,
        restaurantIdOption: true,
        orderTypeOption: true,
        chartTypeOption: false,
      },
      {
        defaultChartType: ChartType.VERTICAL_BAR_CHART,
        periodOption: true,
        restaurantIdOption: true,
        orderTypeOption: true,
        chartTypeOption: true,
      },
      {
        defaultChartType: ChartType.VERTICAL_BAR_CHART,
        periodOption: true,
        restaurantIdOption: true,
        orderTypeOption: false,
        chartTypeOption: true,
      },
      {
        defaultChartType: ChartType.VERTICAL_BAR_CHART,
        periodOption: true,
        restaurantIdOption: true,
        orderTypeOption: true,
        chartTypeOption: false,
      },
      {
        defaultChartType: ChartType.VERTICAL_BAR_CHART,
        periodOption: true,
        restaurantIdOption: true,
        orderTypeOption: true,
        chartTypeOption: true,
      },
    ]
  }
}
