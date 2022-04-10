import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {faUserCircle} from "@fortawesome/free-solid-svg-icons";
import {CalendarOptions, FullCalendarComponent} from "@fullcalendar/angular";
import plLocale from "@fullcalendar/core/locales/pl";
import {Schedule} from "../../model/schedule";
import {EmployeeInfo} from "../../model/employee-info";
import {NgbCalendar, NgbDate, NgbDateStruct, NgbModal, NgbTimeStruct} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, Validators} from "@angular/forms";
import {minDateValidator} from "../../validators/min-date-validator";
import {RegexPattern} from "../../model/regex-pattern";
import {personIdValidator} from "../../validators/pesel-validator";
import {WorkstationListView} from "../../model/workstation-list-view";
import {RestaurantShortInfo} from "../../model/restaurant-short-info";


@Component({
  selector: 'app-employee-info',
  templateUrl: './employee-info.component.html',
  styleUrls: ['./employee-info.component.scss']
})
export class EmployeeInfoComponent implements OnInit {
  @ViewChild('eventForm', {static: false}) private addEvent: any;
  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;
  @Input() workstations!: WorkstationListView[];
  @Input() employeeId!: number;
  faUserCircle = faUserCircle;
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
    timeZone: '+0000',
    displayEventTime: true,
    events: [],
    customButtons: {
      addEventButton: {
        text: 'Dodaj godziny pracy',
        click: () => {
          this.open(this.addEvent)
        },
      }
    },
    headerToolbar: {
      center: 'addEventButton',
    },
    eventContent: (event) => {
      return {
        html: '<span style="font-size: 16px">'+ event.timeText +'</span>' +
          '<span class="editBtn" id="edit-' + event.event.id + '"></span>' +
          '<span class="removeBtn" id="remove-'+ event.event.id + '"></span>'
      };
    },
    eventDidMount: (event) => {
      let removeBtn = event.el.children[0].children[2];
      removeBtn.addEventListener('click', this.removeEvent.bind(this, removeBtn))
      let editBtn = event.el.children[0].children[1];
      editBtn.addEventListener('click', this.editEvent.bind(this, editBtn));
    }
  };
  schedule!: Schedule;
  employeeInfo: EmployeeInfo = {
    shortInfo: {
      id: '77102017553',
      name: 'Marek',
      surname: 'Bykowski',
      workstationId: 1,
    },
    address: {
      city: 'Kielce',
      street: 'Warszawska',
      postcode: '25-734',
      houseNr: '100',
      flatNr: '20',
    },
    phoneNr: '+48 602 602 602',
    accountNr: '85 9159 1036 1388 9882 8976 0258',
    salary: 3000.50,
    active: true,
    employmentDate: new Date(),
    dismissalDate: null,
    restaurantInfo: {
      restaurantId: 1,
      city: 'Kielce',
      street: 'Warszawska'
    },
    scheduleInfo: [
      {
        id: 1,
        startShift: new Date('8 April 2022 08:00:00 UTC'),
        endShift: new Date('8 April 2022 16:00:00 UTC'),
      }
    ]
  }
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

  scheduleForm = this.fb.group({
    startShiftDate: ['', [Validators.required, minDateValidator(this.calendar)]],
    startShiftTime: ['', [Validators.required]],
    endShiftTime: ['', [Validators.required]],
  });

  employeeForm = this.fb.group({
    personId: [this.employeeInfo.shortInfo.id, [Validators.required, Validators.pattern(RegexPattern.ID), personIdValidator()]],
    firstName: [this.employeeInfo.shortInfo.name, [Validators.required, Validators.pattern(RegexPattern.NAME)]],
    surname: [this.employeeInfo.shortInfo.surname, [Validators.required, Validators.pattern(RegexPattern.SURNAME)]],
    phoneNr: [this.employeeInfo.phoneNr, [Validators.required, Validators.pattern(RegexPattern.PHONE)]],
    accountNr: [this.employeeInfo.accountNr, [Validators.required, Validators.pattern(RegexPattern.ACCOUNT_NR)]],
    city: [this.employeeInfo.address.city, [Validators.required, Validators.pattern(RegexPattern.CITY)]],
    street: [this.employeeInfo.address.street, [Validators.required, Validators.pattern(RegexPattern.STREET)]],
    houseNr: [this.employeeInfo.address.houseNr, [Validators.required, Validators.pattern(RegexPattern.HOUSE_NR)]],
    flatNr: [this.employeeInfo.address.flatNr, [Validators.required, Validators.pattern(RegexPattern.FLAT_NR)]],
    postcode: [this.employeeInfo.address.postcode, [Validators.required, Validators.pattern(RegexPattern.POSTCODE)]],
    employmentDate: [{
      year: this.employeeInfo.employmentDate.getFullYear(),
      month: this.employeeInfo.employmentDate.getMonth() + 1,
      day: this.employeeInfo.employmentDate.getDate()
    } as NgbDateStruct, [Validators.required, minDateValidator(this.calendar)]],
    dismissalDate: [this.employeeInfo.dismissalDate ? {
      year: this.employeeInfo.dismissalDate?.getFullYear(),
      month: this.employeeInfo.dismissalDate?.getMonth() + 1,
      day: this.employeeInfo.dismissalDate?.getDate(),
    } as NgbDateStruct : null],
    active: [this.employeeInfo.active],
    workstation: [this.employeeInfo.shortInfo.workstationId, [Validators.required]],
    salary: [this.employeeInfo.salary, [Validators.required, Validators.min(1)]],
    restaurant: [this.employeeInfo.restaurantInfo.restaurantId, [Validators.required]],
  })

  constructor(private modalService: NgbModal, private fb: FormBuilder,
              private calendar: NgbCalendar) {
  }

  ngOnInit(): void {
    for (let schedule of this.employeeInfo.scheduleInfo) {
      this.addScheduleToCalendar(schedule, schedule.id);
    }
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

  onSubmit(modal: any) {
    //todo
    let schedule: Schedule = {
      startShift: this.convertNgbDateTimeToDate(this.scheduleForm.get('startShiftDate')?.value,
        this.scheduleForm.get('startShiftTime')?.value),
      endShift: this.convertNgbDateTimeToDate(this.scheduleForm.get('startShiftDate')?.value,
        this.scheduleForm.get('endShiftTime')?.value)
    }
    if (this.editScheduleId) {
      this.editScheduleFromCalendar(schedule, this.editScheduleId)
      this.editScheduleId = null;
    } else {
      this.addScheduleToCalendar(schedule, 10);
    }
    modal.close();
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

  editScheduleFromCalendar(schedule: any, id: string) {
    let events = this.calendarOptions.events as Array<any>
    events.splice(events.indexOf(events.filter(el => el.id === id)[0]), 1);
    events.push({
      id: id,
      start: schedule.startShift,
      end: schedule.endShift,
    })
    this.calendarOptions.events = Object.assign([], events);
  }

  addScheduleToCalendar(schedule: any, id: number) {
    let events = this.calendarOptions.events as Array<Object>
    events.push({
      id: id.toString(),
      start: schedule.startShift,
      end: schedule.endShift,
    });
    this.calendarOptions.events = Object.assign([], events);
  }

  removeEvent(htmlElement: any) {
    let id = htmlElement.id.split('-')[1];
    this.calendarComponent.getApi().getEventById(id)?.remove();
    let events = this.calendarOptions.events as Array<any>
    let index = events.indexOf(events.filter(el => el.id === id)[0], 0);
    events.splice(index, 1);
    this.calendarOptions.events = events;
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
        hour: event?.start?.getUTCHours(),
        minute: event?.start?.getUTCMinutes(),
        second: event?.start?.getUTCSeconds(),
      }
      this.scheduleForm.get('startShiftTime')?.setValue(startTime);
      let endTime: NgbTimeStruct = {
        hour: event?.end?.getUTCHours(),
        minute: event?.end?.getUTCMinutes(),
        second: event?.end?.getUTCSeconds(),
      }
      this.scheduleForm.get('endShiftTime')?.setValue(endTime);
    }
    this.editScheduleId = id;
    this.open(this.addEvent);
  }

  dismiss(modal: any) {
    this.editScheduleId = undefined;
    modal.dismiss();
  }

  onEmployeeFormSubmit() {
    let employeeInfo: EmployeeInfo = {
      shortInfo: {
        id: this.employeeForm.get('personId')?.value,
        name: this.employeeForm.get('firstName')?.value,
        surname: this.employeeForm.get('surname')?.value,
        workstationId: this.employeeForm.get('workstation')?.value,
      },
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
      scheduleInfo: [],
      restaurantInfo: {
        restaurantId: this.employeeForm.get('restaurant')?.value,
        city: '',
        street: '',
      }
    }
    console.log(employeeInfo);
  }

  getInvalidControl() {
    let controls = this.fe;
    for (let name in controls) {
      if (controls[name].invalid)
        console.log(controls[name]);
    }
    return true;
  }
}
