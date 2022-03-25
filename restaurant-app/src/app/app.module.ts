import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './common/header/header.component';
import { FooterComponent } from './common/footer/footer.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {CommonModule} from "@angular/common";
import { PageNotFoundComponent } from './common/page-not-found/page-not-found.component';
import { MainPageComponent } from './user-common/main-page/main-page.component';
import { NewsComponent } from './user-common/news/news.component';
import { NewsInfoComponent } from './user-common/news-info/news-info.component';
import { NewsFeedComponent } from './user-common/news-feed/news-feed.component';
import { MenuComponent } from './user-common/menu/menu.component';
import { DishListComponent } from './user-common/dish-list/dish-list.component';
import { ContactComponent } from './user-common/contact/contact.component';
import { TopicImageComponent } from './user-common/topic-image/topic-image.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ReservationComponent } from './user-common/reservation/reservation.component';
import '@angular/common/locales/global/pl';
import { LOCALE_ID } from '@angular/core';
import { DateReservationComponent } from './user-common/date-reservation/date-reservation.component';
import { CustomerReservationComponent } from './user-common/customer-reservation/customer-reservation.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    PageNotFoundComponent,
    MainPageComponent,
    NewsComponent,
    NewsInfoComponent,
    NewsFeedComponent,
    MenuComponent,
    DishListComponent,
    ContactComponent,
    TopicImageComponent,
    ReservationComponent,
    DateReservationComponent,
    CustomerReservationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    CommonModule,
    ReactiveFormsModule,
    FontAwesomeModule,
    FormsModule
  ],
  providers: [{ provide: LOCALE_ID, useValue: "pl-PL"}],
  bootstrap: [AppComponent]
})
export class AppModule { }
