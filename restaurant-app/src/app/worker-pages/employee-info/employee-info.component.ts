import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {faEye, faPenToSquare, faUserCircle} from "@fortawesome/free-solid-svg-icons";
import {CalendarOptions, FullCalendarComponent} from "@fullcalendar/angular";
import plLocale from "@fullcalendar/core/locales/pl";
import {Schedule} from "../../model/schedule";
import {EmployeeInfo} from "../../model/employee-info";
import {NgbCalendar, NgbDate, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, Validators} from "@angular/forms";
import {NgbTime} from "@ng-bootstrap/ng-bootstrap/timepicker/ngb-time";


@Component({
  selector: 'app-employee-info',
  templateUrl: './employee-info.component.html',
  styleUrls: ['./employee-info.component.scss']
})
export class EmployeeInfoComponent implements OnInit, AfterViewInit {
  @ViewChild('addEvent', {static: false}) private addEvent: any;
  @ViewChild('calendar') calendarComponent!: FullCalendarComponent;
  faEye = faEye;
  faPenToSquare = faPenToSquare;
  faUserCircle = faUserCircle;
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
        html: '<span style="font-size: 18px">'+ event.timeText +'</span>' +
          '<span class="text-white removeBtn" id="event-'+ event.event.id + '" style="float: right; margin-right: 2px;cursor: pointer;font-size: 18px">X</span>' +
          '<div style="clear: both"></div>'
      };
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

  ngAfterViewInit() {
    let elements = document.getElementsByClassName('removeBtn');
    for (let i = 0; i < elements.length; i++) {
      elements[i].addEventListener('click', this.removeEvent.bind(this, elements[i]));
    }
    console.log(elements);
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
    this.addScheduleToCalendar(schedule, 10);
    modal.close();
  }

  convertNgbDateTimeToDate(date: NgbDate, time: NgbTime): Date {
    return new Date(Date.UTC(date.year, date.month - 1, date.day, time.hour, time.minute, time.second));
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

  removeEvent(event: any) {
    let id = event.id.split('-')[1];
    this.calendarComponent.getApi().getEventById(id)?.remove();
  }
}
