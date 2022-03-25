import {Component, OnInit} from '@angular/core';
import {NgbCalendar} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.scss']
})
export class ReservationComponent implements OnInit {
  reservationImgUrl: string = 'assets/reservation_image.jpg';

  constructor(private calendar: NgbCalendar) { }

  ngOnInit(): void {
  }
}
