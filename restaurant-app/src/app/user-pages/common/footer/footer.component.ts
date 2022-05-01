import {Component, OnInit} from '@angular/core';
import {faCity, faEnvelope, faLocationDot, faPhone} from "@fortawesome/free-solid-svg-icons";
import {RestaurantInfo} from "../../../model/restaurant/restaurant-info";
import {RestaurantService} from "../../../service/restaurant.service";

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
  restaurantInfo!: RestaurantInfo;
  constructor(private restaurantService: RestaurantService) { }

  ngOnInit(): void {
    let restaurantId = sessionStorage.getItem('restaurantId');
    this.restaurantService.getRestaurantInfo(restaurantId).subscribe(data => {
      this.restaurantInfo = data;
      this.restaurantInfo.openingHours.forEach(el => {
        el.fromHour = new Date(el.fromHour);
        el.toHour = new Date(el.toHour);
      });
    }, error => {
      console.error(error);
    })
  }

}
