import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Chart } from '../model/chart/chart';
import { GenerateChartOptions } from '../model/chart/generate-chart-options';
import { GeneralService } from './general.service';

@Injectable({
  providedIn: 'root'
})
export class StatisticService {
  private COMMON_URL = `${GeneralService.BASE_URL}/order/statistic`;
  private GET_TODAY_INCOME = `${this.COMMON_URL}/income`;
  private GET_TODAY_DELIVERED_ORDERS_AMOUNT = `${this.COMMON_URL}/orders`;
  private GET_TODAY_DELIVERED_MEALS_AMOUNT = `${this.COMMON_URL}/meals`;
  private GET_ACTIVE_ORDERS_AMOUNT = `${this.COMMON_URL}/active`;
  private GET_ORDER_AMOUNT_FROM_HOURS = `${this.COMMON_URL}/hours`;
  private GET_STATISTICS = `${this.COMMON_URL}/`;

  constructor(private http: HttpClient) { }

  getTodayIncome(restaurantId: any): Observable<number> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    let path = restaurantId ? this.GET_TODAY_INCOME + '?rId=' + restaurantId : this.GET_TODAY_INCOME;
    return this.http.get<number>(path, {headers});
  }

  getTodayDeliveredOrdersAmount(restaurantId: any): Observable<number> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    let path = restaurantId ? this.GET_TODAY_DELIVERED_ORDERS_AMOUNT + '?rId=' + restaurantId : this.GET_TODAY_DELIVERED_ORDERS_AMOUNT;
    return this.http.get<number>(path, {headers});
  }

  getTodayDeliveredMealsAmount(restaurantId: any): Observable<number> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    let path = restaurantId ? this.GET_TODAY_DELIVERED_MEALS_AMOUNT + '?rId=' + restaurantId : this.GET_TODAY_DELIVERED_MEALS_AMOUNT;
    return this.http.get<number>(path, {headers});
  }

  getActiveOrdersAmount(restaurantId: any): Observable<number> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    let path = restaurantId ? this.GET_ACTIVE_ORDERS_AMOUNT + '?rId=' + restaurantId : this.GET_ACTIVE_ORDERS_AMOUNT;
    return this.http.get<number>(path, {headers});
  }

  getOrderAmountFromHours(restaurantId: any): Observable<Chart> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    let path = restaurantId ? this.GET_ORDER_AMOUNT_FROM_HOURS + '?rId=' + restaurantId : this.GET_ORDER_AMOUNT_FROM_HOURS;
    return this.http.get<Chart>(path, {headers});
  }

  getStatistics(options: GenerateChartOptions): Observable<Chart> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<Chart>(this.GET_STATISTICS, options, {headers});
  }
}
