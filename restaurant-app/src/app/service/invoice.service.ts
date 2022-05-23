import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Invoice } from '../model/invoice/invoice';
import { InvoiceFilters } from '../model/invoice/invoice-filters';
import { InvoiceListView } from '../model/invoice/invoice-list-view';
import { InvoiceView } from '../model/invoice/invoice-view';
import { GeneralService } from './general.service';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {
  private COMMON_URL = `${GeneralService.BASE_URL}/supply/invoice`;
  private GET_INVOICE = `${this.COMMON_URL}/`;
  private GET_INVOICE_LIST = `${this.COMMON_URL}/list`;
  private ADD_INVOICE = `${this.COMMON_URL}/`;
  private UPDATE_INVOICE = `${this.COMMON_URL}/`;

  constructor(private http: HttpClient) { }

  getInvoice(invoiceNr: string): Observable<InvoiceView> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<InvoiceView>(this.GET_INVOICE + invoiceNr, {headers});
  }

  getInvoiceList(filters: InvoiceFilters): Observable<InvoiceListView> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<InvoiceListView>(this.GET_INVOICE_LIST, filters, {headers});
  }

  addInvoice(invoice: Invoice): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<any>(this.ADD_INVOICE, invoice, {headers});
  }

  updateInvoice(invoice: Invoice, invoiceNr: string): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.put<any>(this.UPDATE_INVOICE + invoiceNr, invoice, {headers});
  }
}
