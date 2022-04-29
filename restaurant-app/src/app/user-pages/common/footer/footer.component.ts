import { Component, OnInit } from '@angular/core';
import {OpeningHour} from "../../../model/opening-hour";
import {faPhone, faEnvelope, faLocationDot, faCity} from "@fortawesome/free-solid-svg-icons";
import {RestaurantInfo} from "../../../model/restaurant/restaurant-info";

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
  restaurantInfo: RestaurantInfo = {
    email: 'restaurant24@gmail.com',
    phoneNr: '+48600200300',
    address: {
      city: 'Kielce',
      street: 'Niewiadomskiego',
      houseNr: '102',
      flatNr: '',
      postcode: '25-000',
    }
  }
  openingHours: OpeningHour[] = [
    {
      hourId: 1,
      weekDayNr: 1,
      fromHour: new Date('04 Apr 2022 10:00:00 UTC'),
      toHour: new Date('04 Apr 2022 18:00:00 UTC'),
    },
    {
      hourId: 2,
      weekDayNr: 2,
      fromHour: new Date('05 Apr 2022 10:00:00 UTC'),
      toHour: new Date('05 Apr 2022 18:00:00 UTC'),
    },
    {
      hourId: 3,
      weekDayNr: 3,
      fromHour: new Date('06 Apr 2022 10:00:00 UTC'),
      toHour: new Date('06 Apr 2022 18:00:00 UTC'),
    },
    {
      hourId: 4,
      weekDayNr: 4,
      fromHour: new Date('07 Apr 2022 10:00:00 UTC'),
      toHour: new Date('07 Apr 2022 18:00:00 UTC'),
    },
    {
      hourId: 5,
      weekDayNr: 5,
      fromHour: new Date('08 Apr 2022 10:00:00 UTC'),
      toHour: new Date('08 Apr 2022 18:00:00 UTC'),
    },
    {
      hourId: 6,
      weekDayNr: 6,
      fromHour: new Date('09 Apr 2022 10:00:00 UTC'),
      toHour: new Date('09 Apr 2022 18:00:00 UTC'),
    },
    {
      hourId: 7,
      weekDayNr: 7,
      fromHour: new Date('10 Apr 2022 10:00:00 UTC'),
      toHour: new Date('10 Apr 2022 18:00:00 UTC'),
    },
  ];
  constructor() { }

  ngOnInit(): void {
  }

}
