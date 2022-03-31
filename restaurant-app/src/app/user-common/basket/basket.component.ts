import {Component, Input, OnInit} from '@angular/core';
import {Order} from "../../model/order";
import {DishOrderView} from "../../model/dish-order-view";
import {faMinus, faPlus, faBasketShopping} from "@fortawesome/free-solid-svg-icons";
import {BasketService} from "../../service/basket.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.scss']
})
export class BasketComponent implements OnInit {
  @Input() dishes!: DishOrderView[];
  showFormBtn!: boolean;
  basket: Map<number, Order> = new Map<number, Order>();
  fullPrice!: number;
  minimalPrice!: number;
  deliveryFee: number = 5;
  faMinus = faMinus;
  faPlus = faPlus;
  faBasketShopping = faBasketShopping;

  constructor(private basketService: BasketService, private router: Router) {
    this.basketService.basket$.subscribe(map => {
      this.basket = new Map<number, Order>(map);
      this.setFullPrice();
    })
    router.events.subscribe(val => {
      console.log(this.router.url);
      this.showFormBtn = !this.router.url.includes('order/customer');
    })
  }

  ngOnInit(): void {
    this.minimalPrice = 30.00;
    this.showFormBtn = !this.router.url.includes('order/customer');
  }

  addDish(order: Order) {
    let price = order.price / order.amount;
    order.amount++;
    order.price += price;
    this.setFullPrice();
    this.basketService.addOrders(this.basket);
  }

  removeDish(order: Order) {
    if (order.amount == 1) {
      this.basket.delete(order.dishId);
    } else {
      let price = order.price / order.amount;
      order.amount--;
      order.price -= price;
    }
    this.setFullPrice();
    this.basketService.addOrders(this.basket);
  }

  getDishById(id: number): DishOrderView {
    return this.dishes.filter(dish => dish.id === id)[0];
  }

  setFullPrice() {
    let sum = 0;
    for (let order of this.basket.values())
      sum += order.price;
    this.fullPrice = sum + this.deliveryFee;
  }
}
