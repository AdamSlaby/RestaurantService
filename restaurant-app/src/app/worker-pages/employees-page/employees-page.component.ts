import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  HostListener,
  OnInit,
  QueryList,
  ViewChildren
} from '@angular/core';
import {faEye, faPenToSquare, faXmark, faUserCircle} from "@fortawesome/free-solid-svg-icons";
import {EmployeeList} from "../../model/employee-list";
import {NgbdSortableHeaderDirective} from "../../directive/ngbd-sortable-header.directive";
import {SortEvent} from "../../model/sort-event";
import {RestaurantShortInfo} from "../../model/restaurant-short-info";

@Component({
  selector: 'app-employees-page',
  templateUrl: './employees-page.component.html',
  styleUrls: ['./employees-page.component.scss']
})
export class EmployeesPageComponent implements OnInit, AfterViewInit {
  @ViewChildren(NgbdSortableHeaderDirective) headers!: QueryList<NgbdSortableHeaderDirective>;
  faEye = faEye;
  faPenToSquare = faPenToSquare;
  faUserCircle = faUserCircle
  faXmark = faXmark;
  innerHeight!: number;
  contentHeight!: number;
  keyword!: string;
  previousPage!: number;
  chosenRestaurant!: string;
  chosenEmployee!: string;
  pageNr!: number;
  maxSize: number = 10;
  restaurants: RestaurantShortInfo[] = [
    {
      restaurantId: 1,
      city: 'Kielce',
      street: 'al. XI wieków Kielc'
    },
    {
      restaurantId: 2,
      city: 'Warszawa',
      street: 'Jagiellońska'
    },
    {
      restaurantId: 3,
      city: 'Kraków',
      street: 'Warszawska'
    }
  ]
  employeesList: EmployeeList = {
    maxPage: 10,
    employees: [
      {
        id: '77102017553',
        name: 'Marek',
        surname: 'Bykowski',
        workstation: 'Kucharz',
      },
      {
        id: '77102017553',
        name: 'Adam',
        surname: 'Bykowski',
        workstation: 'Kucharz',
      },
      {
        id: '77102017553',
        name: 'Marcin',
        surname: 'Bykowski',
        workstation: 'Kucharz',
      },
      {
        id: '77102017553',
        name: 'Łukasz',
        surname: 'Bykowski',
        workstation: 'Kucharz',
      },
      {
        id: '77102017553',
        name: 'Marek',
        surname: 'Bykowski',
        workstation: 'Kucharz',
      },
      {
        id: '77102017553',
        name: 'Marek',
        surname: 'Bykowski',
        workstation: 'Kucharz',
      },
      {
        id: '77102017553',
        name: 'Marek',
        surname: 'Bykowski',
        workstation: 'Kucharz',
      },
      {
        id: '77102017553',
        name: 'Marek',
        surname: 'Bykowski',
        workstation: 'Kucharz',
      },
      {
        id: '77102017553',
        name: 'Marek',
        surname: 'Bykowski',
        workstation: 'Kucharz',
      },
      {
        id: '77102017553',
        name: 'Marek',
        surname: 'Bykowski',
        workstation: 'Kucharz',
      },
    ]
  }

  constructor(private cd: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.pageNr = 1;
    this.previousPage = 1;
  }

  ngAfterViewInit() {
    this.innerHeight = document.body.clientHeight;
    this.setContentHeight();
  }

  @HostListener('window:resize',['$event'])
  onResize($event: any) {
    this.innerHeight = document.body.clientHeight;
    this.setContentHeight();
  }

  seeDetails(id: string) {
    //todo
  }

  edit(id: string) {
    //todo
  }

  dismiss(id: string) {
    //todo
  }

  loadPage(page: number) {
    if (this.previousPage !== this.pageNr) {
      this.previousPage = this.pageNr;
    }
  }

  onSort(event: SortEvent) {
    //todo
  }

  getEmployeesByRestaurant() {
    //todo
    console.log(this.chosenRestaurant.split(" ")[0]);
  }

  getEmployeeById() {
    //todo
  }

  getEmployeeBySurname() {
    //todo
  }

  private setContentHeight() {
    let header = document.getElementById('header');
    if (header) {
      this.contentHeight = Math.floor(this.innerHeight - header.getBoundingClientRect().height - 1);
      this.cd.detectChanges();
    }
  }
}
