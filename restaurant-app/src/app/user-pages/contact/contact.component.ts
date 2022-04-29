import {Component, OnInit} from '@angular/core';
import {OpeningHour} from "../../model/opening-hour";
import {FormBuilder, Validators} from "@angular/forms";
import {faPhone, faEnvelope, faLocationDot, faCity} from "@fortawesome/free-solid-svg-icons";
import {MailInfo} from "../../model/mail-info";
import {RestaurantInfo} from "../../model/restaurant/restaurant-info";
import {RegexPattern} from "../../model/regex-pattern";

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
  restaurantInfo: RestaurantInfo = {
    email: 'restaurant24@gmail.com',
    phoneNr: '+48600200300',
    address: {
      city: 'Kielce',
      street: 'Niewiadomskiego',
      houseNr: '102',
      flatNr: '',
      postcode: '25-000',
    }
  }
  openingHours: OpeningHour[] = [
    {
      hourId: 1,
      weekDayNr: 1,
      fromHour: new Date('04 Apr 2022 10:00:00 UTC'),
      toHour: new Date('04 Apr 2022 18:00:00 UTC'),
    },
    {
      hourId: 2,
      weekDayNr: 2,
      fromHour: new Date('05 Apr 2022 10:00:00 UTC'),
      toHour: new Date('05 Apr 2022 18:00:00 UTC'),
    },
    {
      hourId: 3,
      weekDayNr: 3,
      fromHour: new Date('06 Apr 2022 10:00:00 UTC'),
      toHour: new Date('06 Apr 2022 18:00:00 UTC'),
    },
    {
      hourId: 4,
      weekDayNr: 4,
      fromHour: new Date('07 Apr 2022 10:00:00 UTC'),
      toHour: new Date('07 Apr 2022 18:00:00 UTC'),
    },
    {
      hourId: 5,
      weekDayNr: 5,
      fromHour: new Date('08 Apr 2022 10:00:00 UTC'),
      toHour: new Date('08 Apr 2022 18:00:00 UTC'),
    },
    {
      hourId: 6,
      weekDayNr: 6,
      fromHour: new Date('09 Apr 2022 10:00:00 UTC'),
      toHour: new Date('09 Apr 2022 18:00:00 UTC'),
    },
    {
      hourId: 7,
      weekDayNr: 7,
      fromHour: new Date('10 Apr 2022 10:00:00 UTC'),
      toHour: new Date('10 Apr 2022 18:00:00 UTC'),
    },
  ];

  contactForm = this.fb.group({
    firstName: ['', [Validators.required, Validators.pattern(RegexPattern.NAME)]],
    email: ['', [Validators.required, Validators.email]],
    phoneNumber: ['', [Validators.required, Validators.pattern(RegexPattern.PHONE)]],
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
