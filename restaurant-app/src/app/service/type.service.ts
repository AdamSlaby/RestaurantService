import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Type } from '../model/meal/type';
import { GeneralService } from './general.service';

@Injectable({
  providedIn: 'root'
})
export class TypeService {
  private COMMON_URL = `${GeneralService.BASE_URL}/menu/type`;
  private GET_ALL_TYPES = `${this.COMMON_URL}/`;
  private ADD_TYPE = `${this.COMMON_URL}/`;
  private UPDATE_TYPE = `${this.COMMON_URL}/{id}`;

  constructor(private http: HttpClient) { }

  getAllTypes(): Observable<Type[]> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<Type[]>(this.GET_ALL_TYPES, {headers});
  }

  addType(type: string): Observable<Type> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<Type>(this.ADD_TYPE + '?name=' + type, {headers});
  }

  updateType(type: string, typeId: number): Observable<Type> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.put<Type>(this.UPDATE_TYPE + typeId + '?name=' + type, {headers});
  }
}
