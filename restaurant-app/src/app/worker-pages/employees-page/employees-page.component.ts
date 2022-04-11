import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  HostListener,
  OnInit,
  QueryList,
  ViewChildren
} from '@angular/core';
import {faEye, faPenToSquare, faXmark, faUserPlus} from "@fortawesome/free-solid-svg-icons";
import {EmployeeList} from "../../model/employee-list";
import {NgbdSortableHeaderDirective} from "../../directive/ngbd-sortable-header.directive";
import {SortEvent} from "../../model/sort-event";
import {RestaurantShortInfo} from "../../model/restaurant-short-info";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Workstation} from "../../model/workstation";
import {WorkstationListView} from "../../model/workstation-list-view";

@Component({
  selector: 'app-employees-page',
  templateUrl: './employees-page.component.html',
  styleUrls: ['./employees-page.component.scss']
})
export class EmployeesPageComponent implements OnInit, AfterViewInit {
  @ViewChildren(NgbdSortableHeaderDirective) headers!: QueryList<NgbdSortableHeaderDirective>;
  faEye = faEye;
  faPenToSquare = faPenToSquare;
  faXmark = faXmark;
  faUserPlus = faUserPlus;
  innerHeight!: number;
  contentHeight!: number;
  keyword!: string;
  previousPage!: number;
  chosenRestaurant!: string;
  chosenEmployee!: string;
  pageNr!: number;
  maxSize: number = 10;
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

  ngAfterViewInit() {
    this.innerHeight = document.body.clientHeight;
    this.setContentHeight();
  }

  @HostListener('window:resize',['$event'])
  onResize($event: any) {
    this.innerHeight = document.body.clientHeight;
    this.setContentHeight();
  }

  open(content: any) {
    this.modalService.open(content, {}).result.then((result) => {});
  }

  seeEmployeeDetails(id: number) {
    this.showEmployeeDetails = true;
    this.selectedEmployeeId = id;
    setTimeout(() => {
      let employeeInfo = document.getElementById('employeeInfo');
      this.scrollIntoElement(employeeInfo);
    }, 1);
  }

  editEmployee(id: number) {
    this.showEmployeeDetails = true;
    this.selectedEmployeeId = id;
    setTimeout(() => {
      let employeeInfo = document.getElementById('employeeInfo');
      this.scrollIntoElement(employeeInfo);
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

  private setContentHeight() {
    let header = document.getElementById('header');
    if (header) {
      this.contentHeight = Math.floor(this.innerHeight - header.getBoundingClientRect().height - 1);
      this.cd.detectChanges();
    }
  }

  getWorkstationById(id: number): string {
    return this.workstations.filter(el => el.id === id)[0].name.toString();
  }

  scrollIntoElement(element: HTMLElement | null) {
    if (element)
      element.scrollIntoView({behavior: "smooth"});
  }

  addNewEmployee() {
    this.showEmployeeDetails = true;
    this.selectedEmployeeId = -1;
    setTimeout(() => {
      let employeeInfo = document.getElementById('employeeInfo');
      this.scrollIntoElement(employeeInfo);
    }, 1)
  }

  dismissEmployee(dismissEmployeeForm: any) {
    dismissEmployeeForm.close();
  }
}
