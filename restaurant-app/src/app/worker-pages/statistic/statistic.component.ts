import {AfterViewInit, ChangeDetectorRef, Component, Input, NgZone, OnInit} from '@angular/core';
import {ChartGenerateData} from "../../model/chart/chart-generate-data";
import {Chart} from "../../model/chart/chart";
import {RestaurantShortInfo} from "../../model/restaurant/restaurant-short-info";
import {PeriodType} from "../../model/period-type";
import {ChartType} from "../../model/chart/chart-type";
import {OrderType} from "../../model/order-type";
import {LegendPosition} from "@swimlane/ngx-charts";
import {NgbDateAdapter} from "@ng-bootstrap/ng-bootstrap";
import {NgbDateToStringAdapter} from "../../adapter/datepicker-adapter";
import {ChartName} from "../../model/chart/chart-name";

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

  chartOptionsValues: ChartGenerateData = {
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
  restaurants: RestaurantShortInfo[] = [
    {
      restaurantId: 1,
      city: 'Kielce',
      street: 'al. XI wieków Kielc'
    },
    {
      restaurantId: 2,
      city: 'Warszawa',
      street: 'Jagiellońska'
    },
    {
      restaurantId: 3,
      city: 'Kraków',
      street: 'Warszawska'
    }
  ];

  constructor(private zone: NgZone, private cd: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    let types = Object.values(ChartType);
    let index = types.length - 1;
    types.splice(index, 1);
    this.chartTypes = types;
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
    switch (chartName) {
      case ChartName.COMPARE_ORDERS_AMOUNT:
        this.getCompareOrdersAmountData();
        break;
      case ChartName.COMPARE_ORDERS_INCOME:
        this.getCompareOrdersIncomeData();
        break;
      case ChartName.ORDERS_AMOUNT_USING_DIFFERENT_PAYMENT_METHODS:
        this.getPaymentMethodData();
        break;
      case ChartName.ORDERS_AMOUNT_IN_DIFFERENT_HOURS:
        this.getHoursData();
        break;
      case ChartName.SOLD_DISH_AMOUNT:
        this.getSoldDishAmountData();
        break;
      case ChartName.SOLD_DISH_INCOME:
        this.getSoldDishIncomeData();
        break;
      case ChartName.AVERAGE_COMPLETION_ORDER_TIME_DEPENDS_ON_DISHES_AMOUNT:
        this.getCompletionTimeData();
        break;
      case ChartName.ORDERS_AMOUNT_CONSIDERING_DISHES_AMOUNT_IN_EACH:
        this.getOrdersAmountData();
        break;
    }
  }

  getCompareOrdersAmountData(): void {
    this.chartData = {
      name: 'Liczba zamówień online w stosunku do liczby zamówień na miejscu',
      Xlabel: 'Typ zamówienia',
      Ylabel: 'Liczba zamówień',
      series: [
        {
          name: 'Online',
          value: 20000
        },
        {
          name: 'W restauracji',
          value: 40000,
        },
      ],
    } as Chart;
  }

  getCompareOrdersIncomeData(): void {
    this.chartData = {
      name: 'Przychód z zamówień online w stosunku do liczby zamówień na miejscu',
      Xlabel: 'Typ zamówienia',
      Ylabel: 'Dochód z zamówień',
      series: [
        {
          name: 'Online',
          value: 50000,
        },
        {
          name: 'W restauracji',
          value: 30000,
        },
      ],
    } as Chart;
  }

  getPaymentMethodData(): void {
    this.chartData = {
      name: 'Liczba zamówień z użyciem różnych metod płatności',
      Xlabel: 'Typ płatności',
      Ylabel: 'Liczba zamówień',
      series: [
        {
          name: 'Gotówka',
          value: 10000
        },
        {
          name: 'Karta',
          value: 50000,
        },
        {
          name: 'PayPal',
          value: 25000,
        },
        {
          name: 'PayU',
          value: 100000,
        },
      ],
    } as Chart;
  }

  getHoursData(): void {
    this.chartData = {
      name: 'Liczba zamówień w zależności od godziny',
      Xlabel: 'Godzina',
      Ylabel: 'Liczba zamówień',
      series: [
        {
          name: '10:00',
          value: 100
        },
        {
          name: '11:00',
          value: 200,
        },
        {
          name: '12:00',
          value: 300,
        },
      ],
    } as Chart;
  }

  getSoldDishAmountData(): void {
    this.chartData = {
      name: 'Liczba sprzedanych poszczególnych potraw',
      Xlabel: 'Potrawa',
      Ylabel: 'Liczba sprzedanych potraw',
      series: [
        {
          name: 'Pomidorowa',
          value: 10,
        },
        {
          name: 'Ogórkowa',
          value: 20,
        },
      ],
    } as Chart;
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

  generateChart() {
    //todo
    this.selectedChartType = this.chartOptionsValues.chartType;
  }

  mapDataToBarOrPieChartData(data: Chart) {
    return data.series;
  }
}
