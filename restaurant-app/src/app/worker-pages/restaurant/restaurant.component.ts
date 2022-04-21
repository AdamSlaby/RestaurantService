import { Component, OnInit } from '@angular/core';
import {faInfo} from "@fortawesome/free-solid-svg-icons";
import {FormBuilder} from "@angular/forms";

@Component({
  selector: 'app-restaurant',
  templateUrl: './restaurant.component.html',
  styleUrls: ['./restaurant.component.scss']
})
export class RestaurantComponent implements OnInit {
  faInfo = faInfo;
  restaurantForm = this.fb.group({

  });

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
  }

  get f() {
    return this.restaurantForm.controls;
  }

  getInvalidControl() {
    let controls = this.f;
    for (let name in controls) {
      if (controls[name].invalid)
        console.log(controls[name]);
    }
    return true;
  }

  onRestaurantFormSubmit() {
    //todo
  }
}
