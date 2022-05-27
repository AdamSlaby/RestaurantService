import {AfterViewInit, ChangeDetectorRef, Component, Input, NgZone, OnInit} from '@angular/core';
import {ChartGenerateDataOptions} from "../../model/chart/chart-generate-data-options";
import {Chart} from "../../model/chart/chart";
import {RestaurantShortInfo} from "../../model/restaurant/restaurant-short-info";
import {PeriodType} from "../../model/period-type";
import {ChartType} from "../../model/chart/chart-type";
import {OrderType} from "../../model/order-type";
import {LegendPosition} from "@swimlane/ngx-charts";
import {NgbDateAdapter} from "@ng-bootstrap/ng-bootstrap";
import {NgbDateToStringAdapter} from "../../adapter/datepicker-string-adapter";
import {ChartName} from "../../model/chart/chart-name";
import {RestaurantService} from "../../service/restaurant.service";
import { GenerateChartOptions } from 'src/app/model/chart/generate-chart-options';

@Component({
  selector: 'app-statistic',
  templateUrl: './statistic.component.html',
  styleUrls: ['./statistic.component.scss'],
  providers: [{provide: NgbDateAdapter, useClass: NgbDateToStringAdapter}],
})
export class StatisticComponent implements OnInit, AfterViewInit {
  @Input() set chartName(value: string) {
    let chartName = value as unknown as ChartName;
    this.getChartData(chartName);
  }

  @Input() periodOption!: boolean;
  @Input() restaurantIdOption!: boolean;
  @Input() chartTypeOption!: boolean;
  @Input() orderTypeOption!: boolean;

  @Input() set defaultChartType(value: ChartType) {
    if (value !== null) {
      this.chartOptionsValues.chartType = value;
      this.selectedChartType = value;
    }
  }

  chartOptionsValues: ChartGenerateDataOptions = {
    periodType: PeriodType.DAY,
    period: new Date().toUTCString(),
    placeId: localStorage.getItem('restaurantId'),
    chartType: ChartType.VERTICAL_BAR_CHART,
    orderType: OrderType.ALL,
  };
  view!: [number, number];
  monthArr = Array.from({length: 12}, (_, i) => i + 1);
  yearArr = Array.from({length: 10}, (_, i) => new Date().getFullYear() - i);
  periodType = PeriodType;
  chartType = ChartType;
  position = LegendPosition;
  observer!: any;
  selectedChartType!: ChartType;
  chartTypes!: ChartType[];
  orderTypes = Object.values(OrderType);
  chartData!: Chart;
  restaurants!: RestaurantShortInfo[]

  constructor(private zone: NgZone, private cd: ChangeDetectorRef,
              private restaurantService: RestaurantService) {
  }

  ngOnInit(): void {
    let types = Object.values(ChartType);
    let index = types.length - 1;
    types.splice(index, 1);
    this.chartTypes = types;
    this.restaurantService.getAllRestaurants().subscribe(data => {
      this.restaurants = data;
    }, error => {
      console.error(error);
    });
  }

  ngAfterViewInit() {
    this.view = [this.getElementWidth('chart'), 400];
    this.cd.detectChanges();
    this.observer = new ResizeObserver(entries => {
      this.zone.run(() => {
        this.view = [entries[0].contentRect.width, 400];
        this.cd.detectChanges();
      });
    });
    this.observer.observe(document.getElementById('chart'));
  }

  getElementWidth(id: string) {
    let element = document.getElementById(id);
    if (element) {
      return element.getBoundingClientRect().width;
    } else
      return 0;
  }

  getChartData(chartName: ChartName) {
    this.generateChart(chartName);
  }

  getSoldDishIncomeData(): void {

  }

  getCompletionTimeData(): void {

  }

  getOrdersAmountData(): void {

  }

  periodChanged() {
    this.chartOptionsValues.period = null;
  }

  generateChart(chartName: ChartName) {
    //todo
    this.selectedChartType = this.chartOptionsValues.chartType;
    let options: GenerateChartOptions = {
      periodType: this.chartOptionsValues.periodType,
      period: this.chartOptionsValues.period,
      placeId: this.chartOptionsValues.placeId,
      chartName: chartName,
      orderType: this.chartOptionsValues.orderType
    };
  }

  mapDataToBarOrPieChartData(data: Chart) {
    return data.series;
  }
}
