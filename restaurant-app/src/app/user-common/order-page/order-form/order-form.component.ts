import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {RegexPattern} from "../../../model/regex-pattern";
import {faCreditCard, faMoneyBill1Wave} from "@fortawesome/free-solid-svg-icons";
import {faPaypal} from "@fortawesome/free-brands-svg-icons";
import {PaymentMethod} from "../../../model/payment-method";
import {OrderInfo} from "../../../model/order-info";

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
  availableCities: string[] = ['Kielce', 'Zagnańsk', 'Masłów', 'Bilcza', 'Morawica'];
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

  constructor(private fb: FormBuilder, private route: ActivatedRoute) { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    this.route.fragment.subscribe(fragment => {
      let orderForm = document.getElementById('orderForm');
      if (orderForm)
        orderForm.scrollIntoView();
    })
  }

  get f() {
    return this.orderForm.controls;
  }

  onSubmit() {
    this.errors.clear();
    this.loading=true;
    let orderInfo: OrderInfo = {
      name: this.orderForm.get('firstName')?.value,
      surname: this.orderForm.get('surname')?.value,
      email: this.orderForm.get('email')?.value,
      phoneNr: this.orderForm.get('phoneNumber')?.value,
      floor: this.orderForm.get('floor')?.value,
      paymentMethod: this.orderForm.get('paymentMethod')?.value,
      address: {
        city: this.orderForm.get('city')?.value,
        street: this.orderForm.get('street')?.value,
        houseNr: this.orderForm.get('houseNr')?.value,
        flatNr: this.orderForm.get('flatNr')?.value,
        postcode: this.orderForm.get('postcode')?.value,
      }
    }
    console.log(orderInfo);
  }
}
