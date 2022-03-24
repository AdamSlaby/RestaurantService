import {Component, Input, OnInit} from '@angular/core';
import {Dish} from "../../model/dish";

@Component({
  selector: 'app-dish-list',
  templateUrl: './dish-list.component.html',
  styleUrls: ['./dish-list.component.scss']
})
export class DishListComponent implements OnInit {
  @Input() dishes!: Dish[];
  @Input() showBest!: boolean;
  constructor() { }

  ngOnInit(): void {
  }

}
