import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DishOrderView} from "../../model/dish-order-view";
import {Order} from "../../model/order";
import {faMinus, faPlus} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.scss']
})
export class OrderListComponent implements OnInit, AfterViewInit {
  @Input() dishes!: DishOrderView[];
  @Input() basket!: Order[];
  @Output() changePrice = new EventEmitter<void>();
  faMinus = faMinus;
  faPlus = faPlus;

  constructor() { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    let element = document.querySelector('#' + 'dish-' + (this.dishes.length - 1));
    if (element)
      element.classList.remove('border-bottom');
  }

  addToBasket(dish: DishOrderView) {
    this.basket.push({
      dishId: dish.id,
      amount: dish.amount,
      price: (dish.price * dish.amount),
    });
    this.changePrice.emit();
  }

  removeDish(dish: DishOrderView) {
    dish.amount--;
  }

  addDish(dish: DishOrderView) {
    dish.amount++;
  }
}
