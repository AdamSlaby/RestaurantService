import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {OrderShortInfo} from "../../model/order/order-short-info";
import {faCartShopping, faXmark} from "@fortawesome/free-solid-svg-icons";
import {FormBuilder, Validators} from "@angular/forms";
import {RegexPattern} from "../../model/regex-pattern";
import {PaymentMethod} from "../../model/payment-method";
import {OnlineOrderInfo} from "../../model/order/online-order-info";
import {RestaurantOrderInfo} from "../../model/order/restaurant-order-info";
import {DishListView} from "../../model/dish/dish-list-view";
import {OrderInfo} from "../../model/order/order-info";

@Component({
  selector: 'app-order-info',
  templateUrl: './order-info.component.html',
  styleUrls: ['./order-info.component.scss']
})
export class OrderInfoComponent implements OnInit {
  @Output() closeOrderDetails =  new EventEmitter<void>();
  @Input() set orderInfo(value: OrderShortInfo | any) {
    if (value !== null) {
      if (value.isCompleted && value.type === 'Online')
        this.onlineOrderForm.disable();
      this.getOrderInfo(value);
    }
  }
  _orderInfo!: any;
  faXmark = faXmark;
  payment = PaymentMethod;
  faCartShopping = faCartShopping;
  availableCities: string[] = ['Kielce', 'Zagnańsk', 'Masłów', 'Bilcza', 'Morawica'];
  errors: Map<string, string> = new Map<string, string>();
  dishes: DishListView[] = [
    {
      id: 1,
      name: 'Pomidorowa',
      price: 20.50,
    },
    {
      id: 2,
      name: 'Ogórkowa',
      price: 22.50,
    },
    {
      id: 3,
      name: 'Filet z kurczaka',
      price: 30.50,
    },
  ]
  onlineOrderForm = this.fb.group({
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
    paymentMethod: [this.payment.CASH, [Validators.required]],
  });

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
  }

  get fo() {
    return this.onlineOrderForm.controls;
  }

  closeComponent() {
    this.closeOrderDetails.emit();
  }

  getInvalidControl() {
    let controls = this.fo;
    for (let name in controls) {
      if (controls[name].invalid)
        console.log(controls[name]);
    }
    return true;
  }

  onOnlineOrderFormSubmit() {
    //todo
  }

  private getOrderInfo(value: OrderShortInfo): void {
    if (value.type === 'Online') {
      this._orderInfo = {
        id: 1,
        name: 'Adam',
        surname: 'Marciniak',
        email: 'adsa1111@interia.pl',
        phoneNr: '+48602602602',
        floor: null,
        paymentMethod: PaymentMethod.CASH,
        orderDate: new Date(),
        deliveryDate: null,
        price: 42.50,
        address: {
          city: 'Kielce',
          street: 'Warszawska',
          postcode: '25-734',
          houseNr: '100',
          flatNr: '20',
        },
        orders: [
          {
            dishId: 1,
            name: 'Pomidorowa',
            amount: 2,
            price: 42.50,
          },
        ],
      } as OnlineOrderInfo;
      this.onlineOrderForm.patchValue({
        firstName: this._orderInfo.name,
        surname: this._orderInfo.surname,
        email: this._orderInfo.email,
        phoneNumber: this._orderInfo.phoneNumber,
        city: this._orderInfo.address.city,
        street: this._orderInfo.address.street,
        houseNr: this._orderInfo.address.houseNr,
        flatNr: this._orderInfo.address.flatNr,
        postcode: this._orderInfo.address.postcode,
        floor: this._orderInfo.address.floor,
        paymentMethod: this._orderInfo.paymentMethod,
      });
    } else {
      this._orderInfo = {
        id: 1,
        orderDate: new Date(),
        deliveryDate: null,
        orders: [
          {
            dishId: 1,
            name: 'Pomidorowa',
            amount: 2,
            price: 42.50,
          },
        ],
        price: 42.50,
      } as RestaurantOrderInfo;
    }
  }

  removeOrder(id: any) {
    let index = this._orderInfo.orders.findIndex((el: OrderInfo) => el.dishId);
    this._orderInfo.orders.splice(index, 1);
  }
}
