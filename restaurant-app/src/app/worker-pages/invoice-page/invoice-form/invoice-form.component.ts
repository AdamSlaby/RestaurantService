import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {faFileInvoice, faXmark, faPlus} from "@fortawesome/free-solid-svg-icons";
import {FormBuilder, NgForm, Validators} from "@angular/forms";
import {RegexPattern} from "../../../model/regex-pattern";
import {Invoice} from "../../../model/invoice/invoice";
import {NgbDateAdapter} from "@ng-bootstrap/ng-bootstrap";
import {NgbDateToDateAdapter} from "../../../adapter/datepicker-date-adapter";
import {Unit} from "../../../model/unit";
import {TaxType} from "../../../model/invoice/tax-type";
import {Good} from "../../../model/invoice/good";

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
      setTimeout(() => {
        this.form.resetForm();
        this.invoiceForm.patchValue({goods: []});
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
          },
          {
            id: 2,
            ingredientId: 2,
            ingredient: 'Ogórek',
            quantity: 5,
            unit: Unit.KG,
            discount: 1.00,
            unitNetPrice: 6.00,
            netPrice: 30.00,
            taxType: TaxType.C,
            taxPrice: 1.5,
          },
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
  faPlus = faPlus;
  _invoiceNr!: string;
  invoiceInfo!: Invoice;
  units = Object.values(Unit);
  taxTypes = Object.values(TaxType);
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
    let invoice: Invoice = {
      nr: this.invoiceForm.get('nr')?.value,
      restaurantId: localStorage.getItem('restaurantId'),
      restaurantInfo: null,
      date: this.invoiceForm.get('date')?.value,
      buyerName: this.invoiceForm.get('buyerName')?.value,
      sellerName: this.invoiceForm.get('sellerName')?.value,
      buyerAddress: {
        city: this.invoiceForm.get('buyerCity')?.value,
        street: this.invoiceForm.get('buyerStreet')?.value,
        houseNr: this.invoiceForm.get('buyerHouseNr')?.value,
        flatNr: this.invoiceForm.get('buyerFlatNr')?.value,
        postcode: this.invoiceForm.get('buyerPostcode')?.value,
      },
      sellerAddress: {
        city: this.invoiceForm.get('sellerCity')?.value,
        street: this.invoiceForm.get('sellerStreet')?.value,
        houseNr: this.invoiceForm.get('sellerHouseNr')?.value,
        flatNr: this.invoiceForm.get('sellerFlatNr')?.value,
        postcode: this.invoiceForm.get('sellerPostcode')?.value,
      },
      buyerNip: this.invoiceForm.get('buyerNip')?.value,
      sellerNip: this.invoiceForm.get('sellerNip')?.value,
      completionDate: this.invoiceForm.get('completionDate')?.value,
      price: this.invoiceForm.get('price')?.value,
      goods: this.invoiceForm.get('goods')?.value
    };
    console.log(invoice);
    if (this._invoiceNr !== '') {

    } else {

    }
  }

  getInvalidControl() {
    let controls = this.f;
    for (let name in controls) {
      if (controls[name].invalid)
        console.log(controls[name]);
    }
    return true;
  }

  removeGoodFromInvoice(index: number) {
    let goods = this.invoiceForm.get('goods')?.value;
    goods.splice(index, 1);
    this.invoiceForm.get('goods')?.setValue(goods);
  }

  addGoodToInvoice() {
    let goods = this.invoiceForm.get('goods')?.value;
      goods.push({
      id: -1,
      ingredientId: -1,
      ingredient: '',
      quantity: 0,
      unit: null,
      unitNetPrice: 0,
      taxType: null,
      taxPrice: 0,
      netPrice: 0,
      discount: 0,
    } as Good);
    this.invoiceForm.get('goods')?.setValue(goods);
  }

  calculateTaxPrice(index: number) {
    let goods = this.invoiceForm.get('goods')?.value;
    if (goods) {
      let good = goods[index] as Good;
      if (good && good.netPrice > 0) {
        switch (good.taxType) {
          case TaxType.A:
            good.taxPrice = good.netPrice * 0.23;
            break;
          case TaxType.B:
            good.taxPrice = good.netPrice * 0.08;
            break;
          case TaxType.C:
            good.taxPrice = good.netPrice * 0.05;
            break;
          case TaxType.D:
            good.taxPrice = 0;
            break;
          case TaxType.E:
            good.taxPrice = 0;
            break;
        }
      }
    }
  }
}
