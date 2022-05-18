import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Dish } from '../model/dish/dish';
import { DishOrderView } from '../model/dish/dish-order-view';
import { MealMenu } from '../model/meal/meal-menu';
import { MealShortView } from '../model/meal/meal-short-view';
import { MenuView } from '../model/menu-view';
import { GeneralService } from './general.service';

@Injectable({
  providedIn: 'root'
})
export class MenuService {
  private COMMON_URL = `${GeneralService.BASE_URL}/menu`;
  private GET_DISHES_FROM_MENU = `${this.COMMON_URL}/dishes`;
  private GET_DISH_ORDER_VIEWS_FROM_MENU = `${this.COMMON_URL}/dishes/list`;
  private GET_ALL_MENUS = `${this.COMMON_URL}/all`;
  private ADD_MEAL_TO_MENU = `${this.COMMON_URL}/`;
  private REMOVE_MEAL_FROM_MENU = `${this.COMMON_URL}/`;

  constructor(private http: HttpClient) { }

  getDishesFromMenu(): Observable<Dish[]> {
    return this.http.get<Dish[]>(this.GET_DISHES_FROM_MENU);
  }

  getDisheOrderViewsFromMenu(): Observable<DishOrderView[]> {
    return this.http.get<DishOrderView[]>(this.GET_DISH_ORDER_VIEWS_FROM_MENU);
  }

  getAllMenus(): Observable<MenuView[]> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<MenuView[]>(this.GET_ALL_MENUS, {headers});
  }

  addMealToMenu(mealMenu: MealMenu): Observable<MealShortView> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<MealShortView>(this.ADD_MEAL_TO_MENU, mealMenu, {headers});
  }

  removeMealFromMenu(menuId: number, mealId: number): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.delete<any>(this.REMOVE_MEAL_FROM_MENU + menuId + '?meal=' + mealId, {headers});
  }
}
