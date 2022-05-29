import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DishOrderView} from "../../../model/dish/dish-order-view";
import {faMinus, faPlus} from "@fortawesome/free-solid-svg-icons";
import { MealService } from 'src/app/service/meal.service';
import { Order } from 'src/app/model/order/order';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.scss']
})
export class OrderListComponent implements OnInit, AfterViewInit {
  @Input() dishes!: DishOrderView[];
  @Output() addOrder = new EventEmitter<DishOrderView>();
  errors: Map<string, string> = new Map<string, string>();
  faMinus = faMinus;
  faPlus = faPlus;

  constructor(private mealService: MealService) { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    let element = document.getElementById('dish-' + (this.dishes.length - 1));
    if (element)
      element.classList.remove('border-bottom');
  }

  addToBasket(dish: DishOrderView, index: number) {
    let restaurantId = sessionStorage.getItem('restaurantId');
    this.mealService.validateOrder(restaurantId, this.mapDishToOrder(dish)).subscribe(data => {
      this.addOrder.emit(dish);
    }, error => {
        this.errors = new Map(Object.entries(error.error));
        let copy = new Map<string, string>(this.errors);
        copy.forEach((v, k) => {
          this.errors.set(k + index, v);
          this.errors.delete(k);
        });
        console.error(error);
    });
  }

  removeDish(dish: DishOrderView) {
    dish.amount--;
  }

  addDish(dish: DishOrderView) {
    dish.amount++;
  }

  mapDishToOrder(dish: DishOrderView) {
    return {
      dishId: dish.id,
      name: dish.name,
      amount: dish.amount,
      price: dish.price
    } as Order;
  }
}
