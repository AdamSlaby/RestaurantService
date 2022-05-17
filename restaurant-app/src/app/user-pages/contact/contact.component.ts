import {Component, OnInit} from '@angular/core';
import {OpeningHour} from "../../model/opening-hour";
import {FormBuilder, Validators} from "@angular/forms";
import {faPhone, faEnvelope, faLocationDot, faCity} from "@fortawesome/free-solid-svg-icons";
import {MailInfo} from "../../model/mail-info";
import {RestaurantInfo} from "../../model/restaurant/restaurant-info";
import {RegexPattern} from "../../model/regex-pattern";
import {RestaurantService} from "../../service/restaurant.service";
import { MailService } from 'src/app/service/mail.service';

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
  isSuccessful: boolean = false;
  errors: Map<string, string> = new Map<string, string>();
  restaurantInfo!: RestaurantInfo;

  contactForm = this.fb.group({
    firstName: ['', [Validators.required, Validators.pattern(RegexPattern.NAME)]],
    email: ['', [Validators.required, Validators.email]],
    phoneNumber: ['', [Validators.required, Validators.pattern(RegexPattern.PHONE)]],
    subject: ['', [Validators.required, Validators.maxLength(100)]],
    content: ['', [Validators.required, Validators.maxLength(1000)]],
  });
  constructor(private fb: FormBuilder, private restaurantService: RestaurantService,
              private mailService: MailService) { }

  ngOnInit(): void {
    let restaurantId = sessionStorage.getItem('restaurantId');
    this.restaurantService.getRestaurantInfo(restaurantId).subscribe(data => {
      this.restaurantInfo = data;
      this.restaurantInfo.openingHours.forEach(el => {
        el.fromHour = new Date(el.fromHour);
        el.toHour = new Date(el.toHour);
      });
    }, error => {
      console.error(error);
    })
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
    this.loading = true;
    this.mailService.sendMail(mailInfo).subscribe(data => {
      this.loading = false;
      this.isSuccessful = true;
    }, error => {
      this.errors = new Map(Object.entries(error.error));
      this.contactForm.markAsPristine();
      this.loading = false;
      this.isSuccessful = false;
      console.error(error);
    });
  }
}
