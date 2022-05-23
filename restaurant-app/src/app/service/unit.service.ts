import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Unit } from '../model/meal/unit';
import { GeneralService } from './general.service';

@Injectable({
  providedIn: 'root'
})
export class UnitService {
  private COMMON_URL = `${GeneralService.BASE_URL}/menu/unit`;
  private GET_ALL_UNITS = `${this.COMMON_URL}/`;

  private INVOICE_COMMON_URL = `${GeneralService.BASE_URL}/supply/unit`;
  private GET_ALL_INVOICE_UNITS = `${this.INVOICE_COMMON_URL}/`;

  constructor(private http: HttpClient) { }

  getAllUnits(): Observable<Unit[]> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<Unit[]>(this.GET_ALL_UNITS, {headers});
  }

  getAllInvoiceUnits(): Observable<Unit[]> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<Unit[]>(this.GET_ALL_INVOICE_UNITS, {headers});
  }
}
