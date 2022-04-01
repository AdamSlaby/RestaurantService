import {AfterViewInit, Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {FormBuilder, Validators} from "@angular/forms";
import {faUserGroup, faCalendar, faClock} from "@fortawesome/free-solid-svg-icons";

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
    firstName: ['', [Validators.required, Validators.pattern("^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32}$")]],
    surname: ['', [Validators.required, Validators.pattern("^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32}$")]],
    email: ['', [Validators.required, Validators.email]],
    phoneNumber: ['', [Validators.required, Validators.pattern("^(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)$")]],
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
    return sessionStorage.getItem('peopleNr');
  }

  getDate() {
    return sessionStorage.getItem('date');
  }

  onSubmit() {
    //todo
  }
}
