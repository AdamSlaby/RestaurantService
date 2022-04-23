import {Component, OnInit, ViewChild} from '@angular/core';
import {faInfo, faMinus, faPenToSquare, faPlus} from "@fortawesome/free-solid-svg-icons";
import {FormBuilder, NgForm, Validators} from "@angular/forms";
import {RegexPattern} from "../../model/regex-pattern";
import {Restaurant} from "../../model/restaurant/restaurant";
import {NgbTimeAdapter} from "@ng-bootstrap/ng-bootstrap";
import {NgbTimeDateAdapter} from "../../adapter/timepicker-adapter";
import {Table} from "../../model/table";

@Component({
  selector: 'app-restaurant',
  templateUrl: './restaurant.component.html',
  styleUrls: ['./restaurant.component.scss'],
  providers: [{provide: NgbTimeAdapter, useClass: NgbTimeDateAdapter}]
})
export class RestaurantComponent implements OnInit {
  @ViewChild('form') form!: NgForm;
  faInfo = faInfo;
  faMinus = faMinus;
  faPlus = faPlus;
  faPenToSquare = faPenToSquare;
  restaurantInfo!: Restaurant;
  seatsAmount!: any;
  newRestaurant: boolean = false;
  tables!: Table[];
  errors: Map<string, string> =  new Map<string, string>();
  restaurantForm = this.fb.group({
    email: [null, [Validators.required, Validators.email]],
    phoneNr: [null, [Validators.required, Validators.pattern(RegexPattern.PHONE)]],
    deliveryFee: [null, [Validators.required, Validators.min(0)]],
    minimalDeliveryPrice: [null, [Validators.required, Validators.min(0)]],
    city: [null, [Validators.required, Validators.pattern(RegexPattern.CITY)]],
    street: [null, [Validators.required, Validators.pattern(RegexPattern.STREET)]],
    houseNr: [null, [Validators.required, Validators.pattern(RegexPattern.HOUSE_NR)]],
    flatNr: [null, [Validators.required, Validators.pattern(RegexPattern.FLAT_NR)]],
    postcode: [null, [Validators.required, Validators.pattern(RegexPattern.POSTCODE)]],
  });

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.restaurantInfo = {
      restaurantId: 1,
      email: 'restaurant24@gmail.com',
      phoneNr: '+48600200300',
      deliveryFee: 5,
      minimalDeliveryPrice: 30,
      address: {
        city: 'Kielce',
        street: 'Niewiadomskiego',
        houseNr: '102',
        flatNr: '10',
        postcode: '25-000',
      },
      openingHours: [
        {
          hourId: 1,
          weekDayNr: 1,
          from_hour: new Date('04 Apr 2022 10:00:00 UTC'),
          to_hour: new Date('04 Apr 2022 18:00:00 UTC'),
        },
        {
          hourId: 2,
          weekDayNr: 2,
          from_hour: new Date('05 Apr 2022 10:00:00 UTC'),
          to_hour: new Date('05 Apr 2022 18:00:00 UTC'),
        },
        {
          hourId: 3,
          weekDayNr: 3,
          from_hour: new Date('06 Apr 2022 10:00:00 UTC'),
          to_hour: new Date('06 Apr 2022 18:00:00 UTC'),
        },
        {
          hourId: 4,
          weekDayNr: 4,
          from_hour: new Date('07 Apr 2022 10:00:00 UTC'),
          to_hour: new Date('07 Apr 2022 18:00:00 UTC'),
        },
        {
          hourId: 5,
          weekDayNr: 5,
          from_hour: new Date('08 Apr 2022 10:00:00 UTC'),
          to_hour: new Date('08 Apr 2022 18:00:00 UTC'),
        },
        {
          hourId: 6,
          weekDayNr: 6,
          from_hour: new Date('09 Apr 2022 10:00:00 UTC'),
          to_hour: new Date('09 Apr 2022 18:00:00 UTC'),
        },
        {
          hourId: 7,
          weekDayNr: 7,
          from_hour: new Date('10 Apr 2022 10:00:00 UTC'),
          to_hour: new Date('10 Apr 2022 18:00:00 UTC'),
        },
      ],
      tables: [
        {
          id: 1,
          seatsNr: 4,
        },
        {
          id: 2,
          seatsNr: 5,
        },
        {
          id: 3,
          seatsNr: 6,
        },
        {
          id: 4,
          seatsNr: 4,
        },
        {
          id: 5,
          seatsNr: 3,
        },
      ],
    } as Restaurant;
    this.updateRestaurantFormValues();
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

  updateRestaurantFormValues() {
    this.restaurantForm.patchValue({
      email: this.restaurantInfo.email,
      phoneNr: this.restaurantInfo.phoneNr,
      deliveryFee: this.restaurantInfo.deliveryFee,
      minimalDeliveryPrice: this.restaurantInfo.minimalDeliveryPrice,
      city: this.restaurantInfo.address.city,
      street: this.restaurantInfo.address.street,
      houseNr: this.restaurantInfo.address.houseNr,
      flatNr: this.restaurantInfo.address.flatNr,
      postcode: this.restaurantInfo.address.postcode,
    });
    this.tables = this.restaurantInfo.tables;
  }

  onRestaurantFormSubmit() {
    //todo
    let restaurant: Restaurant = {
      restaurantId: this.restaurantInfo.restaurantId,
      email: this.restaurantForm.get('email')?.value,
      phoneNr: this.restaurantForm.get('phoneNr')?.value,
      deliveryFee: this.restaurantForm.get('deliveryFee')?.value,
      minimalDeliveryPrice: this.restaurantForm.get('minimalDeliveryPrice')?.value,
      address: {
        city: this.restaurantForm.get('city')?.value,
        street: this.restaurantForm.get('street')?.value,
        houseNr: this.restaurantForm.get('houseNr')?.value,
        flatNr: this.restaurantForm.get('flatNr')?.value,
        postcode: this.restaurantForm.get('postcode')?.value,
      },
      openingHours: this.restaurantInfo.openingHours,
      tables: this.tables,
    };
    console.log(restaurant);
    if (this.newRestaurant) {

    } else {

    }
  }

  removeTable(index: number) {
    this.tables.splice(index, 1);
  }

  addTable() {
    this.tables.push({
      id: this.restaurantInfo.tables[this.restaurantInfo.tables.length - 1].id + 1,
      seatsNr: this.seatsAmount,
    });
    this.seatsAmount = null;
  }

  addNewRestaurant() {
    this.newRestaurant = true;
    this.tables = [];
    this.form.resetForm();
  }

  editRestaurantData() {
    this.newRestaurant = false;
    this.updateRestaurantFormValues();
  }
}
