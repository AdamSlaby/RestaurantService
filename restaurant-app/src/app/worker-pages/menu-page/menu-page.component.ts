import {Component, OnInit} from '@angular/core';
import {faEye, faMinus, faPenToSquare, faPlus, faXmark} from "@fortawesome/free-solid-svg-icons";
import {Type} from "../../model/meal/type";
import {MealListView} from "../../model/meal/meal-list-view";
import {SortEvent} from "../../model/sort-event";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {MenuView} from "../../model/menu-view";
import {FormBuilder, Validators} from "@angular/forms";
import {MealInfo} from "../../model/meal/meal-info";
import {IngredientView} from "../../model/meal/ingredient-view";
import {IngredientInfo} from "../../model/meal/ingredient-info";
import {Unit} from "../../model/meal/unit";
import { Season } from 'src/app/model/season';
import { Ingredient } from 'src/app/model/meal/ingredient';
import { MealMenu } from 'src/app/model/meal/meal-menu';
import { IngredientService } from 'src/app/service/ingredient.service';
import { MealService } from 'src/app/service/meal.service';
import { MealFilters } from 'src/app/model/meal/meal-filters';
import { MenuService } from 'src/app/service/menu.service';
import { TypeService } from 'src/app/service/type.service';
import { UnitService } from 'src/app/service/unit.service';

@Component({
  selector: 'app-menu-page',
  templateUrl: './menu-page.component.html',
  styleUrls: ['./menu-page.component.scss']
})
export class MenuPageComponent implements OnInit {
  faPlus = faPlus;
  faEye = faEye;
  faPenToSquare = faPenToSquare;
  faXmark = faXmark;
  faMinus = faMinus;
  error!: any;
  season = Season;
  chosenMeal!: string | null;
  chosenType: any = null;
  previousPage!: number;
  pageNr!: number;
  selectedMealId!: number;
  dish!: string;
  typeName!: string;
  errors: Map<string, string> = new Map<string, string>();
  imageUrl: any;
  meal!: MealInfo;
  newIngredients: IngredientView[] = [];
  chosenMealId!: number;
  fd: FormData = new FormData();
  units!: Unit[];
  loading: boolean = false;
  ingredients!: IngredientInfo[];
  types!: Type[];
  mealList!: MealListView;
  menus!: MenuView[];
  mealForm = this.fb.group({
    type: [null, [Validators.required]],
    name: ['', [Validators.required, Validators.maxLength(30)]],
    price: ['', [Validators.required, Validators.min(0)]],
    image: [null],
    ingredients: [''],
  });

  constructor(private modalService: NgbModal, private fb: FormBuilder,
              private ingredientService: IngredientService, 
              private mealService: MealService, private menuService: MenuService,
              private typeService: TypeService, private unitService: UnitService) { }

  ngOnInit(): void {
    this.ingredientService.getAllIngredients().subscribe(data => {
      this.ingredients = data
    }, error => {
      console.error(error);
    });

    this.getMealList(this.filters);

    this.menuService.getAllMenus().subscribe(data => {
      this.menus = data;
    }, error => {
      console.error(error);
    });

    this.typeService.getAllTypes().subscribe(data => {
      this.types = data;
    }, error => {
      console.error(error);
    })

    this.unitService.getAllUnits().subscribe(data => {
      this.units = data;
    }, error => {
      console.error(error);
    })
    this.pageNr = 1;
    this.previousPage = 1;
  }

  get f() {
    return this.mealForm.controls;
  }

  open(content: any) {
    this.modalService.open(content, {}).result.then(() => {}).catch(() => {});
  }

  filterMeals() {
    this.getMealList(this.filters);
  }

  resetFilters() {
    this.chosenType = null;
    this.chosenMeal = null;
    this.getMealList(this.filters);
  }

  getMealList(filters: MealFilters) {
    this.mealService.getMeals(filters).subscribe(data => {
      this.mealList = data;
      console.log(data);
    }, error => {
      console.error(error);
    });
  }

  addMeal(modal: any) {
    this.chosenMealId = -1;
    this.mealForm.reset();
    this.newIngredients = [];
    this.open(modal);
  }

  addMealType(modal: any) {
    this.open(modal);
  }

  onSort($event: SortEvent) {
    let filters = this.filters
    filters.sortEvent = $event;
    this.getMealList(filters);
  }

  loadPage(page: number) {
    let filters = this.filters;
    filters.pageNr = page - 1;
    if (this.previousPage !== this.pageNr) {
      this.mealService.getMeals(filters).subscribe(data => {
        this.previousPage = this.pageNr;
        this.mealList = data;
      }, error => {
        console.error(error);
      })
    }
  }

  editMeal(id: number, modal: any) {
    this.chosenMealId = id;
    this.mealService.getMealInfo(this.chosenMealId).subscribe(data => {
      this.meal = data;
      this.mealForm.patchValue({
        type: this.meal.typeId,
        name: this.meal.name,
        price: this.meal.price,
        ingredients: this.meal.ingredients as IngredientView[],
      });
      this.open(modal);
    }, error => {
      console.error(error);
    });
  }

  removeMeal(modal: any) {
    this.mealService.deleteMeal(this.selectedMealId).subscribe(data => {
      let meals = this.mealList.meals;
      let index = meals.findIndex(el => el.id === this.selectedMealId);
      meals.splice(index, 1);
      modal.close();
    }, error => {
      console.error(error);
    });
  }

  openRemoveModal(id: number, removeMealForm: any) {
    this.selectedMealId = id;
    this.open(removeMealForm);
  }

  getMenuBySeason(season: string) {
    return this.menus.filter(el => el.season === season)[0];
  }

  removeMealFromMenu(season: string, type: string, id: number) {
    let menu = this.menus.filter(el => el.season === season)[0];
    this.menuService.removeMealFromMenu(menu.id, id).subscribe(data => {
      let mealArr = menu.mealMap.get(type);
      if (mealArr) {
        let index = mealArr.findIndex(el => el.id === id);
        mealArr.splice(index, 1);
      }
    }, error => {
      console.error(error);
    });
  }

  addMealToMenu(season: string, type: string) {
    this.errors.clear();
    let menu = this.menus.filter(el => el.season === season)[0];
    let mealMenu: MealMenu = {
      meal: this.dish,
      menuId: menu.id,
    }
    this.menuService.addMealToMenu(mealMenu).subscribe(data => {
      let mealArray = menu.mealMap.get(type);
      if (mealArray) {
        mealArray.push(data);
      } else {
        let newArray = []
        newArray.push(data);
        menu.mealMap.set(type, newArray);
      }
    }, error => {
      this.errors = new Map(Object.entries(error.error));
      console.error(error);
    })
  }

  addType() {
    this.errors.clear();
    this.typeService.addType(this.typeName).subscribe(data => {
      this.types.push(data);
    }, error => {
      this.errors = new Map(Object.entries(error.error));
      console.error(error);
    });
  }

  editType(type: Type) {
    this.typeService.updateType(type.name, type.id).subscribe(data => {
    }, error => {
      this.errors = new Map(Object.entries(error.error));
      console.error(error);
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

  onMealFormSubmit(modal: any) {
    this.errors.clear();
    let resultArr: IngredientView[];
    this.fd.set('name', this.mealForm.get('name')?.value);
    this.fd.set('typeId', this.mealForm.get('type')?.value);
    this.fd.set('price', this.mealForm.get('price')?.value);
    this.fd.set('image', this.mealForm.get('image')?.value);
    let value = this.mealForm.get('ingredients')?.value;
    for (let ingredient of this.newIngredients) {
      let ingredientInfo = this.ingredients.filter(el => el.name === ingredient.name);
      if (ingredientInfo.length > 0)
        ingredient.id = ingredientInfo[0].id;
    }
    if (value instanceof Array) {
      resultArr = value.concat(this.newIngredients);
    } else
      resultArr = this.newIngredients;
    let ingredients = resultArr.map(el => {
      return {
        id: el.id,
        name: el.name,
        amount: el.amount,
        unitId: el.unit.id
      } as Ingredient 
    });
    this.fd.set('ingredients', JSON.stringify(ingredients));
    this.loading = true;
    if (this.chosenMealId === -1) {
      this.mealService.addMeal(this.fd).subscribe(data => {
        this.loading = false;
        modal.close();
      }, error => {
        this.errors = new Map(Object.entries(error.error));
        this.mealForm.markAsPristine();
        this.loading = false;
        console.error(error);
      });
    } else {
      this.mealService.updateMeal(this.fd, this.chosenMealId).subscribe(data => {
        this.loading = false;
        modal.close();
      }, error => {
        this.errors = new Map(Object.entries(error.error));
        this.mealForm.markAsPristine();
        this.loading = false;
        console.error(error);
      });
    }
  }

  uploadPhoto($event: any) {
    if ($event.target.files[0]) {
      this.mealForm.patchValue({
        image: $event.target.files[0],
      });
      const reader = new FileReader();
      reader.onload = () => {
        this.imageUrl = reader.result as string;
      }
      reader.readAsDataURL($event.target.files[0]);
    } else {
      this.mealForm.patchValue({
        image: null,
      });
    }
  }

  addMealIngredient() {
    this.newIngredients.push({
      id: -1,
      name: '',
      amount: -1,
      unit: this.units[0],
    } as IngredientView);
  }

  removeIngredient(ingredient: IngredientView) {
    let index = this.newIngredients.findIndex(el => el === ingredient);
    this.newIngredients.splice(index, 1);
  }

  removeExistingIngredient(ingredient: IngredientView) {
    let ingredients = this.meal.ingredients;
    let index = ingredients.findIndex(el => el === ingredient);
    ingredients.splice(index, 1);
  }

  get filters() {
    return {
      mealName: this.chosenMeal,
      typeId: this.chosenType,
      sortEvent: null,
      pageNr: 0
    } as MealFilters;
  }
}
