import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AvailableHour } from '../model/reservation/available-hours';
import { Reservation } from '../model/reservation/reservation';
import { ReservationInfo } from '../model/reservation/reservation-info';
import { ReservationShortInfo } from '../model/reservation/reservation-short-info';
import { GeneralService } from './general.service';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private COMMON_URL = `${GeneralService.BASE_URL}/restaurant/reservation`;
  private GET_RESERVATION_INFO = `${this.COMMON_URL}/info/`;
  private GET_RESERVATIONS = `${this.COMMON_URL}/list`;
  private GET_AVAILABLE_HOURS = `${this.COMMON_URL}/hours`;
  private ADD_RESERVATION = `${this.COMMON_URL}/`;
  private DELETE_RESERVATION = `${this.COMMON_URL}/`;

  constructor(private http: HttpClient) { }

  getReservationInfo(reservationId: number): Observable<ReservationInfo> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<ReservationInfo>(this.GET_RESERVATION_INFO + reservationId, {headers});
  }

  getReservations(date: Date, restaurantId: any): Observable<ReservationShortInfo[]> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    if (restaurantId === 'null')
      return this.http.get<ReservationShortInfo[]>(this.GET_RESERVATIONS + `?date=${date.toISOString()}`, {headers});
    else 
    return this.http.get<ReservationShortInfo[]>(this.GET_RESERVATIONS + `?date=${date.toISOString()}&restaurant=${restaurantId}`, {headers});
  }

  getAvailableHours(date: Date, peopleNr: number, restaurantId: any): Observable<AvailableHour[]> {
    return this.http.get<AvailableHour[]>(this.GET_AVAILABLE_HOURS + 
      `?date=${date.toISOString()}&people=${peopleNr}&restaurant=${restaurantId}`);
  }

  addReservation(reservation: Reservation): Observable<any> {
    return this.http.post<any>(this.ADD_RESERVATION, reservation);
  }

  deleteReservation(reservationId: number): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.delete<any>(this.DELETE_RESERVATION + reservationId, {headers});
  }
}
