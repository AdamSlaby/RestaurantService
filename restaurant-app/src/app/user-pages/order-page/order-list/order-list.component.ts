import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DishOrderView} from "../../../model/dish/dish-order-view";
import {faMinus, faPlus} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.scss']
})
export class OrderListComponent implements OnInit, AfterViewInit {
  @Input() dishes!: DishOrderView[];
  @Output() addOrder = new EventEmitter<DishOrderView>();
  faMinus = faMinus;
  faPlus = faPlus;

  constructor() { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    let element = document.getElementById('dish-' + (this.dishes.length - 1));
    if (element)
      element.classList.remove('border-bottom');
  }

  addToBasket(dish: DishOrderView) {
    this.addOrder.emit(dish);
  }

  removeDish(dish: DishOrderView) {
    dish.amount--;
  }

  addDish(dish: DishOrderView) {
    dish.amount++;
  }
}
