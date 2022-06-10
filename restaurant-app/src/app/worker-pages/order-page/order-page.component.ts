import {Component, OnInit} from '@angular/core';
import {faPlus, faEye, faPenToSquare} from "@fortawesome/free-solid-svg-icons";
import {NgbCalendar, NgbDateAdapter, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {SortEvent} from "../../model/sort-event";
import {OrderListView} from "../../model/order/order-list-view";
import {OrderShortInfo} from "../../model/order/order-short-info";
import {HtmlUtility} from "../../utility/html-utility";
import { NgbDateToDateAdapter } from 'src/app/adapter/datepicker-date-adapter';
import { OrderFilters } from 'src/app/model/order/order-filters';
import { OrderService } from 'src/app/service/order.service';

@Component({
  selector: 'app-order-page',
  templateUrl: './order-page.component.html',
  styleUrls: ['./order-page.component.scss'],
  providers: [{provide: NgbDateAdapter, useClass: NgbDateToDateAdapter}]
})
export class OrderPageComponent implements OnInit {
  faPlus = faPlus;
  faEye = faEye;
  faPenToSquare = faPenToSquare;
  pageNr!: number;
  previousPage!: number;
  now!: Date;
  chosenType: string = 'Online';
  chosenOrder!: any;
  chosenDate!: any;
  isCompleted: any = null;
  selectedOrder!: OrderShortInfo | any;
  showOrderDetails: boolean = false;
  orderList!: OrderListView;

  constructor(private orderService: OrderService) {
  }

  ngOnInit(): void {
    this.now = new Date();
    this.getOrderList(this.filters);
    this.pageNr = 1;
    this.previousPage = 1;
  }

  getOrderList(filters: OrderFilters) {
    this.orderService.getOrderList(filters).subscribe(data => {
      this.orderList = data;
      this.orderList.orders.forEach(el => el.orderDate = new Date(el.orderDate));
      console.log(this.orderList.orders[0]);
    }, error => {
      console.error(error);
    })
  }

  filterOrders() {
    let filters = this.filters;
    filters.pageNr = 0;
    this.getOrderList(filters);
  }

  addOnlineOrder() {
    this.showOrderDetails = true;
    this.selectedOrder = 'Online';
    setTimeout(() => {
      HtmlUtility.scrollIntoView('orderForm')
    }, 1);
  }

  addRestaurantOrder() {
    this.showOrderDetails = true;
    this.selectedOrder = 'Restaurant';
    setTimeout(() => {
      HtmlUtility.scrollIntoView('orderForm')
    }, 1);
  }

  resetFilters() {
    this.chosenOrder = null;
    this.isCompleted = null;
    this.chosenDate = null;
    this.chosenDate = null;
    this.getOrderList(this.filters);
  }

  onSort($event: SortEvent) {
    let filters = this.filters
    filters.sortEvent = $event;
    this.getOrderList(filters);
  }

  editOrder(order: OrderShortInfo) {
    this.showOrderDetails = true;
    this.selectedOrder = order;
    setTimeout(() => {
      HtmlUtility.scrollIntoView('orderForm')
    }, 1);
  }

  loadPage(page: number) {
    let filters = this.filters;
    filters.pageNr = page - 1;
    if (this.previousPage !== this.pageNr) {
      this.orderService.getOrderList(filters).subscribe(data => {
        this.previousPage = this.pageNr;
        this.pageNr = page;
        this.orderList = data;
        this.orderList.orders.forEach(el => el.orderDate = new Date(el.orderDate));
      }, error => {
        console.error(error);
      })
    }
  }

  closeOrderDetails() {
    this.showOrderDetails = false;
  }

  get filters() {
    return {
      restaurantId: localStorage.getItem('restaurantId'),
      orderId: this.chosenOrder,
      isCompleted: this.isCompleted,
      orderDate: this.chosenDate,
      type: this.chosenType,
      sortEvent: null,
      pageNr: this.pageNr - 1
    } as OrderFilters;
  }
}
