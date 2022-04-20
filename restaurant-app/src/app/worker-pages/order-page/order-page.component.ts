import {Component, OnInit} from '@angular/core';
import {faPlus, faEye, faPenToSquare} from "@fortawesome/free-solid-svg-icons";
import {NgbCalendar, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {SortEvent} from "../../model/sort-event";
import {OrderListView} from "../../model/order/order-list-view";
import {OrderShortInfo} from "../../model/order/order-short-info";
import {HtmlUtility} from "../../utility/html-utility";

@Component({
  selector: 'app-order-page',
  templateUrl: './order-page.component.html',
  styleUrls: ['./order-page.component.scss']
})
export class OrderPageComponent implements OnInit {
  faPlus = faPlus;
  faEye = faEye;
  faPenToSquare = faPenToSquare;
  chosenOrder!: number;
  pageNr!: number;
  previousPage!: number;
  now!: Date;
  chosenDate!: NgbDateStruct;
  isCompleted: any = null;
  selectedOrder!: OrderShortInfo | any;
  showOrderDetails: boolean = false;
  orderList: OrderListView = {
    maxPage: 10,
    totalElements: 110,
    orders: [
      {
        id: 1,
        type: 'Online',
        price: 50.00,
        orderDate: new Date(),
        isCompleted: false,
      }
    ]
  }

  constructor() {
  }

  ngOnInit(): void {
    this.now = new Date();
    let orderShortInfo = this.orderList.orders[0];
    for (let i = 1; i <= 9; i++) {
      this.orderList.orders.push(JSON.parse(JSON.stringify(orderShortInfo)));
      this.orderList.orders[i].isCompleted = true;
      this.orderList.orders[i].type = 'Restaurant';
    }
    this.pageNr = 1;
    this.previousPage = 1;
  }

  getOrderById() {
    //todo
  }

  getOrderByDate() {
    //todo
  }

  getOrderByCompletion() {
    //todo
  }

  addOnlineOrder() {
    //todo
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
    //todo
    this.isCompleted = null;
  }

  onSort($event: SortEvent) {
    //todo
  }

  editOrder(order: OrderShortInfo) {
    this.showOrderDetails = true;
    this.selectedOrder = order;
    setTimeout(() => {
      HtmlUtility.scrollIntoView('orderForm')
    }, 1);
  }

  loadPage($event: number) {
    if (this.previousPage !== this.pageNr) {
      this.previousPage = this.pageNr;
    }
  }

  closeOrderDetails() {
    this.showOrderDetails = false;
  }
}
