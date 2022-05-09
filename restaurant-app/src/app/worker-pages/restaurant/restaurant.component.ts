import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {faInfo, faMinus, faPenToSquare, faPlus} from "@fortawesome/free-solid-svg-icons";
import {FormBuilder, NgForm, Validators} from "@angular/forms";
import {RegexPattern} from "../../model/regex-pattern";
import {Restaurant} from "../../model/restaurant/restaurant";
import {NgbTimeAdapter} from "@ng-bootstrap/ng-bootstrap";
import {NgbTimeDateAdapter} from "../../adapter/timepicker-adapter";
import {Table} from "../../model/table";
import {RestaurantService} from "../../service/restaurant.service";

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
  isSuccessful: boolean = false;
  loading!: boolean;
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

  constructor(private fb: FormBuilder, private cd: ChangeDetectorRef,
              private restaurantService: RestaurantService) { }

  ngOnInit(): void {
    let restaurantId = localStorage.getItem('restaurantId');
    this.loading = true;
    this.restaurantService.getRestaurant(restaurantId).subscribe(data => {
      this.restaurantInfo = data;
      console.log(this.restaurantInfo);
      this.restaurantInfo.openingHours.forEach(el => {
        el.fromHour = new Date(el.fromHour);
        el.toHour = new Date(el.toHour);
      });
      console.log(this.restaurantInfo);
      this.loading = false;
      this.updateRestaurantFormValues();
    }, error => {
      console.error(error);
      this.loading = false;
    })
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
    this.errors.clear();
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
    this.loading = true;
    if (this.newRestaurant) {
      this.restaurantService.addRestaurant(restaurant).subscribe(response => {
        this.loading = false
        this.isSuccessful = true;
      }, error => {
        this.errors = new Map(Object.entries(error.error));
        this.restaurantForm.markAsPristine();
        this.loading = false;
        this.isSuccessful = false;
        console.error(error);
      });
    } else {
      this.restaurantService.updateRestaurant(restaurant, this.getRestaurantId()).subscribe(response => {
        this.loading = false;
        this.isSuccessful = true;
      }, error => {
        this.errors = new Map(Object.entries(error.error));
        this.restaurantForm.markAsPristine();
        this.loading = false;
        this.isSuccessful = false;
        console.error(error);
      })
    }
  }

  removeTable(index: number) {
    let restaurantId = this.getRestaurantId();
    this.restaurantService.removeRestaurantTable(restaurantId, this.tables[index].id).subscribe(data => {
        this.tables.splice(index, 1);
      },
      error => {
        console.error(error);
      });
  }

  addTable() {
    this.restaurantService.getTable(this.seatsAmount, this.getRestaurantId()).subscribe(data => {
      this.tables.push(data);
      this.seatsAmount = null;
    }, error => {
      console.error(error);
    });
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

  isErrorsContainsOpeningHour(index: number): boolean {
    for (let key in this.errors.keys()) {
      if (key.includes('openingHour[' + index + ']'))
        return true;
    }
    return false;
  }

  getRestaurantId(): any {
    if (!this.newRestaurant) {
      return  localStorage.getItem('restaurantId');
    } else
      return -1;
  }
}
