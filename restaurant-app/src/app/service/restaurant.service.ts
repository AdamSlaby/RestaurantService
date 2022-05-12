import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {GeneralService} from "./general.service";
import {Observable} from "rxjs";
import {RestaurantInfo} from "../model/restaurant/restaurant-info";
import {Table} from "../model/table";
import {Restaurant} from "../model/restaurant/restaurant";
import {RestaurantShortInfo} from "../model/restaurant/restaurant-short-info";

@Injectable({
  providedIn: 'root'
})
export class RestaurantService {
  private COMMON_URL = `${GeneralService.BASE_URL}/restaurant`;
  private GET_RESTAURANT_INFO = `${this.COMMON_URL}/info`;
  private GET_TABLE = `${this.COMMON_URL}/table`;
  private GET_RESTAURANT = `${this.COMMON_URL}/details/`;
  private GET_ALL_RESTAURANTS = `${this.COMMON_URL}/`;
  private ADD_RESTAURANT = `${this.COMMON_URL}/new`;
  private UPDATE_RESTAURANT = `${this.COMMON_URL}/`;
  private REMOVE_RESTAURANT_TABLE = `${this.COMMON_URL}/table`;

  constructor(private http: HttpClient) { }

  getRestaurantInfo(restaurantId: any): Observable<RestaurantInfo> {
    return this.http.get<RestaurantInfo>(this.GET_RESTAURANT_INFO + restaurantId);
  }

  getTable(seatsNr: number, restaurantId: any): Observable<Table> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<Table>(this.GET_TABLE + '?seats=' + seatsNr + '&restaurantId=' + restaurantId, {headers});
  }

  getRestaurant(restaurantId: any): Observable<Restaurant> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<Restaurant>(this.GET_RESTAURANT + restaurantId, {headers});
  }

  getAllRestaurants(): Observable<RestaurantShortInfo[]> {
    return this.http.get<RestaurantShortInfo[]>(this.GET_ALL_RESTAURANTS);
  }

  addRestaurant(restaurant: Restaurant): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<any>(this.ADD_RESTAURANT, restaurant, {headers});
  }

  updateRestaurant(restaurant: Restaurant, restaurantId: any): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.put<any>(this.UPDATE_RESTAURANT + restaurantId, restaurant, {headers});
  }

  removeRestaurantTable(restaurantId: any, tableId: number): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http
      .delete<any>(this.REMOVE_RESTAURANT_TABLE + '?tableId=' + tableId + '&restaurantId=' + restaurantId, {headers});
  }
}
