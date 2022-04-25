import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {OrderShortInfo} from "../../model/order/order-short-info";
import {faCartShopping, faXmark} from "@fortawesome/free-solid-svg-icons";
import {FormBuilder, NgForm, Validators} from "@angular/forms";
import {RegexPattern} from "../../model/regex-pattern";
import {PaymentMethod} from "../../model/payment-method";
import {OnlineOrderInfo} from "../../model/order/online-order-info";
import {RestaurantOrderInfo} from "../../model/order/restaurant-order-info";
import {DishListView} from "../../model/dish/dish-list-view";
import {OrderInfo} from "../../model/order/order-info";
import {OnlineOrder} from "../../model/order/online-order";
import {Order} from "../../model/order/order";
import {MapperUtility} from "../../utility/mapper-utility";
import {RestaurantOrder} from "../../model/order/restaurant-order";

@Component({
  selector: 'app-order-info',
  templateUrl: './order-info.component.html',
  styleUrls: ['./order-info.component.scss']
})
export class OrderInfoComponent implements OnInit {
  @ViewChild('form') onlineForm!: NgForm;
  @ViewChild('form2') restaurantForm!: NgForm;
  @Output() closeOrderDetails = new EventEmitter<void>();
  @Input() set orderInfo(value: OrderShortInfo | any) {
    this._orderInfo = null;
    this.disabled = false;
    this.newOrder = false;
    this.onlineOrderForm.enable();
    this.restaurantOrderForm.enable();
    if (typeof value === 'string') {
      this.newOrder = true;
      this.showOnlineForm = value === 'Online';
      setTimeout(() => {
        if (this.showOnlineForm) {
          this.onlineForm.resetForm();
          this.onlineOrderForm.patchValue({paymentMethod: PaymentMethod.CASH, orders: []});
        }
        else {
          this.restaurantForm.resetForm();
          this.restaurantOrderForm.patchValue({orders: [], tableId: ''});
        }
      }, 100);
    } else {
      this.showOnlineForm = value.type === 'Online';
      if (value.isCompleted) {
        this.disabled = true;
        if (value.type === 'Online')
          this.onlineOrderForm.disable();
        if (value.type === 'Restaurant')
          this.restaurantOrderForm.disable();
      }
      this.getOrderInfo(value);
    }
  }
  disabled: boolean = false;
  _orderInfo!: any;
  showOnlineForm: boolean = false;
  newOrder: boolean = false;
  faXmark = faXmark;
  faCartShopping = faCartShopping;
  selectedDishId!: string;
  selectedDishAmount!: any;
  availableCities: string[] = ['Kielce', 'Zagnańsk', 'Masłów', 'Bilcza', 'Morawica'];
  errors: Map<string, string> = new Map<string, string>();
  tables: number[] = [1, 2, 3, 4];
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
    paymentMethod: [PaymentMethod.CASH, [Validators.required]],
    orders: [[], [Validators.required]],
  });
  restaurantOrderForm = this.fb.group({
    orders: [[], [Validators.required]],
    tableId: ['', [Validators.required, Validators.min(1)]],
  });

  constructor(private fb: FormBuilder) {
  }

  ngOnInit(): void {
  }

  get fo() {
    return this.onlineOrderForm.controls;
  }

  get fr() {
    return this.restaurantOrderForm.controls;
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
    let orderArr: Order[] = [];
    this.onlineOrderForm.get('orders')?.value.forEach((el: OrderInfo) => orderArr.push(MapperUtility.mapOrderInfoToOrder(el)));
    let order: OnlineOrder = {
      name: this.onlineOrderForm.get('firstName')?.value,
      surname: this.onlineOrderForm.get('surname')?.value,
      phoneNr: this.onlineOrderForm.get('phoneNumber')?.value,
      email: this.onlineOrderForm.get('email')?.value,
      floor: this.onlineOrderForm.get('floor')?.value,
      restaurantId: localStorage.getItem('restaurantId'),
      orders: orderArr,
      address: {
        city: this.onlineOrderForm.get('city')?.value,
        street: this.onlineOrderForm.get('street')?.value,
        houseNr: this.onlineOrderForm.get('houseNr')?.value,
        flatNr: this.onlineOrderForm.get('flatNr')?.value,
        postcode: this.onlineOrderForm.get('postcode')?.value,
      },
      paymentMethod: this.onlineOrderForm.get('paymentMethod')?.value,
    }
    console.log(order);
    if (this.newOrder) {

    } else {

    }
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
        isPaid: false,
        restaurantInfo: {
          restaurantId: 1,
          city: 'Kielce',
          street: 'Warszawska',
        },
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
        phoneNumber: this._orderInfo.phoneNr,
        city: this._orderInfo.address.city,
        street: this._orderInfo.address.street,
        houseNr: this._orderInfo.address.houseNr,
        flatNr: this._orderInfo.address.flatNr,
        postcode: this._orderInfo.address.postcode,
        floor: this._orderInfo.address.floor,
        paymentMethod: this._orderInfo.paymentMethod,
        orders: this._orderInfo.orders,
      });
    } else {
      this._orderInfo = {
        id: 1,
        orderDate: new Date(),
        deliveryDate: null,
        tableId: 1,
        restaurantInfo: {
          restaurantId: 1,
          city: 'Kielce',
          street: 'Warszawska',
        },
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
      this.restaurantOrderForm.patchValue({
        orders: this._orderInfo.orders,
        tableId: this._orderInfo.tableId,
      });
    }
  }

  removeDish(index: any, controls: any) {
    controls['orders'].value.splice(index, 1);
    controls['orders'].setValue(controls['orders'].value);
  }

  addDish(controls: any) {
    let number = Number.parseInt(this.selectedDishId);
    let dish =  this.dishes.find(el => el.id === number);
    if (dish) {
      controls['orders'].value.push({
        dishId: dish.id,
        name: dish.name,
        amount: this.selectedDishAmount,
        price: dish.price * this.selectedDishAmount,
      } as OrderInfo)
      controls['orders'].setValue(controls['orders'].value);
    }
    this.selectedDishId = '';
    this.selectedDishAmount = null;
  }

  onRestaurantOrderFormSubmit() {
    //todo
    let orderArr: Order[] = [];
    this.restaurantOrderForm.get('orders')?.value.forEach((el: OrderInfo) => orderArr.push(MapperUtility.mapOrderInfoToOrder(el)));
    let order: RestaurantOrder = {
     tableId: this.restaurantOrderForm.get('tableId')?.value,
     restaurantId: localStorage.getItem('restaurantId'),
     orders: orderArr,
    }
    console.log(order);
    if (this.newOrder) {

    } else {

    }
  }
}