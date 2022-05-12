import {ChangeDetectorRef, Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {faEye, faPenToSquare, faUserPlus, faXmark} from "@fortawesome/free-solid-svg-icons";
import {EmployeeListView} from "../../model/employee/employee-list-view";
import {NgbdSortableHeaderDirective} from "../../directive/ngbd-sortable-header.directive";
import {SortEvent} from "../../model/sort-event";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {WorkstationListView} from "../../model/workstation/workstation-list-view";
import {HtmlUtility} from "../../utility/html-utility";
import {RestaurantShortInfo} from "../../model/restaurant/restaurant-short-info";
import {RestaurantService} from "../../service/restaurant.service";
import {EmployeeFilters} from "../../model/employee/employee-filters";
import {EmployeeService} from "../../service/employee.service";

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
  previousPage!: number;
  restaurants!: RestaurantShortInfo[];
  pageNr!: number;
  selectedEmployeeId: any;
  showEmployeeDetails: boolean = false;
  chosenWorkstation: any = null;
  chosenEmployeeId!: any;
  chosenSurname!: any;
  chosenActive: any = true;
  workstations!: WorkstationListView[];
  employeesList!: EmployeeListView;

  constructor(private cd: ChangeDetectorRef, private modalService: NgbModal,
              private restaurantService: RestaurantService,
              private employeeService: EmployeeService) { }

  ngOnInit(): void {
    this.pageNr = 1;
    this.previousPage = 1;
    this.restaurantService.getAllRestaurants().subscribe(data => {
      this.restaurants = data;
    }, error => {
      console.error(error);
    })
    this.employeeService.getWorkstations().subscribe(data => {
      this.workstations = data;
    }, error => {
      console.error(error);
    })
    this.employeeService.getEmployees(this.filters).subscribe(data => {
      this.employeesList = data;
    }, error => {
      console.error(error);
    });
  }

  open(content: any) {
    this.modalService.open(content, {}).result.then((result) => {}).catch(() => {});
  }

  editEmployee(id: number) {
    this.showEmployeeDetails = true;
    this.selectedEmployeeId = id;
    setTimeout(() => {
      HtmlUtility.scrollIntoView('employeeInfo');
    }, 1)
  }

  openDismissModal(id: number, dismissEmployeeForm: any) {
    this.selectedEmployeeId = id;
    this.open(dismissEmployeeForm);
  }

  loadPage(page: number) {
    let filters = this.filters;
    filters.pageNr = page - 1;
    if (this.previousPage !== this.pageNr) {
      this.employeeService.getEmployees(filters).subscribe(data => {
        this.previousPage = this.pageNr;
        this.employeesList = data;
      }, error => {
        console.error(error);
      });
    }
  }

  closeEmployeeDetails() {
    this.showEmployeeDetails = false;
  }

  onSort(event: SortEvent) {
    let filters = this.filters
    filters.sortEvent = event;
    this.employeeService.getEmployees(filters).subscribe(data => {
      this.employeesList = data;
    }, error => {
      console.error(error);
    });
  }

  filterEmployees() {
    let filters = this.filters
    this.employeeService.getEmployees(filters).subscribe(data => {
      this.employeesList = data;
    }, error => {
      console.error(error);
    });
  }

  resetFilters() {
    this.chosenActive = null;
    this.chosenEmployeeId = null;
    this.chosenSurname = null;
    this.chosenWorkstation = null;
    let filters = this.filters
    this.employeeService.getEmployees(filters).subscribe(data => {
      this.employeesList = data;
    }, error => {
      console.error(error);
    });
  }

  getWorkstationById(id: number): string {
    if (this.workstations) {
      return this.workstations.filter(el => el.id === id)[0].name.toString();
    }
    return '';
  }

  addNewEmployee() {
    this.showEmployeeDetails = true;
    this.selectedEmployeeId = -1;
    setTimeout(() => {
      HtmlUtility.scrollIntoView('employeeInfo');
    }, 1)
  }

  dismissEmployee(dismissEmployeeForm: any) {
    this.employeeService.dismissEmployee(this.selectedEmployeeId).subscribe(data => {
      dismissEmployeeForm.close();
      this.filterEmployees();
    }, error => {
      console.error(error);
    });
  }

  get filters() {
    return {
      restaurantId: localStorage.getItem('restaurantId'),
      employeeId: this.chosenEmployeeId,
      surname: this.chosenSurname,
      workstationId: this.chosenWorkstation,
      active: this.chosenActive,
      sortEvent: null,
      pageNr: 0
    } as EmployeeFilters;
  }
}
