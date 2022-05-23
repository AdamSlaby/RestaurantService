import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NewSupply } from '../model/supply/new-supply';
import { SupplyInfo } from '../model/supply/supply-info';
import { GeneralService } from './general.service';

@Injectable({
  providedIn: 'root'
})
export class SupplyService {
  private COMMON_URL = `${GeneralService.BASE_URL}/supply`;
  private GET_ALL_SUPPLIES = `${this.COMMON_URL}/list/`;
  private ADD_SUPPLY = `${this.COMMON_URL}/`;
  private UPDATE_SUPPLY = `${this.COMMON_URL}/`;

  constructor(private http: HttpClient) { }

  getAllSupplies(restaurantId: any): Observable<SupplyInfo[]> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<SupplyInfo[]>(this.GET_ALL_SUPPLIES + restaurantId, {headers});
  }

  addSupply(supply: NewSupply): Observable<SupplyInfo> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<SupplyInfo>(this.ADD_SUPPLY, supply, {headers});
  }

  updateSupply(supply: SupplyInfo): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.put<any>(this.UPDATE_SUPPLY, supply, {headers});
  }
}
