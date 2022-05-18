import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IngredientInfo } from '../model/meal/ingredient-info';
import { GeneralService } from './general.service';

@Injectable({
  providedIn: 'root'
})
export class IngredientService {
  private COMMON_URL = `${GeneralService.BASE_URL}/menu/ingredient`;
  private GET_ALL_INGREDIENTS = `${this.COMMON_URL}/`;

  constructor(private http: HttpClient) { }

  getAllIngredients(): Observable<IngredientInfo[]> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<IngredientInfo[]>(this.GET_ALL_INGREDIENTS, {headers});
  }
}
