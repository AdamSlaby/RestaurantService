import { Component, OnInit } from '@angular/core';
import {NgbDateAdapter, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NgbDateToDateAdapter} from "../../adapter/datepicker-date-adapter";
import {ReservationShortInfo} from "../../model/reservation/reservation-short-info";
import {faXmark, faEye, faPenToSquare, faSearch} from "@fortawesome/free-solid-svg-icons";
import {FormBuilder} from "@angular/forms";
import {ReservationInfo} from "../../model/reservation/reservation-info";
import { ReservationService } from 'src/app/service/reservation.service';

@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.scss'],
  providers: [{provide: NgbDateAdapter, useClass: NgbDateToDateAdapter}]
})
export class ReservationsComponent implements OnInit {
  selectedDate!: Date;
  faXmark = faXmark;
  faEye = faEye;
  faPenToSquare = faPenToSquare;
  faSearch = faSearch;
  now = new Date();
  reservationIndex!: number;
  selectedTableId!: any;
  selectedReservationId!: any;
  reservation!: ReservationInfo;
  reservationsCopy!: ReservationShortInfo[];
  reservations!: ReservationShortInfo[];

  constructor(private modalService: NgbModal, private fb: FormBuilder,
              private reservationService: ReservationService) { }

  ngOnInit(): void {
    this.selectedDate = new Date();
    this.getReservationsFromDate();
  }

  open(content: any) {
    this.modalService.open(content, {}).result.then(() => {}).catch(() => {});
  }

  getReservationsFromDate() {
    let restaurantId = localStorage.getItem('restaurantId');
    this.reservationService.getReservations(this.selectedDate, restaurantId).subscribe(data => {
      this.reservations = data;
      this.reservations.forEach(el => {
        el.fromHour = new Date(el.fromHour);
        el.toHour = new Date(el.toHour);
      });
      this.reservationsCopy = [...this.reservations];
    }, error => {
      console.error(error);
    });
  }

  viewReservation(id: number, reservationView: any) {
    this.reservationService.getReservationInfo(id).subscribe(data => {
      this.reservation = data;
      this.reservation.fromHour = new Date(this.reservation.fromHour);
      this.reservation.toHour = new Date(this.reservation.toHour);
      this.open(reservationView);
    }, error => {
      console.error(error);
    });
  }

  openRemoveReservationModal(id: number, i: number, modal: any) {
    this.reservationIndex = i;
    this.selectedReservationId = id;
    this.open(modal);
  }

  removeReservation(modal: any) {
    this.reservationService.deleteReservation(this.selectedReservationId).subscribe(data => {
      this.reservations.splice(this.reservationIndex, 1);
      this.reservationsCopy.splice(this.reservationIndex, 1);
      this.selectedReservationId = null;
      modal.close();
    }, error => {
      console.error(error);
    });
  }

  filterByTableId() {
    this.selectedReservationId = null;
    this.reservationsCopy = [...this.reservations];
    if (this.selectedTableId) {
      this.reservationsCopy = this.reservationsCopy.filter(el => el.tableIds.includes(this.selectedTableId));
    }
  }

  filterByReservationId() {
    this.selectedTableId = null;
    this.reservationsCopy = [...this.reservations];
    if (this.selectedReservationId) {
      this.reservationsCopy = this.reservationsCopy.filter(el => el.id === this.selectedReservationId);
    }
  }
}
