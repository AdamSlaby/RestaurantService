import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {faFileInvoice, faXmark, faPlus} from "@fortawesome/free-solid-svg-icons";
import {FormBuilder, NgForm, Validators} from "@angular/forms";
import {RegexPattern} from "../../../model/regex-pattern";
import {InvoiceView} from "../../../model/invoice/invoice-view";
import {NgbDateAdapter} from "@ng-bootstrap/ng-bootstrap";
import {NgbDateToDateAdapter} from "../../../adapter/datepicker-date-adapter";
import {Unit} from "../../../model/meal/unit";
import {TaxType} from "../../../model/invoice/tax-type";
import {GoodView} from "../../../model/invoice/good-view";
import { Invoice } from 'src/app/model/invoice/invoice';
import { IngredientInfo } from 'src/app/model/meal/ingredient-info';
import { InvoiceService } from 'src/app/service/invoice.service';
import { Good } from 'src/app/model/invoice/good';
import { IngredientService } from 'src/app/service/ingredient.service';
import { UnitService } from 'src/app/service/unit.service';

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
      this.ingredientService.getAllIngredientsMap().subscribe(data => {
        this.ingredients = new Map<number, string>();
        Object.entries(data).forEach(value => this.ingredients.set(Number.parseInt(value[0]), value[1]));
      }, error => {
        console.error(error);
      });
      setTimeout(() => {
        this.form.resetForm();
        this.invoiceForm.patchValue({goods: []});
      }, 100);
    } else {
      this.invoiceService.getInvoice(this._invoiceNr).subscribe(data => {
        this.invoiceInfo = data;
        this.invoiceInfo.date = new Date(this.invoiceInfo.date);
        this.invoiceInfo.completionDate = new Date(this.invoiceInfo.completionDate);
        this.ingredientService.getAllIngredientsMap().subscribe(data => {
          this.ingredients = new Map<number, string>();
          Object.entries(data).forEach(value => this.ingredients.set(Number.parseInt(value[0]), value[1]));
          this.invoiceInfo.goods.forEach(el => el.ingredient = this.ingredients.get(el.ingredientId));
        }, error => {
          console.error(error);
        });

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
      }, error => {
        console.error(error);
      });
    }
  }

  faXmark = faXmark;
  faFileInvoice = faFileInvoice;
  faPlus = faPlus;
  _invoiceNr!: string;
  invoiceInfo!: InvoiceView;
  loading: boolean = false;
  isSuccessful: boolean = false;
  units!: Unit[];
  ingredients!: Map<number, string>;
  taxType = TaxType;
  taxTypes = this.keys();
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

  constructor(private fb: FormBuilder, private invoiceService: InvoiceService,
              private ingredientService: IngredientService, private unitService: UnitService) {
  }

  ngOnInit(): void {
    this.unitService.getAllInvoiceUnits().subscribe(data => {
      this.units = data;
    }, error => {
      console.error(error);
    })
  }

  get f() {
    return this.invoiceForm.controls;
  }

  keys() : Array<string> {
    var keys = Object.keys(this.taxType);
    return keys.slice(keys.length / 2);
  }

  closeComponent() {
    this.closeInvoiceDetails.emit();
  }

  onInvoiceFormSubmit() {
    this.errors.clear();

    let invoice: Invoice = {
      nr: this.invoiceForm.get('nr')?.value,
      restaurantId: localStorage.getItem('restaurantId'),
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
      goods: this.mapGoodViewToGood()
    };
    this.loading = true;
    if (this._invoiceNr !== '') {
      this.invoiceService.updateInvoice(invoice, this._invoiceNr).subscribe(data => {
        this.loading = false;
        this.isSuccessful = true;
      }, error => {
        this.errors = new Map(Object.entries(error.error));
        this.invoiceForm.markAsPristine();
        this.loading = false;
        this.isSuccessful = false;
        console.error(error);
      });
    } else {
      this.invoiceService.addInvoice(invoice).subscribe(data => {
        this.loading = false;
        this.isSuccessful = true;
      }, error => {
        this.errors = new Map(Object.entries(error.error));
        this.invoiceForm.markAsPristine();
        this.loading = false;
        this.isSuccessful = false;
        console.error(error);
      });
    }
  }

  mapGoodViewToGood() {
    let goods = this.invoiceForm.get('goods')?.value as Array<GoodView>;
    return goods.map(el => {
      for (let entry of this.ingredients) {
        if (entry[1] === el.ingredient) {
          el.ingredientId = entry[0];
        }
      }
      return {
        ingredientId: el.ingredientId,
        quantity: el.quantity,
        unitId: el.unitId,
        unitNetPrice: el.unitNetPrice,
        discount: el.discount,
        netPrice: el.netPrice,
        taxType: el.taxType,
        taxPrice: el.taxPrice
      } as Good
    });
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
      unitId: -1,
      unit: null,
      unitNetPrice: 0,
      taxType: null,
      taxPrice: 0,
      netPrice: 0,
      discount: 0,
    } as GoodView);
    this.invoiceForm.get('goods')?.setValue(goods);
  }

  calculateTaxPrice(index: number) {
    let goods = this.invoiceForm.get('goods')?.value;
    if (goods) {
      let good = goods[index] as GoodView;
      if (good && good.netPrice > 0) {
        let value = Object.entries(this.taxType).filter((v, k) => v[0] === good.taxType)[0][1];
        switch (value) {
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
