import { Component, OnInit } from '@angular/core';
import {OpeningHour} from "../model/opening-hour";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {
  openingHours: OpeningHour[] = [
    {
      weekDayName: "poniedziałek",
      hours: "10:00 - 18:00"
    },
    {
      weekDayName: "wtorek",
      hours: "10:00 - 18:00"
    },
    {
      weekDayName: "środa",
      hours: "10:00 - 18:00"
    },
    {
      weekDayName: "czwartek",
      hours: "10:00 - 18:00"
    },
    {
      weekDayName: "piątek",
      hours: "10:00 - 18:00"
    },
    {
      weekDayName: "sobota",
      hours: "10:00 - 18:00"
    },
    {
      weekDayName: "niedziela",
      hours: "10:00 - 18:00"
    },
  ];
  constructor() { }

  ngOnInit(): void {
  }

}
