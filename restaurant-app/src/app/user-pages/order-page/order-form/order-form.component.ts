import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {RegexPattern} from "../../../model/regex-pattern";
import {faCreditCard, faMoneyBill1Wave} from "@fortawesome/free-solid-svg-icons";
import {faPaypal} from "@fortawesome/free-brands-svg-icons";
import {PaymentMethod} from "../../../model/payment-method";
import {OnlineOrder} from "../../../model/order/online-order";
import {BasketService} from "../../../service/basket.service";
import {Order} from "../../../model/order/order";
import { OrderService } from 'src/app/service/order.service';
import { RestaurantService } from 'src/app/service/restaurant.service';
import { PaymentService } from 'src/app/service/payment.service';

@Component({
  selector: 'app-order-form',
  templateUrl: './order-form.component.html',
  styleUrls: ['./order-form.component.scss']
})
export class OrderFormComponent implements OnInit, AfterViewInit {
  errors: Map<string, string> = new Map<string, string>();
  faMoneyBill1Wave = faMoneyBill1Wave;
  payment = PaymentMethod;
  faCreditCard = faCreditCard;
  faPaypal = faPaypal;
  loading = false;
  isSuccessful: boolean = true;
  availableCities: string[] = [];
  orderForm = this.fb.group({
    firstName: ['', [Validators.required, Validators.pattern(RegexPattern.NAME)]],
    surname: ['', [Validators.required, Validators.pattern(RegexPattern.SURNAME)]],
    email: ['', [Validators.required, Validators.email]],
    phoneNumber: ['', [Validators.required, Validators.pattern(RegexPattern.PHONE)]],
    city: ['', [Validators.required]],
    street: ['', [Validators.required, Validators.pattern(RegexPattern.STREET)]],
    houseNr: ['', [Validators.required, Validators.pattern(RegexPattern.HOUSE_NR)]],
    flatNr: ['', [Validators.required, Validators.pattern(RegexPattern.FLAT_NR)]],
    postcode: ['', [Validators.required, Validators.pattern(RegexPattern.POSTCODE)]],
    floor: [''],
    paymentMethod: [this.payment.PAYPAL, [Validators.required]],
  });

  constructor(private fb: FormBuilder, private route: ActivatedRoute,
              private basketService: BasketService, private orderService: OrderService, 
              private restaurantService: RestaurantService, private paymentService: PaymentService) { }

  ngOnInit(): void {
    let restaurantId = sessionStorage.getItem('restaurantId');
    this.restaurantService.getRestaurantShortInfo(restaurantId).subscribe(data => {
      this.availableCities = [];
      this.availableCities.push(data.city);
    }, error => {
      console.error(error);
    });
  }

  ngAfterViewInit() {
    this.route.fragment.subscribe(fragment => {
      let orderForm = document.getElementById('orderForm');
      setTimeout(() => {
        if (orderForm)
          orderForm.scrollIntoView();
      }, 1)
    })
  }

  get f() {
    return this.orderForm.controls;
  }

  onSubmit() {
    this.errors.clear();
    this.loading=true;
    let orders:Order[] = []
    this.basketService.basket.forEach(el => orders.push(el));
    let onlineOrder: OnlineOrder = {
      name: this.orderForm.get('firstName')?.value,
      surname: this.orderForm.get('surname')?.value,
      email: this.orderForm.get('email')?.value,
      phoneNr: this.orderForm.get('phoneNumber')?.value,
      floor: this.orderForm.get('floor')?.value,
      paymentMethod: this.orderForm.get('paymentMethod')?.value,
      restaurantId: sessionStorage.getItem('restaurantId'),
      address: {
        city: this.orderForm.get('city')?.value,
        street: this.orderForm.get('street')?.value,
        houseNr: this.orderForm.get('houseNr')?.value,
        flatNr: this.orderForm.get('flatNr')?.value,
        postcode: this.orderForm.get('postcode')?.value,
      },
      orders: orders,
    }
    this.loading = true;
    this.orderService.reserveOrder(onlineOrder).subscribe(data => {
      this.loading = false;
      this.isSuccessful = true;
      if (onlineOrder.paymentMethod === PaymentMethod.PAYPAL) {
        this.paymentService.payPayPal(data).subscribe(link => {
          window.location.href = link;
        }, error => {
          console.error(error);
        })
      } else if (onlineOrder.paymentMethod === PaymentMethod.PAYU) {
        this.paymentService.payPayU(data).subscribe(link => {
          window.location.href = link;
        }, error => {
          console.error(error);
        })
      }
    }, error => {
      this.errors = new Map(Object.entries(error.error));
        this.orderForm.markAsPristine();
        this.loading = false;
        this.isSuccessful = false;
        console.error(error);
    });
  }
}
