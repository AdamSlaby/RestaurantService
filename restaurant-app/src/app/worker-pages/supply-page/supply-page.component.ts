import {Component, OnInit} from '@angular/core';
import {faSearch, faXmark} from "@fortawesome/free-solid-svg-icons";
import {SupplyInfo} from "../../model/supply/supply-info";
import {Unit} from "../../model/unit";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, Validators} from "@angular/forms";
import {Supply} from "../../model/supply/supply";

@Component({
  selector: 'app-supply-page',
  templateUrl: './supply-page.component.html',
  styleUrls: ['./supply-page.component.scss']
})
export class SupplyPageComponent implements OnInit {
  faSearch = faSearch;
  faXmark = faXmark;
  chosenIngredient!: string;
  supplies!: SupplyInfo[];
  suppliesCopy!: SupplyInfo[];
  errors: Map<string, string> = new Map<string, string>();
  units = Object.values(Unit);
  ingredientForm = this.fb.group({
    ingredientName: ['', [Validators.required]],
    quantity: [null, [Validators.required, Validators.min(0)]],
    unit: [null, [Validators.required]],
  });

  constructor(private modalService: NgbModal, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.supplies = [
      {
        restaurantId: 1,
        ingredientId: 1,
        ingredientName: 'Pomidor',
        quantity: 10,
        unit: Unit.KG,
      },
      {
        restaurantId: 1,
        ingredientId: 2,
        ingredientName: 'Ogórek',
        quantity: 10,
        unit: Unit.KG,
      },
    ];
    for (let i = 1; i <= 199; i++) {
      this.supplies.push(Object.assign({}, this.supplies[1]));
    }
    this.suppliesCopy = [...this.supplies];
  }

  get f() {
    return this.ingredientForm.controls;
  }

  open(content: any) {
    this.modalService.open(content, {}).result.then(() => {}).catch(() => {});
  }

  filterIngredientList() {
    //todo
    this.suppliesCopy = [...this.supplies];
    this.suppliesCopy = this.supplies
      .filter(el => el.ingredientName.toLowerCase().includes(this.chosenIngredient.toLowerCase()));
  }

  saveSuppliesState() {
    //todo
    console.log(this.supplies);
  }

  onIngredientFormSubmit(modal: any) {
    //todo
    let supply: Supply = {
      restaurantId: localStorage.getItem('restaurantId'),
      ingredientName: this.ingredientForm.get('ingredientName')?.value,
      quantity: this.ingredientForm.get('quantity')?.value,
      unit: this.ingredientForm.get('unit')?.value,
    };
    console.log(supply);
    modal.close();
  }

  removeSupply(supply: SupplyInfo, index: number) {
    this.suppliesCopy.splice(index, 1);
    this.supplies.splice(index, 1);
  }
}
