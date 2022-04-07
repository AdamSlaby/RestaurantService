import { Component, OnInit } from '@angular/core';
import {RestaurantShortInfo} from "../../model/restaurant-short-info";
import {Router} from "@angular/router";

@Component({
  selector: 'app-restaurant-page',
  templateUrl: './restaurant-page.component.html',
  styleUrls: ['./restaurant-page.component.scss']
})
export class RestaurantPageComponent implements OnInit {
  restaurants: RestaurantShortInfo[] = [
    {
      restaurantId: 1,
      city: 'Kielce',
      street: 'al. XI wieków Kielc'
    },
    {
      restaurantId: 2,
      city: 'Warszawa',
      street: 'Jagiellońska'
    },
    {
      restaurantId: 3,
      city: 'Kraków',
      street: 'Warszawska'
    }
  ]

  constructor(private router: Router) {

  }

  ngOnInit(): void {
    if (this.restaurants.length === 1) {
      sessionStorage.setItem('restaurantId', this.restaurants[0].restaurantId + '');
      this.router.navigateByUrl('/main-site');
    }
  }

  chooseRestaurant(restaurant: RestaurantShortInfo) {
    sessionStorage.setItem('restaurantId', restaurant.restaurantId + '');
    this.router.navigateByUrl('/main-site');
  }
}
