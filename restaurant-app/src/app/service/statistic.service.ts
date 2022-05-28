import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
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

  getOrderInfo(restaurantId: number): Observable<number> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<number>(this.GET_TODAY_INCOME + '?rId' + restaurantId, {headers});
  }
}
