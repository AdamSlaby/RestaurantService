import {AfterViewInit, Component, OnInit} from '@angular/core';
import {NgbCalendar, NgbDate, NgbDateAdapter, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {faUserGroup, faCalendar, faClock} from "@fortawesome/free-solid-svg-icons";
import {ActivatedRoute, Router} from "@angular/router";
import {DateUtility} from "../../../utility/date-utility";
import { AvailableHour } from 'src/app/model/reservation/available-hours';
import { ReservationService } from 'src/app/service/reservation.service';
import { NgbDateToDateAdapter } from 'src/app/adapter/datepicker-date-adapter';

@Component({
  selector: 'app-date-reservation',
  templateUrl: './date-reservation.component.html',
  styleUrls: ['./date-reservation.component.scss'],
})
export class DateReservationComponent implements OnInit, AfterViewInit {
  faUserGroup = faUserGroup;
  faCalendar = faCalendar;
  faClock = faClock;
  selectedDate!: Date;
  today = this.calendar.getNext(this.calendar.getToday());
  selectedDay!: NgbDate;
  selectedGuestsNumber!: any;
  selectedGroupGuests!: any;
  guestsNumberArray = Array.from({length: 8}, (_, i) => i + 1);
  guestsGroupNumberArray = Array.from({length: 20}, (_, i) => i + 10);
  availableHours!: AvailableHour[];
  constructor(private calendar: NgbCalendar, private router: Router, 
              private reservationService: ReservationService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    let peopleNr = sessionStorage.getItem('peopleNr');
    if (peopleNr) {
      let number = parseInt(peopleNr);
      if (number > 10)
        this.selectedGroupGuests = number;
      else
        this.selectedGuestsNumber = number;
    }
    let date = sessionStorage.getItem('date');
    if (date) {
      this.selectedDate = new Date(date);
      this.selectedDay = new NgbDate(this.selectedDate.getFullYear(),
        this.selectedDate.getMonth() + 1, this.selectedDate.getDate());
    }
  }

  ngAfterViewInit() {
    this.route.fragment.subscribe(fragment => {
      let element = document.querySelector('#' + fragment);
      if (element)
        element.scrollIntoView();
    })
  }

  selectButton(number: number) {
    this.selectedGuestsNumber = number;
    this.selectedGroupGuests = undefined;
  }

  onGroupChange() {
    this.selectedGuestsNumber = undefined;
  }

  onDateSelect($event: NgbDateStruct) {
    this.selectedDate = new Date($event.year, $event.month - 1, $event.day, 14, 0);
    this.reservationService.getAvailableHours(this.selectedDate, this.getPeopleNr(), sessionStorage.getItem('restaurantId'))
      .subscribe(data => {
        this.availableHours = data;
        this.availableHours.forEach(el => el.hour = new Date(el.hour));
      }, error => {
        console.error(error);
      });
  }

  getPeopleNr() {
    if (this.selectedGuestsNumber)
      return this.selectedGuestsNumber;
    else
      return this.selectedGroupGuests;
  }

  hourClick(hour: AvailableHour) {
    this.selectedDate.setHours(hour.hour.getHours(), hour.hour.getMinutes());
    sessionStorage.setItem('peopleNr', this.getPeopleNr());
    sessionStorage.setItem('date', new Date(DateUtility.getUtcDAte(this.selectedDate)).toISOString());
    sessionStorage.setItem('tables', JSON.stringify(hour.tables));
    this.router.navigate( ['/reservation/customer'], {fragment: 'customerForm'});
  }
}
