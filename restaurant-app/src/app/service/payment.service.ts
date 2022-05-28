import { HttpClient } from '@angular/common/http';
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

  payPayPal(orderId: number): Observable<string> {
    return this.http.get<string>(this.PAY_PAYPAL + orderId);
  }

  payPayU(orderId: number): Observable<string> {
    return this.http.get<string>(this.PAY_PAYU + orderId);
  }
}
