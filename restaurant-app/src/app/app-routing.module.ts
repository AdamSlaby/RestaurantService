import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PageNotFoundComponent} from "./common/page-not-found/page-not-found.component";
import {MainPageComponent} from "./user-pages/main-page/main-page.component";
import {NewsComponent} from "./user-pages/news-page/news/news.component";
import {NewsInfoComponent} from "./user-pages/news-page/news-info/news-info.component";
import {NewsFeedComponent} from "./user-pages/news-page/news-feed/news-feed.component";
import {MenuComponent} from "./user-pages/menu-page/menu/menu.component";
import {ContactComponent} from "./user-pages/contact/contact.component";
import {ReservationComponent} from "./user-pages/reservation-page/reservation/reservation.component";
import {DateReservationComponent} from "./user-pages/reservation-page/date-reservation/date-reservation.component";
import {CustomerReservationComponent} from "./user-pages/reservation-page/customer-reservation/customer-reservation.component";
import {ReservationGuard} from "./guard/reservation.guard";
import {OrderComponent} from "./user-pages/order-page/order/order.component";
import {OrderFoodComponent} from "./user-pages/order-page/order-food/order-food.component";
import {OrderFormComponent} from "./user-pages/order-page/order-form/order-form.component";
import {RestaurantPageComponent} from "./user-pages/restaurant-page/restaurant-page.component";
import {LoginPageComponent} from "./worker-pages/login-page/login-page.component";
import {DashboardComponent} from "./worker-pages/dashboard/dashboard.component";
import {MainNavComponent} from "./worker-pages/main-nav/main-nav.component";

const routes: Routes = [
  {
    path: "",
    component: RestaurantPageComponent,
    pathMatch: 'full'
  },
  {
    path: "main-site",
    component: MainPageComponent,
  },
  {
    path: "menu",
    component: MenuComponent,
  },
  {
    path: "contact",
    component: ContactComponent,
  },
  {
    path: "order",
    component: OrderComponent,
    children: [
      {
        path: '',
        component: OrderFoodComponent,
      },
      {
        path: 'customer',
        component: OrderFormComponent,
      }
    ],
  },
  {
    path: "reservation",
    component: ReservationComponent,
    children: [
      {
        path: '',
        component: DateReservationComponent,
      },
      {
        path: 'customer',
        component: CustomerReservationComponent,
        canActivate: [ReservationGuard],
      },
    ],
  },
  {
    path: "news",
    component: NewsComponent,
    children: [
      {
        path: ":id",
        component: NewsInfoComponent,
      },
      {
        path: '',
        component: NewsFeedComponent,
      }
    ]
  },
  {
    path: "login",
    component: LoginPageComponent,
  },
  {
    path: "admin",
    component: MainNavComponent,
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent,
      }
    ]
  },
  {
    path: "**",
    component: PageNotFoundComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {scrollPositionRestoration: 'enabled', anchorScrolling: 'enabled'})],
  exports: [RouterModule]
})
export class AppRoutingModule {}
