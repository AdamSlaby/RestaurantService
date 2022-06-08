import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GeneralService } from './general.service';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private COMMON_URL = `${GeneralService.BASE_URL}/order/pay`;
  private PAY_PAYPAL = `${this.COMMON_URL}/paypal/`;
  private PAY_PAYU = `${this.COMMON_URL}/payu/`;

  constructor(private http: HttpClient) { }

  payPayPal(orderId: number): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json',  accept: 'text/plain'}),
      responseType: 'text'
    };
    return this.http.get(this.PAY_PAYPAL + orderId, {responseType: 'text'});
  }

  payPayU(orderId: number): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json',  accept: 'text/plain'}),
      responseType: 'text'
    };
    return this.http.get(this.PAY_PAYU + orderId, {responseType: 'text'});
  }
}
