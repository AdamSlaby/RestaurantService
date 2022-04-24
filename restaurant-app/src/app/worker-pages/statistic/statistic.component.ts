import {AfterViewInit, ChangeDetectorRef, Component, Input, NgZone, OnInit} from '@angular/core';
import {ChartGenerateData} from "../../model/chart-generate-data";
import {Chart} from "../../model/chart";
import {RestaurantShortInfo} from "../../model/restaurant/restaurant-short-info";
import {PeriodType} from "../../model/period-type";
import {ChartType} from "../../model/chart-type";
import {OrderType} from "../../model/order-type";
import {LegendPosition} from "@swimlane/ngx-charts";
import {NgbDateAdapter} from "@ng-bootstrap/ng-bootstrap";
import {NgbDateToStringAdapter} from "../../adapter/datepicker-adapter";
import {ChartName} from "../../model/chart-name";

@Component({
  selector: 'app-statistic',
  templateUrl: './statistic.component.html',
  styleUrls: ['./statistic.component.scss'],
  providers: [{provide: NgbDateAdapter, useClass: NgbDateToStringAdapter}],
})
export class StatisticComponent implements OnInit, AfterViewInit {
  @Input() chartName!: ChartName;
  chartOptions!: ChartGenerateData;
  view!: [number, number];
  monthArr = Array.from({length: 12}, (_, i) => i + 1);
  yearArr = Array.from({length: 10}, (_, i) => new Date().getFullYear() - i);
  periodType = PeriodType;
  chartType = ChartType;
  position = LegendPosition;
  observer!:any;
  selectedChartType: ChartType = ChartType.VERTICAL_BAR_CHART;
  chartTypes = Object.values(ChartType);
  orderTypes = Object.values(OrderType);
  chartData: Chart =
    {
      name: 'Liczba zamówień online w stosunku do liczby zamówień na miejscu',
      Xlabel: 'Typ zamówienia',
      Ylabel: 'Liczba zamówień',
      series: [
        {
          name: 'Online',
          value: 250
        },
        {
          name: 'Offline',
          value: 250,
        },
        {
          name: 'Pomidorowa',
          value: 250,
        },
        {
          name: 'Ogórkowa',
          value: 300,
        },
        {
          name: 'Naleśniki',
          value: 350,
        },
        {
          name: 'Marcepanki',
          value: 400,
        },
        {
          name: 'Filet z kurczaka',
          value: 400,
        },
        {
          name: 'Schabowy',
          value: 400,
        },
        {
          name: 'Barszcz z uszkami',
          value: 400,
        },
      ],
    };
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

  constructor(private zone: NgZone, private cd: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.chartOptions = {
      periodType: PeriodType.DAY,
      period: new Date().toUTCString(),
      placeId: localStorage.getItem('restaurantId'),
      chartType: ChartType.VERTICAL_BAR_CHART,
      orderType: OrderType.ALL,
    };
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

  removePeriod() {
    this.chartOptions.period = null;
  }

  generateChart() {
    //todo
    this.selectedChartType = this.chartOptions.chartType;
  }

  mapDataToBarOrPieChartData(data: Chart) {
    return data.series;
  }

  mapDataToAreaChartData(data: Chart) {
    return [
      {
        name: data.name,
        series: data.series,
      },
    ];
  }
}
