import {Component, OnInit, ViewChild} from '@angular/core';
import {faEye, faPenToSquare, faUserCircle} from "@fortawesome/free-solid-svg-icons";
import {CalendarOptions} from "@fullcalendar/angular";
import plLocale from "@fullcalendar/core/locales/pl";
import {Schedule} from "../../model/schedule";
import {EmployeeInfo} from "../../model/employee-info";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'app-employee-info',
  templateUrl: './employee-info.component.html',
  styleUrls: ['./employee-info.component.scss']
})
export class EmployeeInfoComponent implements OnInit {
  @ViewChild('addEvent', { static: false }) private addEvent: any;
  faEye = faEye;
  faPenToSquare = faPenToSquare;
  faUserCircle = faUserCircle;
  calendarOptions: CalendarOptions = {
    locale: plLocale,
    initialView: 'timeGridWeek',
    slotMinTime: '07:00:00',
    slotMaxTime: '23:00:00',
    allDaySlot: false,
    events: [],
    customButtons: {
      addEventButton: {
        text: 'Dodaj zdarzenie',
        click: () => {this.open(this.addEvent)},
      }
    },
    headerToolbar: {
      center: 'addEventButton',
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

  constructor(private modalService: NgbModal, private fb: FormBuilder) { }

  ngOnInit(): void {
    let events = this.calendarOptions.events as Array<Object>
    for (let schedule of this.employeeInfo.scheduleInfo) {
      events.push({
        start: schedule.startShift,
        end: schedule.endShift,
      });
    }
  }

  get f() {
    return this.addScheduleForm.controls;
  }

  open(content: any) {
    this.modalService.open(content, {}).result.then((result) => {});
  }

  onSubmit(modal: any) {
    //todo
    console.log(this.addScheduleForm.get('startShiftDate')?.value);
    console.log(this.addScheduleForm.get('startShiftTime')?.value);
    console.log(this.addScheduleForm.get('endShiftTime')?.value);
    modal.close();
  }

  dismiss(modal: any) {
    //todo make it works
    modal.promise.then().catch((res: any) => {

    });
  }
}
