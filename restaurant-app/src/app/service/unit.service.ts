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

  constructor(private http: HttpClient) { }

  getAllUnits(): Observable<Unit[]> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<Unit[]>(this.GET_ALL_UNITS, {headers});
  }
}
