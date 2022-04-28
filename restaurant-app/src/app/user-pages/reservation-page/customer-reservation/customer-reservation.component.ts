import {AfterViewInit, Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {FormBuilder, Validators} from "@angular/forms";
import {faUserGroup, faCalendar, faClock} from "@fortawesome/free-solid-svg-icons";
import {RegexPattern} from "../../../model/regex-pattern";
import {Reservation} from "../../../model/reservation/reservation";
import {DateUtility} from "../../../utility/date-utility";

@Component({
  selector: 'app-customer-reservation',
  templateUrl: './customer-reservation.component.html',
  styleUrls: ['./customer-reservation.component.scss']
})
export class CustomerReservationComponent implements OnInit, AfterViewInit {
  loading = false;
  faUserGroup = faUserGroup;
  faCalendar = faCalendar;
  faClock = faClock;
  errors: Map<string, string> = new Map<string, string>();
  customerForm = this.fb.group({
    firstName: ['', [Validators.required, Validators.pattern(RegexPattern.NAME)]],
    surname: ['', [Validators.required, Validators.pattern(RegexPattern.SURNAME)]],
    email: ['', [Validators.required, Validators.email]],
    phoneNumber: ['', [Validators.required, Validators.pattern(RegexPattern.PHONE)]],
  });

  constructor(private route: ActivatedRoute, private fb: FormBuilder) { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    this.route.fragment.subscribe((fragment) => {
      let element = document.querySelector('#' + fragment);
      if (element)
        element.scrollIntoView();
    })
  }

  get f() {
    return this.customerForm.controls;
  }

  getPeopleNr() {
    let peopleNr = sessionStorage.getItem('peopleNr');
    return peopleNr ? peopleNr : '0';
  }

  getDate(): string {
    let date = sessionStorage.getItem('date');
    return date ? date : new Date().toUTCString();
  }

  onSubmit() {
    //todo
    let reservation: Reservation = {
      restaurantId: localStorage.getItem('restaurantId'),
      name: this.customerForm.get('firstName')?.value,
      surname: this.customerForm.get('surname')?.value,
      email: this.customerForm.get('email')?.value,
      phoneNr: this.customerForm.get('phoneNumber')?.value,
      fromHour: new Date(this.getDate()),
      peopleNr: Number.parseInt(this.getPeopleNr()),
    }
    console.log(reservation);
  }
}
