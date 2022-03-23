import {Component, OnInit} from '@angular/core';
import {Dish} from "../model/dish";
import {DishType} from "../model/type";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.scss']
})
export class MainPageComponent implements OnInit {
  welcomeImg: string = 'assets/restaurant.jpg';
  menuImg: string = 'assets/menu.jpg';
  tableImg: string = 'assets/table.jpg';
  bestDishes: Dish[] = [
    {
      id: 1,
      name: 'Śledź',
      type: DishType.FISH,
      ingredients: 'Śledź 300g, frytki, zestaw surówek',
      price: 18.23,
      isBest: false,
    },
    {
      id: 1,
      name: 'Śledź',
      type: DishType.FISH,
      ingredients: 'Śledź 300g, frytki, zestaw surówek',
      price: 18,
      isBest: false,
    },
    {
      id: 1,
      name: 'Śledź',
      type: DishType.FISH,
      ingredients: 'Śledź 300g, frytki, zestaw surówek',
      price: 18,
      isBest: false,
    },
    {
      id: 1,
      name: 'Śledź',
      type: DishType.FISH,
      ingredients: 'Śledź 300g, frytki, zestaw surówek',
      price: 18,
      isBest: false,
    },
    {
      id: 1,
      name: 'Śledź',
      type: DishType.FISH,
      ingredients: 'Śledź 300g, frytki, zestaw surówek',
      price: 18,
      isBest: false,
    },
    {
      id: 1,
      name: 'Śledź',
      type: DishType.FISH,
      ingredients: 'Śledź 300g, frytki, zestaw surówek',
      price: 18,
      isBest: false,
    },
    {
      id: 1,
      name: 'Śledź',
      type: DishType.FISH,
      ingredients: 'Śledź 300g, frytki, zestaw surówek',
      price: 18,
      isBest: false,
    },
  ]
  constructor() { }

  ngOnInit(): void {

  }
}
