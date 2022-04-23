import {AfterViewInit, ChangeDetectorRef, Component, NgZone, OnInit} from '@angular/core';
import {ChartGenerateData} from "../../model/chart-generate-data";
import {ChartType} from "../../model/chart-type";
import {OrderType} from "../../model/order-type";

@Component({
  selector: 'app-statistics-page',
  templateUrl: './statistics-page.component.html',
  styleUrls: ['./statistics-page.component.scss']
})
export class StatisticsPageComponent implements OnInit, AfterViewInit {
  chartOptions!: ChartGenerateData[];
  view!: [number, number];

  constructor(private zone: NgZone, private cd: ChangeDetectorRef) { }

  ngOnInit(): void {
    for (let i = 1; i <= 8; i++) {
      this.chartOptions.push({
        period: new Date().toLocaleDateString(),
        placeId: localStorage.getItem('restaurantId'),
        chartType: ChartType.VERTICAL_BAR_CHART,
        orderType: OrderType.ALL,
      });
    }
  }

  ngAfterViewInit() {

  }

  getElementWidth(id: string) {
    let element = document.getElementById(id);
    if (element) {
      return element.getBoundingClientRect().width;
    } else
      return 0;
  }
}
