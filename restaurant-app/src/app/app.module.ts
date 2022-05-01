import {LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './user-pages/common/header/header.component';
import {FooterComponent} from './user-pages/common/footer/footer.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {CommonModule} from "@angular/common";
import {PageNotFoundComponent} from './user-pages/common/page-not-found/page-not-found.component';
import {MainPageComponent} from './user-pages/main-page/main-page.component';
import {NewsComponent} from './user-pages/news-page/news/news.component';
import {NewsInfoComponent} from './user-pages/news-page/news-info/news-info.component';
import {NewsFeedComponent} from './user-pages/news-page/news-feed/news-feed.component';
import {MenuComponent} from './user-pages/menu-page/menu/menu.component';
import {DishListComponent} from './user-pages/menu-page/dish-list/dish-list.component';
import {ContactComponent} from './user-pages/contact/contact.component';
import {TopicImageComponent} from './user-pages/common/topic-image/topic-image.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {ReservationComponent} from './user-pages/reservation-page/reservation/reservation.component';
import '@angular/common/locales/global/pl';
import {DateReservationComponent} from './user-pages/reservation-page/date-reservation/date-reservation.component';
import {
  CustomerReservationComponent
} from './user-pages/reservation-page/customer-reservation/customer-reservation.component';
import {OrderComponent} from './user-pages/order-page/order/order.component';
import {OrderFoodComponent} from './user-pages/order-page/order-food/order-food.component';
import {OrderListComponent} from './user-pages/order-page/order-list/order-list.component';
import {BasketComponent} from './user-pages/order-page/basket/basket.component';
import {BasketService} from "./service/basket.service";
import {OrderFormComponent} from './user-pages/order-page/order-form/order-form.component';
import {PhonePipe} from './pipe/phone.pipe';
import {DayPipe} from './pipe/day.pipe';
import {RestaurantPageComponent} from './user-pages/restaurant-page/restaurant-page.component';
import {LoginPageComponent} from './worker-pages/login-page/login-page.component';
import {MainNavComponent} from './worker-pages/main-nav/main-nav.component';
import {DashboardComponent} from "./worker-pages/dashboard/dashboard.component";
import {NgxChartsModule} from "@swimlane/ngx-charts";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { EmployeesPageComponent } from './worker-pages/employees-page/employees-page.component';
import { NgbdSortableHeaderDirective } from './directive/ngbd-sortable-header.directive';
import { BankAccountPipe } from './pipe/bank-account.pipe';
import {FullCalendarModule} from "@fullcalendar/angular";
import timeGridPlugin from '@fullcalendar/timegrid';
import { EmployeeInfoComponent } from './worker-pages/employee-info/employee-info.component';
import { NewsPageComponent } from './worker-pages/news-page/news-page.component';
import {NewsFormComponent} from "./worker-pages/news-form/news-form.component";
import { MenuPageComponent } from './worker-pages/menu-page/menu-page.component';
import { OrderPageComponent } from './worker-pages/order-page/order-page.component';
import { OrderInfoComponent } from './worker-pages/order-info/order-info.component';
import { SupplyPageComponent } from './worker-pages/supply-page/supply-page.component';
import {RestaurantComponent} from "./worker-pages/restaurant/restaurant.component";
import { StatisticsPageComponent } from './worker-pages/statistics-page/statistics-page.component';
import { MonthPipe } from './pipe/month.pipe';
import { StatisticComponent } from './worker-pages/statistic/statistic.component';
import { InvoicesComponent } from './worker-pages/invoice-page/invoices/invoices.component';
import { InvoiceFormComponent } from './worker-pages/invoice-page/invoice-form/invoice-form.component';
import { ReservationsComponent } from './worker-pages/reservations/reservations.component';
import {HttpClientModule} from "@angular/common/http";

FullCalendarModule.registerPlugins([
  timeGridPlugin,
]);

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
    CustomerReservationComponent,
    OrderComponent,
    OrderFoodComponent,
    OrderListComponent,
    BasketComponent,
    OrderFormComponent,
    PhonePipe,
    DayPipe,
    RestaurantPageComponent,
    LoginPageComponent,
    MainNavComponent,
    DashboardComponent,
    EmployeesPageComponent,
    NgbdSortableHeaderDirective,
    BankAccountPipe,
    EmployeeInfoComponent,
    NewsPageComponent,
    NewsFormComponent,
    MenuPageComponent,
    OrderPageComponent,
    OrderInfoComponent,
    SupplyPageComponent,
    RestaurantComponent,
    StatisticsPageComponent,
    MonthPipe,
    StatisticComponent,
    InvoicesComponent,
    InvoiceFormComponent,
    ReservationsComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    NgbModule,
    CommonModule,
    ReactiveFormsModule,
    FontAwesomeModule,
    FormsModule,
    NgxChartsModule,
    FullCalendarModule,
    HttpClientModule
  ],
  providers: [{ provide: LOCALE_ID, useValue: "pl-PL"}, BasketService],
  bootstrap: [AppComponent]
})
export class AppModule { }
