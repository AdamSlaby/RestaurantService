import {Component, OnInit} from '@angular/core';
import {DishOrderView} from "../../model/dish-order-view";
import {DishType} from "../../model/type";
import {faMagnifyingGlass, faXmark} from "@fortawesome/free-solid-svg-icons";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {BasketService} from "../../service/basket.service";

@Component({
  selector: 'app-order-food',
  templateUrl: './order-food.component.html',
  styleUrls: ['./order-food.component.scss']
})
export class OrderFoodComponent implements OnInit {
  showSearchInput: boolean = false;
  type = DishType;
  searchPhrase: string = '';
  faMagnifyingGlass = faMagnifyingGlass;
  faXmark = faXmark;
  searchedDishes: DishOrderView[] = [];
  dishes: DishOrderView[] = [];

  constructor(private modalService: NgbModal, private basketService: BasketService) {
    this.basketService.dishes$.subscribe(dishes => {
      this.dishes = [...dishes];
    })
  }

  ngOnInit(): void {
    for (let i = 0; i < 6; i++) {
      let orderView = this.dishes[i];
      for (let j = 1; j <= 7; j++) {
        let dishCopy = {...orderView};
        this.dishes.push(dishCopy);
      }
    }
    this.getDishesByType(this.type.SOUP);
  }

  onSearchClick() {
    this.showSearchInput = true;
  }

  onCloseClick() {
    this.showSearchInput = false;
    this.getDishesByType(this.type.SOUP);
  }

  getDishesByType(type: DishType) {
    this.searchedDishes = this.dishes.filter(dish => dish.type === type)
  }

  search() {
    if (this.searchPhrase.length > 2) {
      this.searchedDishes = this.dishes
        .filter(dish => dish.name.toLowerCase().includes(this.searchPhrase.toLowerCase()));
    }
  }

  addOrder(dish: DishOrderView) {
    this.basketService.addOrder(dish);
  }
}
