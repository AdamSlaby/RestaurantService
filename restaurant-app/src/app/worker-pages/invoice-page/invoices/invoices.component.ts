import {Component, OnInit} from '@angular/core';
import {NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {faPlus, faEye, faPenToSquare} from "@fortawesome/free-solid-svg-icons";
import {InvoiceListView} from "../../../model/invoice/invoice-list-view";
import {SortEvent} from "../../../model/sort-event";
import {HtmlUtility} from "../../../utility/html-utility";

@Component({
  selector: 'app-invoices',
  templateUrl: './invoices.component.html',
  styleUrls: ['./invoices.component.scss']
})
export class InvoicesComponent implements OnInit {
  faPlus = faPlus;
  faEye = faEye;
  faPenToSquare = faPenToSquare;
  chosenInvoice!: string;
  invoiceDate!: NgbDateStruct;
  pageNr!: number;
  showInvoiceDetails: boolean = false;
  previousPage!: number;
  sellerName!: string;
  selectedInvoiceNr!: string;
  invoiceList: InvoiceListView = {
    totalElements: 110,
    invoices: [
      {
        nr: '7/03-2018',
        date: new Date(),
        sellerName: 'Avani',
        price: 4000.00
      },
    ]
  }

  constructor() {
  }

  ngOnInit(): void {
    for (let i = 1; i < 9; i++) {
      this.invoiceList.invoices.push(Object.assign({}, this.invoiceList.invoices[0]));
    }
    this.pageNr = 1;
    this.previousPage = 1;
  }

  getInvoiceByNr() {
    //todo
  }

  resetFilters() {
    //todo
  }

  getInvoiceBySellerName() {
    //todo
  }

  addNewInvoice() {
    this.selectedInvoiceNr = '';
    this.showInvoiceDetails = true;
    setTimeout(() => {
      HtmlUtility.scrollIntoView('invoiceInfo');
    }, 50);
  }

  onSort($event: SortEvent) {
    //todo
  }


  editInvoice(nr: string) {
    this.selectedInvoiceNr = nr;
    this.showInvoiceDetails = true;
    setTimeout(() => {
      HtmlUtility.scrollIntoView('invoiceInfo');
    }, 50);
  }

  loadPage($event: number) {
    if (this.previousPage !== this.pageNr) {
      this.previousPage = this.pageNr;
    }
    this.pageNr = $event;
  }

  closeInvoiceDetails() {
    this.showInvoiceDetails = false;
  }
}
