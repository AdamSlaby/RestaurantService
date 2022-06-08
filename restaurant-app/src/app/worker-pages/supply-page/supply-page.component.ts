import {Component, OnInit} from '@angular/core';
import {faSearch, faCheck} from "@fortawesome/free-solid-svg-icons";
import {SupplyInfo} from "../../model/supply/supply-info";
import {Unit} from "../../model/meal/unit";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, Validators} from "@angular/forms";
import {NewSupply} from "../../model/supply/new-supply";
import { SupplyService } from 'src/app/service/supply.service';
import { IngredientService } from 'src/app/service/ingredient.service';
import { UnitService } from 'src/app/service/unit.service';

@Component({
  selector: 'app-supply-page',
  templateUrl: './supply-page.component.html',
  styleUrls: ['./supply-page.component.scss']
})
export class SupplyPageComponent implements OnInit {
  faSearch = faSearch;
  faCheck = faCheck;
  chosenIngredient!: string;
  supplies!: SupplyInfo[];
  suppliesCopy!: SupplyInfo[];
  loading: boolean = false;
  correctSupply!: number;
  ingredients!: Map<number, String>;
  errors: Map<string, string> = new Map<string, string>();
  units!: Unit[];
  supplyForm = this.fb.group({
    ingredientName: ['', [Validators.required]],
    quantity: [null, [Validators.required, Validators.min(0)]],
    unit: [null, [Validators.required]],
  });

  constructor(private modalService: NgbModal, private fb: FormBuilder,
              private supplyService: SupplyService, private ingredientService: IngredientService, 
              private unitService: UnitService) { }

  ngOnInit(): void {
    let restaurantId = localStorage.getItem('restaurantId');
    this.supplyService.getAllSupplies(restaurantId).subscribe(data => {
      this.supplies = data;
      this.ingredientService.getAllIngredientsMap().subscribe(data => {
        let map = Object.entries(data);
        this.ingredients = new Map<number, string>();
        map.forEach(value => this.ingredients.set(Number.parseInt(value[0]), value[1]));
        this.supplies.forEach(el => el.ingredientName = this.ingredients.get(el.ingredientId));
        this.suppliesCopy = [...this.supplies];
      }, error => {
        console.error(error);
      });
    }, error => {
      console.error(error);
    });
    
    this.unitService.getAllInvoiceUnits().subscribe(data => {
      this.units = data;
    }, error => {
      console.error(error);
    })
  }

  get f() {
    return this.supplyForm.controls;
  }

  open(content: any) {
    this.modalService.open(content, {}).result.then(() => {}).catch(() => {});
  }

  filterIngredientList() {
    this.suppliesCopy = [...this.supplies];
    this.suppliesCopy = this.supplies
          .filter(el => el.ingredientName.toLocaleLowerCase().includes(this.chosenIngredient.toLowerCase()));
  }

  saveSupplyState(index: number) {
    this.correctSupply = -1;
    this.errors.clear();
    console.log(this.suppliesCopy[index]);
    this.supplyService.updateSupply(this.suppliesCopy[index]).subscribe(data => {
      this.correctSupply = index;
    }, error => {
      console.error(error);
      this.errors = new Map(Object.entries(error.error));
      let copy = new Map<string, string>(this.errors);
      copy.forEach((v, k) => this.errors.set(k + index, v));
    });
  }

  onIngredientFormSubmit(modal: any) {
    this.errors.clear();
    let supply: NewSupply = {
      restaurantId: localStorage.getItem('restaurantId'),
      ingredientName: this.supplyForm.get('ingredientName')?.value,
      quantity: this.supplyForm.get('quantity')?.value,
      unitId: this.supplyForm.get('unit')?.value,
    };
    this.loading = true;
    this.supplyService.addSupply(supply).subscribe(data => {
      this.loading = false;
      modal.close();
    }, error => {
        this.errors = new Map(Object.entries(error.error));
        this.supplyForm.markAsPristine();
        this.loading = false;
        console.error(error);
    });
  }

  removeSupply(supply: SupplyInfo, index: number) {
    this.suppliesCopy.splice(index, 1);
    this.supplies.splice(index, 1);
  }
}
