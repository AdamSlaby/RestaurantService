import { Component, OnInit } from '@angular/core';
import {RestaurantShortInfo} from "../../model/restaurant/restaurant-short-info";
import {Router} from "@angular/router";
import {RestaurantService} from "../../service/restaurant.service";

@Component({
  selector: 'app-restaurant',
  templateUrl: './restaurant-page.component.html',
  styleUrls: ['./restaurant-page.component.scss']
})
export class RestaurantPageComponent implements OnInit {
  restaurants!: RestaurantShortInfo[];

  constructor(private router: Router, private restaurantService: RestaurantService) {
  }

  ngOnInit(): void {
    this.restaurantService.getAllRestaurants().subscribe(data => {
      this.restaurants = data;
      if (this.restaurants.length === 1) {
        sessionStorage.setItem('restaurantId', this.restaurants[0].restaurantId + '');
        this.router.navigateByUrl('/main-site');
      }
    }, error => {
      console.error(error);
    });
  }

  chooseRestaurant(restaurant: RestaurantShortInfo) {
    sessionStorage.setItem('restaurantId', restaurant.restaurantId + '');
    this.router.navigateByUrl('/main-site');
  }
}
