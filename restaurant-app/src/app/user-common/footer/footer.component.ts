import { Component, OnInit } from '@angular/core';
import {OpeningHour} from "../../model/opening-hour";
import {faPhone, faEnvelope, faLocationDot, faCity} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {
  faPhone = faPhone;
  faEnvelope = faEnvelope;
  faLocationDot = faLocationDot;
  faCity = faCity;
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
