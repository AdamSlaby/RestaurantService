import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DishOrderView} from "../../model/dish-order-view";

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.scss']
})
export class OrderListComponent implements OnInit, AfterViewInit {
  @Input() dishes!: DishOrderView[];
  @Input() basket!: DishOrderView[];

  constructor() { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    let element = document.querySelector('#' + 'dish-' + (this.dishes.length - 1));
    if (element)
      element.classList.remove('border-bottom');
  }

  addToBasket(dish: DishOrderView) {
    this.basket.push(dish);
  }
}
