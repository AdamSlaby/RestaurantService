import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MailInfo } from '../model/mail-info';
import { GeneralService } from './general.service';

@Injectable({
  providedIn: 'root'
})
export class MailService {
  private COMMON_URL = `${GeneralService.BASE_URL}/mail`;
  private SEND_MAIL = `${this.COMMON_URL}/`;

  constructor(private http: HttpClient) { }

  sendMail(mailInfo: MailInfo): Observable<any> {
    return this.http.post<any>(this.SEND_MAIL, mailInfo);
  }
}
