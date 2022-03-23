import { Component, OnInit } from '@angular/core';
import {OpeningHour} from "../model/opening-hour";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent implements OnInit {
  contactImg: string = 'assets/contact_image.jpg';
  submitted = false;
  errors: Map<string, string> = new Map<string, string>();

  openingHours: OpeningHour[] = [
    {
      weekDayName: "poniedziałek",
      hours: "10:00 - 18:00"
    },
    {
      weekDayName: "wtorek",
      hours: "10:00 - 18:00"
    },
    {
      weekDayName: "środa",
      hours: "10:00 - 18:00"
    },
    {
      weekDayName: "czwartek",
      hours: "10:00 - 18:00"
    },
    {
      weekDayName: "piątek",
      hours: "10:00 - 18:00"
    },
    {
      weekDayName: "sobota",
      hours: "10:00 - 18:00"
    },
    {
      weekDayName: "niedziela",
      hours: "10:00 - 18:00"
    },
  ];

  contactForm = this.fb.group({
    firstName: ['', Validators.required],

  });
  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
  }

  get f() {
    return this.contactForm.controls;
  }

  onSubmit() {

  }
}
