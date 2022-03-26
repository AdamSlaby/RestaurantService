import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {
  orderImgUrl: string = 'assets/order_image.jpg';

  constructor() { }

  ngOnInit(): void {
  }

}
