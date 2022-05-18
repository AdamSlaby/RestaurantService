import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MealFilters } from '../model/meal/meal-filters';
import { MealInfo } from '../model/meal/meal-info';
import { MealListView } from '../model/meal/meal-list-view';
import { GeneralService } from './general.service';

@Injectable({
  providedIn: 'root'
})
export class MealService {
  private COMMON_URL = `${GeneralService.BASE_URL}/menu/meal`;
  private GET_MEAL_INFO = `${this.COMMON_URL}/info/`;
  private GET_MEALS = `${this.COMMON_URL}/list`;
  private ADD_MEAL = `${this.COMMON_URL}/`;
  private UPDATE_MEAL = `${this.COMMON_URL}/`;
  private DELETE_MEAL = `${this.COMMON_URL}/`;

  constructor(private http: HttpClient) { }

  getMealInfo(mealId: number): Observable<MealInfo> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<MealInfo>(this.GET_MEAL_INFO + mealId, {headers});
  }

  getMeals(filters: MealFilters): Observable<MealListView> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<MealListView>(this.GET_MEALS, filters, {headers});
  }

  addMeal(meal: FormData): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<any>(this.ADD_MEAL, meal, {headers});
  }

  updateMeal(meal: FormData, mealId: number): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.patch<any>(this.UPDATE_MEAL + mealId , meal, {headers});
  }

  deleteMeal(mealId: number): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.delete<any>(this.DELETE_MEAL + mealId , {headers});
  }
}
