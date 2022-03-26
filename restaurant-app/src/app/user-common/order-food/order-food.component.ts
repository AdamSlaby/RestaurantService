import {Component, OnInit} from '@angular/core';
import {DishOrderView} from "../../model/dish-order-view";
import {DishType} from "../../model/type";
import {faMagnifyingGlass, faXmark} from "@fortawesome/free-solid-svg-icons";

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
  basket: DishOrderView[] = [];
  dishes: DishOrderView[] = [
    {
      id: 6,
      name: 'Ogórkowa',
      imageUrl: 'assets/soup.jpg',
      type: DishType.SOUP,
      ingredients: '30g makaronu',
      price: 21.32,
    },
    {
      id: 6,
      name: 'Pomidorowa',
      imageUrl: 'assets/soup.jpg',
      type: DishType.SOUP,
      ingredients: '30g makaronu',
      price: 21.32,
    },
    {
      id: 2,
      name: 'Pierogi',
      imageUrl: 'assets/soup.jpg',
      type: DishType.MAIN_DISH,
      ingredients: 'z kapustą i mięsem',
      price: 25.00,
    },
    {
      id: 3,
      name: 'Śledź',
      imageUrl: 'assets/soup.jpg',
      type: DishType.FISH,
      ingredients: 'Śledź 300g, frytki, zestaw surówek',
      price: 18.23,
    },
    {
      id: 4,
      name: 'Sałatka grecka',
      imageUrl: 'assets/soup.jpg',
      type: DishType.SALAD,
      ingredients: 'i kurczakiem i serem pleśniowym',
      price: 28.23,
    },
    {
      id: 5,
      name: 'TIRAMISU',
      imageUrl: 'assets/soup.jpg',
      type: DishType.DESSERT,
      ingredients: 'bita śmietana',
      price: 28.23,
    },
    {
      id: 7,
      name: 'Mirinda 1L',
      imageUrl: 'assets/soup.jpg',
      type: DishType.BEVERAGE,
      ingredients: '',
      price: 5.23,
    },
  ]

  constructor() { }

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
}
