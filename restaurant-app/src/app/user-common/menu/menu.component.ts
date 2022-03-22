import {AfterViewInit, Component, OnInit} from '@angular/core';
import {Dish} from "../model/dish";
import {DishType} from "../model/type";
import {cloneDeep} from 'lodash';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit, AfterViewInit {
  type = DishType;
  menu: Dish[] = [
    {
      id: 1,
      name: 'Zupa szczawiowa',
      type: DishType.SOUP,
      ingredients: 'z jajkiem i kiełbasą',
      price: 20.32
    },
    {
      id: 2,
      name: 'Pierogi',
      type: DishType.MAIN_DISH,
      ingredients: 'z kapustą i mięsem',
      price: 25.00
    },
    {
      id: 3,
      name: 'Śledź',
      type: DishType.FISH,
      ingredients: 'Śledź 300g, frytki, zestaw surówek',
      price: 18.23,
    },
    {
      id: 4,
      name: 'Sałatka grecka',
      type: DishType.SALAD,
      ingredients: 'i kurczakiem i serem pleśniowym',
      price: 28.23,
    },
    {
      id: 5,
      name: 'TIRAMISU',
      type: DishType.DESSERT,
      ingredients: 'bita śmietana',
      price: 28.23,
    },
  ]
  constructor() {}

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    for (let j = 1; j <= 7; j++) {
      let dishesCopy = cloneDeep(this.menu);
      this.menu.push.apply(this.menu, dishesCopy);
    }
  }

  getDishes(type: DishType, colNr: number) {
    let dishes = this.menu.filter(el => el.type === type);
    let amountInCol = Math.ceil(dishes.length / 2.0);
    let firstIndex = colNr * amountInCol;
    let secondIndex = firstIndex + amountInCol;
    return dishes.slice(firstIndex, secondIndex);
  }
}
