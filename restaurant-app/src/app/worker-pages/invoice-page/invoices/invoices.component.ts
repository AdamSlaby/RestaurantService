import {Component, OnInit} from '@angular/core';
import {NgbDateAdapter, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {faPlus, faEye, faPenToSquare} from "@fortawesome/free-solid-svg-icons";
import {InvoiceListView} from "../../../model/invoice/invoice-list-view";
import {SortEvent} from "../../../model/sort-event";
import {HtmlUtility} from "../../../utility/html-utility";
import { InvoiceService } from 'src/app/service/invoice.service';
import { InvoiceFilters } from 'src/app/model/invoice/invoice-filters';
import { NgbDateToDateAdapter } from 'src/app/adapter/datepicker-date-adapter';

@Component({
  selector: 'app-invoices',
  templateUrl: './invoices.component.html',
  styleUrls: ['./invoices.component.scss'],
  providers: [{provide: NgbDateAdapter, useClass: NgbDateToDateAdapter}]
})
export class InvoicesComponent implements OnInit {
  faPlus = faPlus;
  faEye = faEye;
  faPenToSquare = faPenToSquare;
  chosenInvoice!: any;
  chosenInvoiceDate!: any;
  chosenSellerName!: any;
  pageNr!: number;
  showInvoiceDetails: boolean = false;
  previousPage!: number;
  selectedInvoiceNr!: string;
  invoiceList!: InvoiceListView;

  constructor(private invoiceService: InvoiceService) {
  }

  ngOnInit(): void {
    this.getInvoiceList(this.filters)
    this.pageNr = 1;
    this.previousPage = 1;
  }

  getInvoiceList(filters: InvoiceFilters) {
    this.invoiceService.getInvoiceList(filters).subscribe(data => {
      this.invoiceList = data;
      console.log(data);
      this.invoiceList.invoices.forEach(el => el.date = new Date(el.date));
    }, error => {
      console.error(error);
    })
  }

  filterInvoices() {
    let filters = this.filters
    console.log(filters);
    this.getInvoiceList(filters);
  }

  resetFilters() {
    this.chosenInvoice = "";
    this.chosenInvoiceDate = null;
    this.chosenSellerName = "";
    this.getInvoiceList(this.filters);
  }

  addNewInvoice() {
    this.selectedInvoiceNr = '';
    this.showInvoiceDetails = true;
    setTimeout(() => {
      HtmlUtility.scrollIntoView('invoiceInfo');
    }, 50);
  }

  onSort($event: SortEvent) {
    let filters = this.filters
    filters.sortEvent = $event;
    this.getInvoiceList(filters);
  }


  editInvoice(nr: string) {
    this.selectedInvoiceNr = nr;
    this.showInvoiceDetails = true;
    setTimeout(() => {
      HtmlUtility.scrollIntoView('invoiceInfo');
    }, 50);
  }

  loadPage(page: number) {
    let filters = this.filters;
    filters.pageNr = page - 1;
    if (this.previousPage !== this.pageNr) {
      this.invoiceService.getInvoiceList(filters).subscribe(data => {
        this.previousPage = this.pageNr;
        this.pageNr = page;
        this.invoiceList = data;
        this.invoiceList.invoices.forEach(el => el.date = new Date(el.date));
      }, error => {
        console.error(error);
      })
    }
  }

  closeInvoiceDetails() {
    this.showInvoiceDetails = false;
  }

  get filters() {
    return {
      restaurantId: localStorage.getItem('restaurantId'),
      nr: this.chosenInvoice,
      date: this.chosenInvoiceDate,
      sellerName: this.chosenSellerName,
      sortEvent: null,
      pageNr: 0
    } as InvoiceFilters;
  }
}
