import {Component, OnInit, ViewChild} from '@angular/core';
import {faEye, faPenToSquare, faUserCircle} from "@fortawesome/free-solid-svg-icons";
import {CalendarOptions, FullCalendarComponent} from "@fullcalendar/angular";
import plLocale from "@fullcalendar/core/locales/pl";
import {Schedule} from "../../model/schedule";
import {EmployeeInfo} from "../../model/employee-info";
import {NgbCalendar, NgbDate, NgbDateStruct, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, Validators} from "@angular/forms";
import { NgbTimeStruct } from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-employee-info',
  templateUrl: './employee-info.component.html',
  styleUrls: ['./employee-info.component.scss']
})
export class EmployeeInfoComponent implements OnInit {
  @ViewChild('eventForm', {static: false}) private addEvent: any;
  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;
  faEye = faEye;
  faPenToSquare = faPenToSquare;
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
      workstation: 'Kucharz',
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
    dismissalDate: undefined,
    restaurantInfo: {
      restaurantId: 1,
      city: 'Kielce',
      street: 'Warszawska'
    },
    appraisalInfo: [
      {
        id: 1,
        date: new Date(),
        overall: 84.4,
        comment: 'Sumienny pracownik.',
      }
    ],
    scheduleInfo: [
      {
        id: 1,
        startShift: new Date('8 April 2022 08:00:00 UTC'),
        endShift: new Date('8 April 2022 16:00:00 UTC'),
      }
    ]
  }
  addScheduleForm = this.fb.group({
    startShiftDate: ['', [Validators.required]],
    startShiftTime: ['', [Validators.required]],
    endShiftTime: ['', [Validators.required]],
  });

  constructor(private modalService: NgbModal, private fb: FormBuilder,
              private calendar: NgbCalendar) {
  }

  ngOnInit(): void {
    for (let schedule of this.employeeInfo.scheduleInfo) {
      this.addScheduleToCalendar(schedule, schedule.id);
    }
  }

  get f() {
    return this.addScheduleForm.controls;
  }

  open(content: any) {
    this.modalService.open(content, {}).result.then(result => {
    }).catch(() => {});
  }

  onSubmit(modal: any) {
    //todo
    let schedule: Schedule = {
      startShift: this.convertNgbDateTimeToDate(this.addScheduleForm.get('startShiftDate')?.value,
        this.addScheduleForm.get('startShiftTime')?.value),
      endShift: this.convertNgbDateTimeToDate(this.addScheduleForm.get('startShiftDate')?.value,
        this.addScheduleForm.get('endShiftTime')?.value)
    }
    if (this.editScheduleId) {
      this.editScheduleFromCalendar(schedule, this.editScheduleId)
      this.editScheduleId = undefined;
    } else {
      this.addScheduleToCalendar(schedule, 10);
    }
    modal.close();
  }

  convertNgbDateTimeToDate(date: NgbDate, time: NgbTimeStruct): Date {
    return new Date(Date.UTC(date.year, date.month - 1, date.day, time.hour, time.minute, time.second));
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
    //todo set initial value to calendar
    if (event?.start) {
      let startDate: NgbDateStruct = {
        year: event.start.getFullYear(),
        month: event?.start?.getMonth(),
        day: event?.start?.getDate(),
      }
      this.addScheduleForm.get('startShiftDate')?.setValue(startDate);
    }
    if (event?.start && event?.end) {
      let startTime: NgbTimeStruct = {
        hour: event?.start?.getUTCHours(),
        minute: event?.start?.getUTCMinutes(),
        second: event?.start?.getUTCSeconds(),
      }
      this.addScheduleForm.get('startShiftTime')?.setValue(startTime);
      let endTime: NgbTimeStruct = {
        hour: event?.end?.getUTCHours(),
        minute: event?.end?.getUTCMinutes(),
        second: event?.end?.getUTCSeconds(),
      }
      this.addScheduleForm.get('endShiftTime')?.setValue(endTime);
    }
    this.editScheduleId = id;
    this.open(this.addEvent);
  }
}
