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
import { MealService } from 'src/app/service/meal.service';
import { OrderService } from 'src/app/service/order.service';
import { PaymentService } from 'src/app/service/payment.service';
import { RestaurantService } from 'src/app/service/restaurant.service';

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
      if (value.type === 'Online')
        this.disabled = true;
      if (value.completed) {
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
  loading: boolean = false;
  isSuccessful: boolean = false;
  faXmark = faXmark;
  faCartShopping = faCartShopping;
  selectedDishId!: string;
  selectedDishAmount!: any;
  availableCities: string[] = [];
  errors: Map<string, string> = new Map<string, string>();
  tables!: number[];
  dishes!: DishListView[];
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

  constructor(private fb: FormBuilder, private mealService: MealService, 
              private orderService: OrderService, private restaurantService: RestaurantService) {
  }

  ngOnInit(): void {
    this.mealService.getAllMeals().subscribe(data => {
      this.dishes = data;
    }, error => {
      console.error(error);
    });
    let restaurantId = localStorage.getItem('restaurantId');
    this.restaurantService.getRestaurantShortInfo(restaurantId).subscribe(data => {
      this.availableCities = [];
      this.availableCities.push(data.city);
    }, error => {
      console.error(error);
    });
    this.restaurantService.getAllTables(restaurantId).subscribe(data => {
      this.tables = data;
    }, error => {
      console.error(error);
    })
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
    this.errors.clear();
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
    this.loading = true;
    if (this.newOrder) {
      this.orderService.reserveOrder(order).subscribe(data => {
        this.loading = false;
        this.isSuccessful = true;
      }, error => {
        this.errors = new Map(Object.entries(error.error));
        this.onlineOrderForm.markAsPristine();
        this.loading = false;
        this.isSuccessful = false;
        console.error(error);
      });
    }
  }

  private getOrderInfo(value: OrderShortInfo): void {
    if (value.type === 'Online') {
      this.orderService.getOrderInfo(value.id, value.type).subscribe(data => {
        this._orderInfo = data.onlineOrder;
        this._orderInfo.orderDate = new Date(this._orderInfo.orderDate);
        if (this._orderInfo.deliveryDate)
          this._orderInfo.deliveryDate = new Date(this._orderInfo.deliveryDate);

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
      }, error => {
        console.error(error);
      });
    } else {
      this.orderService.getOrderInfo(value.id, value.type).subscribe(data => {
        this._orderInfo = data.restaurantOrder;
        this._orderInfo.orderDate = new Date(this._orderInfo.orderDate);
        if (this._orderInfo.deliveryDate)
          this._orderInfo.deliveryDate = new Date(this._orderInfo.deliveryDate);

        this.restaurantOrderForm.patchValue({
          orders: this._orderInfo.orders,
          tableId: this._orderInfo.tableId,
        });
      }, error => {
        console.error(error);
      });
    }
  }

  removeDish(index: any, controls: any) {
    controls['orders'].value.splice(index, 1);
    controls['orders'].setValue(controls['orders'].value);
  }

  addDish(controls: any) {
    this.errors.clear();
    let number = Number.parseInt(this.selectedDishId);
    let dish =  this.dishes.find(el => el.id === number);
    if (dish) {
      let restaurantId = localStorage.getItem('restaurantId');
      this.loading = true;
      let order = this.mapDishToOrder(dish);
      this.mealService.validateOrder(restaurantId, order).subscribe(data => {
        this.loading = false;
        controls['orders'].value.push({
          dishId: order.dishId,
          name: order.name,
          amount: order.amount,
          price: order.price,
          isNew: true
        } as OrderInfo);
        controls['orders'].setValue(controls['orders'].value);

        this.selectedDishId = '';
        this.selectedDishAmount = null;
      }, error => {
        this.errors = new Map(Object.entries(error.error));
        this.loading = false;
        console.error(error);
      });
    }
  }

  mapDishToOrder(dish: DishListView): Order {
    return {
      dishId: dish.id,
      amount: this.selectedDishAmount,
      name: dish.name,
      price: dish.price * this.selectedDishAmount,
    } as Order;
  }

  onRestaurantOrderFormSubmit() {
    this.errors.clear();
    let price: number = 0;
    let orderArr: Order[] = [];
    this.restaurantOrderForm.get('orders')?.value.forEach((el: OrderInfo) => {
      if (el.isNew && el.isNew === true)
        orderArr.push(MapperUtility.mapOrderInfoToOrder(el));
    });
    orderArr.forEach(el => price += el.price);
    let order: RestaurantOrder = {
     tableId: this.restaurantOrderForm.get('tableId')?.value,
     restaurantId: localStorage.getItem('restaurantId'),
     orders: orderArr,
     price: price,
    }
    if (this.newOrder) {
      this.orderService.addRestaurantOrder(order).subscribe(data => {
        this.loading = false;
        this.isSuccessful = true;
      }, error => {
        this.errors = new Map(Object.entries(error.error));
        this.restaurantOrderForm.markAsPristine();
        this.loading = false;
        this.isSuccessful = false;
        console.error(error);
      });
    } else {
      this.orderService.updateRestaurantOrder(this._orderInfo.id, order).subscribe(data => {
        this.loading = false;
        this.isSuccessful = true;
      }, error => {
        this.errors = new Map(Object.entries(error.error));
        this.restaurantOrderForm.markAsPristine();
        this.loading = false;
        this.isSuccessful = false;
        console.error(error);
      });
    }
  }
}