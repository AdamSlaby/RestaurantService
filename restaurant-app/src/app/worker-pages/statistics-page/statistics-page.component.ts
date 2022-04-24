import {AfterViewInit, ChangeDetectorRef, Component, NgZone, OnInit} from '@angular/core';
import {ChartGenerateData} from "../../model/chart-generate-data";
import {ChartType} from "../../model/chart-type";
import {OrderType} from "../../model/order-type";
import {PeriodType} from "../../model/period-type";
import {NgbDateAdapter} from "@ng-bootstrap/ng-bootstrap";
import {NgbDateToStringAdapter} from "../../adapter/datepicker-adapter";
import {RestaurantShortInfo} from "../../model/restaurant/restaurant-short-info";
import {Chart} from "../../model/chart";
import {ChartData} from "../../model/chart-data";
import {LegendPosition} from "@swimlane/ngx-charts";
import {ChartName} from "../../model/chart-name";

@Component({
  selector: 'app-statistics-page',
  templateUrl: './statistics-page.component.html',
  styleUrls: ['./statistics-page.component.scss'],
})
export class StatisticsPageComponent implements OnInit {
  chartName = ChartName;

  constructor() {
  }

  ngOnInit(): void {
  }
}
