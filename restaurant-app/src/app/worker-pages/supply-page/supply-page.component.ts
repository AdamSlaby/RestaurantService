import { Component, OnInit } from '@angular/core';
import {NgbCalendar, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-supply-page',
  templateUrl: './supply-page.component.html',
  styleUrls: ['./supply-page.component.scss']
})
export class SupplyPageComponent implements OnInit {
  chosenIngredient!: string;
  chosenDate: NgbDateStruct = this.calendar.getToday();

  constructor(private calendar: NgbCalendar) { }

  ngOnInit(): void {
  }

  getIngredientByName() {
    //todo
  }

  getSuppliesByDate() {
    //todo
  }

  resetFilters() {
    //todo
  }
}
