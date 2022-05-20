import {Component, OnInit} from '@angular/core';
import { MealService } from 'src/app/service/meal.service';
import {Dish} from "../../model/dish/dish";

@Component({
  selector: 'app-dashboard',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.scss']
})
export class MainPageComponent implements OnInit {
  welcomeImg: string = 'assets/restaurant.jpg';
  menuImg: string = 'assets/menu.jpg';
  tableImg: string = 'assets/table.jpg';
  bestDishes!: Dish[];
  constructor(private mealService: MealService) { }

  ngOnInit(): void {
    this.mealService.getBestMeals().subscribe(data => {
      this.bestDishes = data;
    }, error => {
      console.error(error);
    })
  }
}
