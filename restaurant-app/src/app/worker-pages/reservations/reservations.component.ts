import { Component, OnInit } from '@angular/core';
import {NgbDateAdapter, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NgbDateToDateAdapter} from "../../adapter/datepicker-date-adapter";
import {ReservationShortInfo} from "../../model/reservation/reservation-short-info";
import {faXmark, faEye, faPenToSquare, faSearch} from "@fortawesome/free-solid-svg-icons";
import {FormBuilder} from "@angular/forms";
import {ReservationInfo} from "../../model/reservation/reservation-info";

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
  reservations: ReservationShortInfo[] = [
    {
      id: 1,
      tableId: 1,
      fromHour: new Date(),
      toHour: new Date(),
    },
    {
      id: 2,
      tableId: 2,
      fromHour: new Date(),
      toHour: new Date(),
    },
  ];

  constructor(private modalService: NgbModal, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.selectedDate = new Date();
    for (let i = 1; i <= 9; i++)
      this.reservations.push(Object.assign({}, this.reservations[0]));
    this.reservationsCopy = [...this.reservations];
  }

  open(content: any) {
    this.modalService.open(content, {}).result.then(() => {}).catch(() => {});
  }

  getReservationsFromDate() {
    //todo
  }

  viewReservation(id: number, reservationView: any) {
    this.reservation = {
      id: 1,
      tableId: 1,
      fromHour: new Date(),
      toHour: new Date(),
      name: 'Marek',
      surname: 'Marciniak',
      email: 'adsa3333@interia.pl',
      phoneNr: '+48602602602',
    }
    this.open(reservationView);
  }

  openRemoveReservationModal(id: number, i: number, modal: any) {
    this.reservationIndex = i;
    this.selectedReservationId = id;
    this.open(modal);
  }

  removeReservation(modal: any) {
    //todo
    this.reservations.splice(this.reservationIndex, 1);
    this.reservationsCopy.splice(this.reservationIndex, 1);
    modal.close();
    this.selectedReservationId = null;
  }

  filterByTableId() {
    this.selectedReservationId = null;
    this.reservationsCopy = [...this.reservations];
    if (this.selectedTableId) {
      this.reservationsCopy = this.reservationsCopy.filter(el => el.tableId === this.selectedTableId);
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
