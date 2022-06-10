import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {faUserCircle, faXmark} from "@fortawesome/free-solid-svg-icons";
import {CalendarOptions, FullCalendarComponent} from "@fullcalendar/angular";
import plLocale from "@fullcalendar/core/locales/pl";
import {Schedule} from "../../model/schedule/schedule";
import {EmployeeInfo} from "../../model/employee/employee-info";
import {NgbCalendar, NgbDate, NgbDateStruct, NgbModal, NgbTimeStruct} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, NgForm, Validators} from "@angular/forms";
import {minDateValidator} from "../../validators/min-date-validator";
import {RegexPattern} from "../../model/regex-pattern";
import {personIdValidator} from "../../validators/pesel-validator";
import {WorkstationListView} from "../../model/workstation/workstation-list-view";
import {RestaurantShortInfo} from "../../model/restaurant/restaurant-short-info";
import {Employee} from "../../model/employee/employee";
import {EmployeeService} from "../../service/employee.service";
import {Credentials} from "../../model/credentials";
import { ScheduleInfo } from 'src/app/model/schedule/schedule-info';
import { DateUtility } from 'src/app/utility/date-utility';

@Component({
  selector: 'app-employee-info',
  templateUrl: './employee-info.component.html',
  styleUrls: ['./employee-info.component.scss']
})
export class EmployeeInfoComponent implements OnInit {
  @ViewChild('eventForm', {static: false}) private eventForm: any;
  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;
  @ViewChild('form') form!: NgForm;
  @Input() restaurants!: RestaurantShortInfo[]
  @Input() workstations!: WorkstationListView[];
  @Input() set employeeId(value: any) {
    this._employeeId = value;
    if (value !== -1) {
      this.employeeService.getEmployeeInfo(this._employeeId).subscribe(data => {
        this.employeeInfo = data;
        this.mapDatesToJsDate()
        this.updateEmployeeForm()
        this.isSuccessful = false;
        this.calendarOptions.events = [];
        console.log(this.employeeInfo.schedulesInfo);
        for (let schedule of this.employeeInfo.schedulesInfo) {
          this.addScheduleTocalendar(schedule, schedule.id);
        }
      }, error => {
        console.error(error);
      })
    } else {
      setTimeout(() => {
        this.form.resetForm();
        this.employeeForm.patchValue({active: true});
      }, 100);
    }
  }
  @Output() closeEmployeeDetails = new EventEmitter<void>();
  faUserCircle = faUserCircle;
  faXmark = faXmark;
  _employeeId!: any;
  editScheduleId: any;
  errors: Map<string, string> = new Map<string, string>();
  loading = false;
  startDate = this.calendar.getToday();
  calendarOptions: CalendarOptions = {
    locale: plLocale,
    initialView: 'timeGridWeek',
    slotMinTime: '07:00:00',
    slotMaxTime: '23:00:00',
    allDaySlot: false,
    displayEventTime: true,
    events: [],
    customButtons: {
      addEventButton: {
        text: 'Dodaj godziny pracy',
        click: () => {
          this.open(this.eventForm)
        },
      }
    },
    headerToolbar: {
      center: 'addEventButton',
    },
    eventContent: (event) => {
      return {
        html: '<div class="d-block d-md-flex">' +
          '<span class="position-relative" style="font-size: 16px;">'+ event.timeText +'</span>' +
          '<span class="editBtn ms-md-4 position-relative" id="edit-' + event.event.id + '"></span>' +
          '<span class="removeBtn ms-md-1 position-relative" id="remove-'+ event.event.id + '"></span>' +
          '</div>'
      };
    },
    eventDidMount: (event) => {
      let editBtn = event.el.children[0].children[0].children[1];
      editBtn.addEventListener('click', this.editEvent.bind(this, editBtn));
      let removeBtn = event.el.children[0].children[0].children[2];
      removeBtn.addEventListener('click', this.removeEvent.bind(this, removeBtn))
    }
  };
  schedule!: Schedule;
  employeeInfo!: EmployeeInfo;
  isSuccessful: boolean = false;
  credentials!: Credentials

  scheduleForm = this.fb.group({
    startShiftDate: ['', [Validators.required, minDateValidator(this.calendar)]],
    startShiftTime: ['', [Validators.required]],
    endShiftTime: ['', [Validators.required]],
  });

  employeeForm = this.fb.group({
    personId: [null, [Validators.required, Validators.pattern(RegexPattern.ID), personIdValidator()]],
    firstName: [null, [Validators.required, Validators.pattern(RegexPattern.NAME)]],
    surname: [null, [Validators.required, Validators.pattern(RegexPattern.SURNAME)]],
    phoneNr: [null, [Validators.required, Validators.pattern(RegexPattern.PHONE)]],
    accountNr: [null, [Validators.required, Validators.pattern(RegexPattern.ACCOUNT_NR)]],
    city: [null, [Validators.required, Validators.pattern(RegexPattern.CITY)]],
    street: [null, [Validators.required, Validators.pattern(RegexPattern.STREET)]],
    houseNr: [null, [Validators.required, Validators.pattern(RegexPattern.HOUSE_NR)]],
    flatNr: [null, [Validators.required, Validators.pattern(RegexPattern.FLAT_NR)]],
    postcode: [null, [Validators.required, Validators.pattern(RegexPattern.POSTCODE)]],
    employmentDate: [null, [Validators.required]],
    dismissalDate: [null],
    active: [null],
    workstation: [null, [Validators.required]],
    salary: [null, [Validators.required, Validators.min(1)]],
    restaurant: [null, [Validators.required]],
  })

  constructor(private modalService: NgbModal, private fb: FormBuilder,
              private calendar: NgbCalendar, private employeeService: EmployeeService) {
  }

  ngOnInit(): void {
  }

  get fs() {
    return this.scheduleForm.controls;
  }

  get fe() {
    return this.employeeForm.controls;
  }

  open(content: any) {
    this.modalService.open(content, {}).result.then(result => {
    }).catch(() => {});
  }

  onScheduleFormSubmit(modal: any, scheduleId?: number) {
    //todo
    this.errors.clear();
    if (this.editScheduleId && scheduleId) {
      let schedule: ScheduleInfo = {
        id: scheduleId,
        startShift: this.convertNgbDateTimeToDate(this.scheduleForm.get('startShiftDate')?.value,
          this.scheduleForm.get('startShiftTime')?.value),
        endShift: this.convertNgbDateTimeToDate(this.scheduleForm.get('startShiftDate')?.value,
          this.scheduleForm.get('endShiftTime')?.value)
      }
      this.editScheduleFromCalendar(schedule, this.editScheduleId, modal)
      this.editScheduleId = null;
    } else {
      let schedule: Schedule = {
        employeeId: this.employeeInfo.shortInfo.id,
        startShift: this.convertNgbDateTimeToDate(this.scheduleForm.get('startShiftDate')?.value,
          this.scheduleForm.get('startShiftTime')?.value),
        endShift: this.convertNgbDateTimeToDate(this.scheduleForm.get('startShiftDate')?.value,
          this.scheduleForm.get('endShiftTime')?.value)
      }
      this.addSchedule(schedule, modal);
    }
  }

  convertNgbDateTimeToDate(date: NgbDate, time: NgbTimeStruct): Date {
    return new Date(Date.UTC(date.year, date.month - 1, date.day, time.hour, time.minute, time.second));
  }

  convertNgbDateToDate(date: NgbDate): Date | null {
    if (date !== undefined)
      return new Date(Date.UTC(date.year, date.month - 1, date.day));
    else
      return null;
  }

  editScheduleFromCalendar(schedule: any, id: string, modal: any) {
    this.errors.clear();
    this.loading = true;
    this.employeeService.updateEmployeeSchedule(schedule).subscribe(data => {
      this.loading = false;
      let events = this.calendarOptions.events as Array<any>
      events.splice(events.indexOf(events.filter(el => el.id === id)[0]), 1);
      events.push({
        id: id,
        start: new Date(data.startShift),
        end: new Date(data.endShift),
      })
      this.calendarOptions.events = Object.assign([], events);
      modal.close();
    }, error => {
      this.errors = new Map(Object.entries(error.error));
      this.scheduleForm.markAsPristine();
      this.loading = false;
      console.error(error);
    });
  }

  addScheduleTocalendar(schedule: any, id: number) {
    let events = this.calendarOptions.events as Array<Object>
      events.push({
        id: id.toString(),
        start: schedule.startShift,
        end: schedule.endShift,
      });
      this.calendarOptions.events = Object.assign([], events);
  }

  addSchedule(schedule: any, modal: any) {
    this.errors.clear();
    this.loading = true;
    this.employeeService.addScheduleForEmployee(schedule).subscribe(data => {
      this.loading = false;
      data.startShift = new Date(data.startShift);
      data.endShift = new Date(data.endShift);
      this.addScheduleTocalendar(data, data.id);
      modal.close();
    }, error => {
      this.errors = new Map(Object.entries(error.error));
      this.scheduleForm.markAsPristine();
      this.loading = false;
      console.error(error);
    });
  }

  removeEvent(htmlElement: any) {
    let id = htmlElement.id.split('-')[1];
    this.employeeService.removeEmployeeSchedule(id).subscribe(data => {
      this.calendarComponent.getApi().getEventById(id)?.remove();
      let events = this.calendarOptions.events as Array<any>
      let index = events.indexOf(events.filter(el => el.id === id)[0], 0);
      events.splice(index, 1);
      this.calendarOptions.events = events;
    }, error => {
      console.error(error);
    })
  }

  editEvent(htmlElement: any) {
    let id = htmlElement.id.split('-')[1];
    let event = this.calendarComponent.getApi().getEventById(id);
    if (event?.start) {
      let startDate: NgbDateStruct = {
        year: event.start.getFullYear(),
        month: event?.start?.getMonth() + 1,
        day: event?.start?.getDate(),
      }
      this.scheduleForm.get('startShiftDate')?.setValue(startDate);
    }
    if (event?.start && event?.end) {
      let startTime: NgbTimeStruct = {
        hour: event?.start?.getHours(),
        minute: event?.start?.getMinutes(),
        second: event?.start?.getSeconds(),
      }
      this.scheduleForm.get('startShiftTime')?.setValue(startTime);
      let endTime: NgbTimeStruct = {
        hour: event?.end?.getHours(),
        minute: event?.end?.getMinutes(),
        second: event?.end?.getSeconds(),
      }
      this.scheduleForm.get('endShiftTime')?.setValue(endTime);
    }
    this.editScheduleId = id;
    this.open(this.eventForm);
  }

  dismiss(modal: any) {
    this.editScheduleId = undefined;
    modal.dismiss();
  }

  onEmployeeFormSubmit() {
    this.errors.clear();
    let employeeInfo: Employee = {
      name: this.employeeForm.get('firstName')?.value,
      surname: this.employeeForm.get('surname')?.value,
      workstationId: this.employeeForm.get('workstation')?.value,
      pesel: this.employeeForm.get('personId')?.value,
      phoneNr: this.employeeForm.get('phoneNr')?.value,
      salary: this.employeeForm.get('salary')?.value,
      active: this.employeeForm.get('active')?.value,
      accountNr: this.employeeForm.get('accountNr')?.value,
      employmentDate: this.convertNgbDateToDate(this.employeeForm.get('employmentDate')?.value),
      dismissalDate: this.employeeForm.get('dismissalDate')?.value ?
        this.convertNgbDateToDate(this.employeeForm.get('dismissalDate')?.value) : null,
      address: {
        city: this.employeeForm.get('city')?.value,
        street: this.employeeForm.get('street')?.value,
        houseNr: this.employeeForm.get('houseNr')?.value,
        flatNr: this.employeeForm.get('flatNr')?.value,
        postcode: this.employeeForm.get('postcode')?.value,
      },
      restaurantId: this.employeeForm.get('restaurant')?.value,
    }
    this.loading = true
    if (this.isNewEmployee()) {
      this.employeeService.addEmployee(employeeInfo).subscribe(data => {
        this.loading = false;
        this.isSuccessful = true;
        this.credentials = data;
      }, error => {
        this.errors = new Map(Object.entries(error.error));
        this.employeeForm.markAsPristine();
        this.loading = false;
        this.isSuccessful = false;
        console.error(error);
      })
    } else {
      this.employeeService.updateEmployee(employeeInfo, this._employeeId).subscribe(data => {
        this.loading = false;
        this.isSuccessful = true;
      }, error => {
        this.errors = new Map(Object.entries(error.error));
        this.employeeForm.markAsPristine();
        this.loading = false;
        this.isSuccessful = false;
        console.error(error);
      })
    }
  }

  getInvalidControl() {
    let controls = this.fe;
    for (let name in controls) {
      if (controls[name].invalid)
        console.log(controls[name]);
    }
    return true;
  }

  closeComponent() {
    this.closeEmployeeDetails.emit();
  }

  private updateEmployeeForm() {
    this.employeeForm.patchValue({
      personId: this.employeeInfo?.pesel,
      firstName: this.employeeInfo?.shortInfo?.name,
      surname: this.employeeInfo?.shortInfo?.surname,
      phoneNr: this.employeeInfo?.phoneNr,
      accountNr: this.employeeInfo?.accountNr,
      city: this.employeeInfo?.address?.city,
      street: this.employeeInfo?.address?.street,
      houseNr: this.employeeInfo?.address?.houseNr,
      flatNr: this.employeeInfo?.address?.flatNr,
      postcode: this.employeeInfo?.address?.postcode,
      employmentDate: this.employeeInfo?.employmentDate ? {
        year: this.employeeInfo?.employmentDate?.getFullYear(),
        month: this.employeeInfo?.employmentDate?.getMonth() + 1,
        day: this.employeeInfo?.employmentDate?.getDate()
      } as NgbDateStruct : null,
      dismissalDate: this.employeeInfo?.dismissalDate ? {
        year: this.employeeInfo?.dismissalDate?.getFullYear(),
        month: this.employeeInfo?.dismissalDate?.getMonth() + 1,
        day: this.employeeInfo?.dismissalDate?.getDate(),
      } as NgbDateStruct : null,
      active: this.employeeInfo?.active ? this.employeeInfo?.active : true,
      workstation: this.employeeInfo?.shortInfo?.workstationId,
      salary: this.employeeInfo?.salary,
      restaurant: this.employeeInfo?.restaurantInfo?.restaurantId,
    });
  }

  private mapDatesToJsDate() {
    this.employeeInfo.employmentDate = new Date(this.employeeInfo.employmentDate);
    if (this.employeeInfo.dismissalDate !== null)
      this.employeeInfo.dismissalDate = new Date(this.employeeInfo.dismissalDate);
    this.employeeInfo.schedulesInfo.forEach(el => {
      el.startShift = new Date(el.startShift);
      el.endShift = new Date(el.endShift);
    });
  }

  private isNewEmployee() {
    return this._employeeId == -1;
  }
}
