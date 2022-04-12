import {AfterViewInit, Component, OnInit} from '@angular/core';
import {NgbCalendar, NgbDate} from "@ng-bootstrap/ng-bootstrap";
import {faUserGroup, faCalendar, faClock} from "@fortawesome/free-solid-svg-icons";
import {ActivatedRoute, Router} from "@angular/router";
import {TimeUtility} from "../../../utility/time-utility";

@Component({
  selector: 'app-date-reservation',
  templateUrl: './date-reservation.component.html',
  styleUrls: ['./date-reservation.component.scss']
})
export class DateReservationComponent implements OnInit, AfterViewInit {
  faUserGroup = faUserGroup;
  faCalendar = faCalendar;
  faClock = faClock;
  selectedDate!: Date;
  today = this.calendar.getToday();
  selectedDay!: NgbDate;
  selectedGuestsNumber!: any;
  selectedGroupGuests!: any;
  guestsNumberArray = Array.from({length: 8}, (_, i) => i + 1);
  guestsGroupNumberArray = Array.from({length: 20}, (_, i) => i + 10);
  availableHours: Date[] = [
      new Date(), new Date(), new Date(), new Date(), new Date(), new Date(), new Date(), new Date()
  ]
  constructor(private calendar: NgbCalendar, private router: Router,
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

  onDateSelect($event: NgbDate) {
    this.selectedDate = new Date($event.year, $event.month - 1, $event.day);
    console.log(this.selectedDate);
  }

  getPeopleNr() {
    if (this.selectedGuestsNumber)
      return this.selectedGuestsNumber;
    else
      return this.selectedGroupGuests;
  }

  hourClick(hour: Date) {
    this.selectedDate.setHours(hour.getHours(), hour.getMinutes());
    sessionStorage.setItem('peopleNr', this.getPeopleNr());
    sessionStorage.setItem('date', new Date(TimeUtility.getUtcDAte(this.selectedDate)).toISOString());
    this.router.navigate( ['/reservation/customer'], {fragment: 'customerForm'});
  }
}
