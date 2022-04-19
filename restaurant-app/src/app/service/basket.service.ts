import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {Order} from "../model/order/order";
import {DishOrderView} from "../model/dish/dish-order-view";

@Injectable({
  providedIn: 'root'
})
export class BasketService {
  private _basket = new Map<number, Order>();
  private _dishes: DishOrderView[] = [];
  private _dishes$ = new BehaviorSubject<ReadonlyArray<DishOrderView>>(this.dishes);
  private _basket$ = new BehaviorSubject<ReadonlyMap<number, Order>>(this.basket);

  get basket(): ReadonlyMap<number, Order> {
    return this._basket;
  }

  get dishes(): ReadonlyArray<DishOrderView> {
    return this._dishes;
  }


  get dishes$(): Observable<ReadonlyArray<DishOrderView>> {
    return this._dishes$.asObservable();
  }

  get basket$(): Observable<ReadonlyMap<number, Order>> {
    return this._basket$.asObservable();
  }

  addDishes(dishes: DishOrderView[]) {
    this._dishes = dishes;
    this.notifyDishesChange()
  }

  addOrder(dish: DishOrderView) {
    if (this.basket.has(dish.id)) {
      let order = this.basket.get(dish.id);
      if (order) {
        let price = order.price / order.amount;
        order.amount += dish.amount;
        order.price += dish.amount * price;
      }
    } else {
      this._basket.set(dish.id, {
        dishId: dish.id,
        amount: dish.amount,
        price: (dish.price * dish.amount),
      });
    }
    this.notifyBasketChange();
  }

  addOrders(orders: Map<number, Order>) {
    this._basket = orders;
    this.notifyBasketChange();
  }

  private notifyBasketChange() {
    this._basket$.next(this.basket);
  }

  private notifyDishesChange() {
    this._dishes$.next(this.dishes);
  }
}
