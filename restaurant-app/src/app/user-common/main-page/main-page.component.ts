import { Component, OnInit } from '@angular/core';
import {Dish} from "../model/dish";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.scss']
})
export class MainPageComponent implements OnInit {
  bestDishes: Dish[] = [
    {
      name: 'Śledź',
      price: 18
    },
    {
      name: 'Śledź',
      price: 18
    },
    {
      name: 'Śledź',
      price: 18
    },
    {
      name: 'Śledź',
      price: 18
    },
    {
      name: 'Śledź',
      price: 18
    },
    {
      name: 'Śledź',
      price: 18
    },
    {
      name: 'Śledź',
      price: 18
    },
    {
      name: 'Śledź',
      price: 18
    },
  ]
  constructor() { }

  ngOnInit(): void {
  }
}
