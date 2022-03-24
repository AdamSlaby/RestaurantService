import {Component, OnInit} from '@angular/core';
import {OpeningHour} from "../../model/opening-hour";
import {FormBuilder, Validators} from "@angular/forms";
import {faPhone, faEnvelope, faLocationDot, faCity} from "@fortawesome/free-solid-svg-icons";
import {MailInfo} from "../../model/mail-info";

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent implements OnInit {
  contactImg: string = 'assets/contact_image.jpg';
  faPhone = faPhone
  faEnvelope = faEnvelope;
  faLocationDot = faLocationDot;
  faCity = faCity;
  loading = false;
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
    firstName: ['', [Validators.required, Validators.pattern("^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32}$")]],
    email: ['', [Validators.required, Validators.email]],
    phoneNumber: ['', [Validators.required, Validators.pattern("^(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)$")]],
    subject: ['', [Validators.required, Validators.maxLength(100)]],
    content: ['', [Validators.required, Validators.maxLength(1000)]],
  });
  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
  }

  get f() {
    return this.contactForm.controls;
  }

  onSubmit() {
    this.errors.clear();
    this.loading=true;
    let mailInfo: MailInfo = {
      name: this.contactForm.get('firstName')?.value,
      email: this.contactForm.get('email')?.value,
      phoneNumber: this.contactForm.get('phoneNumber')?.value,
      subject: this.contactForm.get('subject')?.value,
      content: this.contactForm.get('content')?.value,
    }
    console.log(mailInfo);
  }
}
