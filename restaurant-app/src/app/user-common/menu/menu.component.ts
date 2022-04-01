import {AfterViewInit, Component, OnInit} from '@angular/core';
import {Dish} from "../../model/dish";
import {DishType} from "../../model/type";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit, AfterViewInit {
  type = DishType;
  soupImg: string = 'assets/soup-image.jpg';
  mainDishImg: string = 'assets/main_dish_image.jpg';
  fishImg: string = 'assets/fish_image.jpg';
  saladImg: string = 'assets/salad-image.jpg';
  dessertImg: string = 'assets/dessert_image.jpg';
  beverageImg: string = 'assets/beverage-picture.jpg'
  menu: Dish[] = [
    {
      id: 6,
      name: 'Barszcz z uszkami',
      type: DishType.SOUP,
      ingredients: '30g uszek',
      price: 22.32,
      isBest: true,
    },
    {
      id: 6,
      name: 'Pomidorowa',
      type: DishType.SOUP,
      ingredients: '30g makaronu',
      price: 21.32,
      isBest: true,
    },
    {
      id: 1,
      name: 'Zupa szczawiowa',
      type: DishType.SOUP,
      ingredients: 'z jajkiem i kiełbasą',
      price: 20.32,
      isBest: false,
    },
    {
      id: 2,
      name: 'Pierogi',
      type: DishType.MAIN_DISH,
      ingredients: 'z kapustą i mięsem',
      price: 25.00,
      isBest: false,
    },
    {
      id: 3,
      name: 'Śledź',
      type: DishType.FISH,
      ingredients: 'Śledź 300g, frytki, zestaw surówek',
      price: 18.23,
      isBest: false,
    },
    {
      id: 4,
      name: 'Sałatka grecka',
      type: DishType.SALAD,
      ingredients: 'i kurczakiem i serem pleśniowym',
      price: 28.23,
      isBest: false,
    },
    {
      id: 5,
      name: 'TIRAMISU',
      type: DishType.DESSERT,
      ingredients: 'bita śmietana',
      price: 28.23,
      isBest: false,
    },
    {
      id: 7,
      name: 'Mirinda 1L',
      type: DishType.BEVERAGE,
      ingredients: '',
      price: 5.23,
      isBest: false,
    },
  ]

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    for (let i = 2; i < 8; i++) {
      let dish = {...this.menu[i]};
      for (let j = 1; j <= 8; j++) {
        this.menu.push(dish);
      }
    }
  }

  ngAfterViewInit() {
    this.route.fragment.subscribe(fragment => {
        let element = document.querySelector("#" + fragment);
        if (element)
          element.scrollIntoView();
      }
    );
  }

  getDishes(type: DishType, colNr: number) {
    let dishes = this.menu.filter(el => el.type === type);
    let amountInCol = Math.ceil(dishes.length / 2.0);
    let firstIndex = colNr * amountInCol;
    let secondIndex = firstIndex + amountInCol;
    return dishes.slice(firstIndex, secondIndex);
  }

  isSectionActive(fragment: string) {
    let isActive = false;
    this.route.fragment.subscribe(_fragment => {
      isActive = _fragment === fragment
    })
    return isActive;
  }
}
