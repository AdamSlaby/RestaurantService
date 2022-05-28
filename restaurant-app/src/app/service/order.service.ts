import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OnlineOrder } from '../model/order/online-order';
import { OrderFilters } from '../model/order/order-filters';
import { OrderListView } from '../model/order/order-list-view';
import { OrdersInfo } from '../model/order/orders-info';
import { RestaurantOrder } from '../model/order/restaurant-order';
import { GeneralService } from './general.service';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private COMMON_URL = `${GeneralService.BASE_URL}/order`;
  private GET_ORDER_LIST = `${this.COMMON_URL}/list`;
  private GET_ORDER_INFO = `${this.COMMON_URL}/`;
  private GET_ACTIVE_ORDERS = `${this.COMMON_URL}/active`;
  private ADD_RESTAURANT_ORDER = `${this.COMMON_URL}/restaurant`;
  private RESERVE_ORDER = `${this.COMMON_URL}/reserve`;
  private UPDATE_RESTAURANT_ORDER = `${this.COMMON_URL}/`;
  private COMPLETE_RESTAURANT_ORDER = `${this.COMMON_URL}/restaurant/`;
  private COMPLETE_ONLINE_ORDER = `${this.COMMON_URL}/online/`;

  constructor(private http: HttpClient) { }

  getOrderList(filters: OrderFilters): Observable<OrderListView> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<OrderListView>(this.GET_ORDER_LIST, filters, {headers});
  }

  getOrderInfo(orderId: number, type: string): Observable<OrdersInfo> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<OrdersInfo>(this.GET_ORDER_INFO + orderId + '?type=' + type, {headers});
  }

  getActiveOrders(restaurantId: any): Observable<OrdersInfo> {
    let path = restaurantId ? this.GET_ACTIVE_ORDERS + '?rId=' + restaurantId : this.GET_ACTIVE_ORDERS;
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<OrdersInfo>(path, {headers});
  }

  addRestaurantOrder(order: RestaurantOrder): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<any>(this.ADD_RESTAURANT_ORDER, order, {headers});
  }

  reserveOrder(order: OnlineOrder): Observable<number> {
    return this.http.post<number>(this.RESERVE_ORDER, order);
  }

  updateRestaurantOrder(orderId: number, order: RestaurantOrder): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.patch<any>(this.UPDATE_RESTAURANT_ORDER + orderId, order, {headers});
  }

  completeRestaurantOrder(orderId: number): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.patch<any>(this.COMPLETE_RESTAURANT_ORDER + orderId, {headers});
  }

  completeOnlineOrder(orderId: number): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.patch<any>(this.COMPLETE_ONLINE_ORDER + orderId, {headers});
  }
}
