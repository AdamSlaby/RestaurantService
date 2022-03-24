import { Component, OnInit } from '@angular/core';
import {faUserGroup, faCalendar} from "@fortawesome/free-solid-svg-icons";
import {NgbCalendar} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.scss']
})
export class ReservationComponent implements OnInit {
  reservationImgUrl: string = 'assets/reservation_image.jpg';
  faUserGroup = faUserGroup;
  faCalendar = faCalendar;
  today = this.calendar.getToday();
  selectedGuestsNumber!: any;
  selectedGroupGuests!: any;
  guestsNumberArray = Array.from({length: 8}, (_, i) => i + 1);
  guestsGroupNumberArray = Array.from({length: 20}, (_, i) => i + 10);
  constructor(private calendar: NgbCalendar) { }

  ngOnInit(): void {
  }

  selectButton(number: number) {
    this.selectedGuestsNumber = number;
    this.selectedGroupGuests = undefined;
  }

  onGroupChange() {
    this.selectedGuestsNumber = undefined;
  }
}
