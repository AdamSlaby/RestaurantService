import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {faFileInvoice, faXmark} from "@fortawesome/free-solid-svg-icons";
import {FormBuilder, NgForm, Validators} from "@angular/forms";
import {RegexPattern} from "../../../model/regex-pattern";
import {Invoice} from "../../../model/invoice/invoice";
import {NgbDateAdapter} from "@ng-bootstrap/ng-bootstrap";
import {NgbDateToDateAdapter} from "../../../adapter/datepicker-date-adapter";
import {Unit} from "../../../model/unit";
import {TaxType} from "../../../model/invoice/tax-type";

@Component({
  selector: 'app-invoice-form',
  templateUrl: './invoice-form.component.html',
  styleUrls: ['./invoice-form.component.scss'],
  providers: [{provide: NgbDateAdapter, useClass: NgbDateToDateAdapter}],
})
export class InvoiceFormComponent implements OnInit {
  @ViewChild('form') form!: NgForm;
  @Output() closeInvoiceDetails = new EventEmitter<void>();

  @Input() set invoiceNr(value: string) {
    this._invoiceNr = value;
    if (value === '') {
      this.invoiceForm.patchValue({goods: []});
      setTimeout(() => {
        this.form.resetForm();
      }, 100);
    } else {
      this.invoiceInfo = {
        nr: '7/03-2018',
        restaurantId: 1,
        restaurantInfo: {
          restaurantId: 1,
          city: 'Kielce',
          street: 'Warszawska',
        },
        date: new Date(),
        buyerName: 'Restaurant',
        sellerName: 'Avivia',
        buyerAddress: {
          city: 'Kielce',
          street: 'Warszawska',
          houseNr: '102',
          flatNr: '20',
          postcode: '25-703',
        },
        sellerAddress: {
          city: 'Warszawa',
          street: 'Krakowska',
          houseNr: '110',
          flatNr: '20',
          postcode: '30-703',
        },
        buyerNip: '9999999999',
        sellerNip: '1111111111',
        completionDate: new Date(),
        price: 4000.50,
        goods: [
          {
            id: 1,
            ingredientId: 1,
            ingredient: 'Pomidor',
            quantity: 10,
            unit: Unit.KG,
            discount: 0.00,
            unitNetPrice: 5.00,
            netPrice: 50.00,
            taxType: TaxType.C,
            taxPrice: 2.5,
          }
        ],
      }
      this.invoiceForm.patchValue({
        nr: this.invoiceInfo.nr,
        date: this.invoiceInfo.date,
        sellerName: this.invoiceInfo.sellerName,
        buyerName: this.invoiceInfo.buyerName,
        sellerCity: this.invoiceInfo.sellerAddress.city,
        sellerStreet: this.invoiceInfo.sellerAddress.street,
        sellerHouseNr: this.invoiceInfo.sellerAddress.houseNr,
        sellerFlatNr: this.invoiceInfo.sellerAddress.flatNr,
        sellerPostcode: this.invoiceInfo.sellerAddress.postcode,
        buyerCity: this.invoiceInfo.buyerAddress.city,
        buyerStreet: this.invoiceInfo.buyerAddress.street,
        buyerHouseNr: this.invoiceInfo.buyerAddress.houseNr,
        buyerFlatNr: this.invoiceInfo.buyerAddress.flatNr,
        buyerPostcode: this.invoiceInfo.buyerAddress.postcode,
        sellerNip: this.invoiceInfo.sellerNip,
        buyerNip: this.invoiceInfo.buyerNip,
        completionDate: this.invoiceInfo.completionDate,
        price: this.invoiceInfo.price,
        goods: this.invoiceInfo.goods,
      });
    }
  }

  faXmark = faXmark;
  faFileInvoice = faFileInvoice;
  _invoiceNr!: string;
  invoiceInfo!: Invoice;
  units = Object.values(Unit);
  errors: Map<string, string> = new Map<string, string>();
  invoiceForm = this.fb.group({
    nr: [null, [Validators.required, Validators.maxLength(20)]],
    date: [null, [Validators.required]],
    sellerName: [null, [Validators.required, Validators.maxLength(65)]],
    buyerName: [null, [Validators.required, Validators.maxLength(65)]],
    sellerCity: [null, [Validators.required, Validators.pattern(RegexPattern.CITY)]],
    sellerStreet: [null, [Validators.required, Validators.pattern(RegexPattern.STREET)]],
    sellerHouseNr: [null, [Validators.required, Validators.pattern(RegexPattern.HOUSE_NR)]],
    sellerFlatNr: [null, [Validators.required, Validators.pattern(RegexPattern.FLAT_NR)]],
    sellerPostcode: [null, [Validators.required, Validators.pattern(RegexPattern.POSTCODE)]],
    buyerCity: [null, [Validators.required, Validators.pattern(RegexPattern.CITY)]],
    buyerStreet: [null, [Validators.required, Validators.pattern(RegexPattern.STREET)]],
    buyerHouseNr: [null, [Validators.required, Validators.pattern(RegexPattern.HOUSE_NR)]],
    buyerFlatNr: [null, [Validators.required, Validators.pattern(RegexPattern.FLAT_NR)]],
    buyerPostcode: [null, [Validators.required, Validators.pattern(RegexPattern.POSTCODE)]],
    sellerNip: [null, [Validators.required, Validators.pattern(RegexPattern.NIP)]],
    buyerNip: [null, [Validators.required, Validators.pattern(RegexPattern.NIP)]],
    completionDate: [null, [Validators.required]],
    price: [null, [Validators.required, Validators.min(0)]],
    goods: [null, [Validators.required]],
  });


  constructor(private fb: FormBuilder) {
  }

  ngOnInit(): void {
  }

  get f() {
    return this.invoiceForm.controls;
  }

  closeComponent() {
    this.closeInvoiceDetails.emit();
  }

  onInvoiceFormSubmit() {
    //todo
  }

  getInvalidControl() {
    let controls = this.f;
    for (let name in controls) {
      if (controls[name].invalid)
        console.log(controls[name]);
    }
    return true;
  }
}
