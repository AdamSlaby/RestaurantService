import {ChangeDetectorRef, Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {faEye, faPenToSquare, faUserPlus, faXmark} from "@fortawesome/free-solid-svg-icons";
import {EmployeeList} from "../../model/employee/employee-list";
import {NgbdSortableHeaderDirective} from "../../directive/ngbd-sortable-header.directive";
import {SortEvent} from "../../model/sort-event";
import {RestaurantShortInfo} from "../../model/restaurant/restaurant-short-info";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Workstation} from "../../model/workstation/workstation";
import {WorkstationListView} from "../../model/workstation/workstation-list-view";
import {HtmlUtility} from "../../utility/html-utility";

@Component({
  selector: 'app-employees-page',
  templateUrl: './employees-page.component.html',
  styleUrls: ['./employees-page.component.scss']
})
export class EmployeesPageComponent implements OnInit {
  @ViewChildren(NgbdSortableHeaderDirective) headers!: QueryList<NgbdSortableHeaderDirective>;
  faEye = faEye;
  faPenToSquare = faPenToSquare;
  faXmark = faXmark;
  faUserPlus = faUserPlus;
  keyword!: string;
  previousPage!: number;
  chosenRestaurant!: string;
  chosenEmployee!: string;
  pageNr!: number;
  pageSize: number = 10;
  selectedEmployeeId: any;
  showEmployeeDetails: boolean = false;
  workstations: WorkstationListView[] = [
    {
      id: 1,
      name: Workstation.CHEF,
    },
    {
      id: 2,
      name: Workstation.KITCHEN_MANAGER,
    },
    {
      id: 3,
      name: Workstation.COOK,
    },
  ];
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
        id: 1,
        name: 'Marek',
        surname: 'Bykowski',
        workstationId: 1,
      },
      {
        id: 2,
        name: 'Adam',
        surname: 'Bykowski',
        workstationId: 1,
      },
      {
        id: 3,
        name: 'Marcin',
        surname: 'Bykowski',
        workstationId: 1,
      },
      {
        id: 4,
        name: 'Łukasz',
        surname: 'Bykowski',
        workstationId: 1,
      },
      {
        id: 5,
        name: 'Marek',
        surname: 'Bykowski',
        workstationId: 1,
      },
      {
        id: 6,
        name: 'Marek',
        surname: 'Bykowski',
        workstationId: 1,
      },
      {
        id: 7,
        name: 'Marek',
        surname: 'Bykowski',
        workstationId: 1,
      },
      {
        id: 8,
        name: 'Marek',
        surname: 'Bykowski',
        workstationId: 1,
      },
      {
        id: 9,
        name: 'Marek',
        surname: 'Bykowski',
        workstationId: 1,
      },
      {
        id: 10,
        name: 'Marek',
        surname: 'Bykowski',
        workstationId: 1,
      },
    ]
  }

  constructor(private cd: ChangeDetectorRef, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.pageNr = 1;
    this.previousPage = 1;
  }

  open(content: any) {
    this.modalService.open(content, {}).result.then((result) => {});
  }

  editEmployee(id: number) {
    this.showEmployeeDetails = true;
    this.selectedEmployeeId = id;
    setTimeout(() => {
      HtmlUtility.scrollIntoView('employeeInfo');
    }, 1)
  }

  openDismissModal(id: number, dismissEmployeeForm: any) {
    //todo
    this.selectedEmployeeId = id;
    this.open(dismissEmployeeForm);
  }

  loadPage(page: number) {
    if (this.previousPage !== this.pageNr) {
      this.previousPage = this.pageNr;
    }
  }

  closeEmployeeDetails() {
    this.showEmployeeDetails = false;
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

  getWorkstationById(id: number): string {
    return this.workstations.filter(el => el.id === id)[0].name.toString();
  }

  addNewEmployee() {
    this.showEmployeeDetails = true;
    this.selectedEmployeeId = -1;
    setTimeout(() => {
      HtmlUtility.scrollIntoView('employeeInfo');
    }, 1)
  }

  dismissEmployee(dismissEmployeeForm: any) {
    dismissEmployeeForm.close();
  }

  resetFilters() {
    //todo
  }
}
