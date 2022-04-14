import {Component, OnInit} from '@angular/core';
import {faEye, faPenToSquare, faPlus, faXmark, faMinus} from "@fortawesome/free-solid-svg-icons";
import {Type} from "../../model/meal/type";
import {MealListView} from "../../model/meal/meal-list-view";
import {SortEvent} from "../../model/sort-event";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {MenuView} from "../../model/menu-view";
import {Season} from "../../model/season";
import {MealShortView} from "../../model/meal/meal-short-view";
import {FormBuilder} from "@angular/forms";

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
  previousPage!: number;
  pageNr!: number;
  chosenType: any = null;
  selectedMealId!: number;
  dishName!: string;
  typeName!: string;
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
    maxPage: 10,
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
      season: Season.WINTER,
      mealMap: new Map<string, MealShortView[]>(),
    },
    {
      id: 2,
      season: Season.SPRING,
      mealMap: new Map<string, MealShortView[]>(),
    },
    {
      id: 3,
      season: Season.SUMMER,
      mealMap: new Map<string, MealShortView[]>(),
    },
    {
      id: 4,
      season: Season.AUTUMN,
      mealMap: new Map<string, MealShortView[]>(),
    },
  ];

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

  open(content: any) {
    this.modalService.open(content, {}).result.then(() => {}).catch(() => {});
  }

  getMealByName() {
    //todo
  }

  getMealByType() {
    //todo
  }

  resetFilters() {
    //todo
    this.chosenType = null;
  }

  addMeal() {
    //todo
  }

  addMealType(content: any) {
    //todo
    this.open(content);
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

  editMeal(id: number) {
    //todo
  }

  removeMeal(removeMealForm: any) {
    //todo
  }

  openRemoveModal(id: number, removeMealForm: any) {
    //todo
    this.selectedMealId = id;
    this.open(removeMealForm);
  }

  getMenuBySeason(season: Season) {
    return this.menus.filter(el => el.season === season)[0];
  }

  removeDishFromMenu(season: Season, type: string, id: number) {
    let mealArray = this.menus.filter(el => el.season === season)[0]
      .mealMap.get(type);
    console.log(mealArray);
    if (mealArray) {
      let index = mealArray.findIndex(el => el.id === id);
      mealArray.splice(index, 1);
    }
  }

  addMealToMenu(season: Season, type: string) {
    //todo
    let menu = this.menus.filter(el => el.season === season)[0];
    let mealArray = menu.mealMap.get(type);
    if (mealArray) {
      mealArray.push({
        id: 5,
        name: this.dishName,
      });
    } else {
      let newArray = []
      newArray.push({
        id: 5,
        name: this.dishName,
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
}
