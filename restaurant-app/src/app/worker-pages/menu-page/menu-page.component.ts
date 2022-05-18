import {Component, OnInit} from '@angular/core';
import {faEye, faMinus, faPenToSquare, faPlus, faXmark} from "@fortawesome/free-solid-svg-icons";
import {Type} from "../../model/meal/type";
import {MealListView} from "../../model/meal/meal-list-view";
import {SortEvent} from "../../model/sort-event";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {MenuView} from "../../model/menu-view";
import {MealShortView} from "../../model/meal/meal-short-view";
import {FormBuilder, Validators} from "@angular/forms";
import {MealInfo} from "../../model/meal/meal-info";
import {IngredientView} from "../../model/meal/ingredient-view";
import {IngredientInfo} from "../../model/meal/ingredient-info";
import {Unit} from "../../model/meal/unit";
import { Season } from 'src/app/model/season';
import { Ingredient } from 'src/app/model/meal/ingredient';

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
  chosenMeal!: string;
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
  ingredients: IngredientInfo[] = [
    {
      id: 1,
      name: 'Mięso',
    },
    {
      id: 2,
      name: 'Marchewka',
    },
    {
      id: 3,
      name: 'Szczypiorek',
    },
  ];
  types: Type[] = [
    {
      id: 1,
      name: 'Zupy',
    },
    {
      id: 2,
      name: 'Dania główne',
    },
    {
      id: 3,
      name: 'Ryby',
    },
    {
      id: 4,
      name: 'Desery',
    },
  ];
  mealList: MealListView = {
    totalElements: 110,
    meals: [
      {
        id: 1,
        type: 'Zupy',
        name: 'Pomidorowa',
        price: 20.50,
      },
    ]
  };
  menus: MenuView[] = [
    {
      id: 1,
      season: "Zima",
      mealMap: new Map<string, MealShortView[]>(),
    },
    {
      id: 2,
      season: "Wiosna",
      mealMap: new Map<string, MealShortView[]>(),
    },
    {
      id: 3,
      season: "Lato",
      mealMap: new Map<string, MealShortView[]>(),
    },
    {
      id: 4,
      season: "Jesień",
      mealMap: new Map<string, MealShortView[]>(),
    },
  ];
  mealForm = this.fb.group({
    type: [null, [Validators.required]],
    name: ['', [Validators.required, Validators.maxLength(30)]],
    price: ['', [Validators.required, Validators.min(0)]],
    image: [null],
    ingredients: [''],
  });

  constructor(private modalService: NgbModal, private fb: FormBuilder) { }

  ngOnInit(): void {
    let meal = this.mealList.meals[0];
    let dishes;
    for (let i = 1; i < 10; i++) {
      this.mealList.meals.push(Object.assign({}, meal));
    }
    for (let dishType of this.types) {
      dishes = [];
      for (let i = 1; i < 6; i++) {
        dishes.push(Object.assign({}, meal));
      }
      this.menus[0].mealMap.set(dishType.name, dishes);
    }
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
    //todo
  }

  resetFilters() {
    //todo
    this.chosenType = null;
  }

  addMeal(modal: any) {
    //todo
    this.chosenMealId = -1;
    this.mealForm.reset();
    this.newIngredients = [];
    this.open(modal);
  }

  addMealType(modal: any) {
    //todo
    this.open(modal);
  }

  onSort($event: SortEvent) {
    //todo
  }

  loadPage($event: number) {
    //todo
    if (this.previousPage !== this.pageNr) {
      this.previousPage = this.pageNr;
    }
  }

  editMeal(id: number, modal: any) {
    //todo
    this.chosenMealId = id;
    this.meal = {
      id: 1,
      name: 'Pomidorowa',
      price: 20.50,
      typeId: 1,
      imageUrl: 'assets/soup.jpg',
      ingredients: [
      ]
    };
    this.mealForm.patchValue({
      type: this.meal.typeId,
      name: this.meal.name,
      price: this.meal.price,
      ingredients: this.meal.ingredients as IngredientView[],
    });
    this.open(modal);
  }

  removeMeal(modal: any) {
    //todo
    let meals = this.mealList.meals;
    let index = meals.findIndex(el => el.id === this.chosenMealId);
    meals.splice(index, 1);
    modal.close();
  }

  openRemoveModal(id: number, removeMealForm: any) {
    //todo
    this.selectedMealId = id;
    this.open(removeMealForm);
  }

  getMenuBySeason(season: string) {
    return this.menus.filter(el => el.season === season)[0];
  }

  removeDishFromMenu(season: string, type: string, id: number) {
    let mealArray = this.menus.filter(el => el.season === season)[0]
      .mealMap.get(type);
    if (mealArray) {
      let index = mealArray.findIndex(el => el.id === id);
      mealArray.splice(index, 1);
    }
  }

  addMealToMenu(season: string, type: string) {
    //todo
    let menu = this.menus.filter(el => el.season === season)[0];
    let mealArray = menu.mealMap.get(type);
    if (mealArray) {
      mealArray.push({
        id: 5,
        name: this.dish,
      });
    } else {
      let newArray = []
      newArray.push({
        id: 5,
        name: this.dish,
      });
      menu.mealMap.set(type, newArray);
    }
  }

  removeType(id: number) {
    //todo
    let index = this.types.findIndex(el => el.id === id);
    this.types.splice(index, 1);
  }

  addType() {
    //todo
    this.types.push({
      id: 10,
      name: this.typeName,
    });
  }

  editType(type: Type) {
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

  onMealFormSubmit(modal: any) {
    //todo
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
    modal.close();
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
}
