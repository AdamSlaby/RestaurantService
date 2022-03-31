import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PageNotFoundComponent} from "./common/page-not-found/page-not-found.component";
import {MainPageComponent} from "./user-common/main-page/main-page.component";
import {NewsComponent} from "./user-common/news/news.component";
import {NewsInfoComponent} from "./user-common/news-info/news-info.component";
import {NewsFeedComponent} from "./user-common/news-feed/news-feed.component";
import {MenuComponent} from "./user-common/menu/menu.component";
import {ContactComponent} from "./user-common/contact/contact.component";
import {ReservationComponent} from "./user-common/reservation/reservation.component";
import {DateReservationComponent} from "./user-common/date-reservation/date-reservation.component";
import {CustomerReservationComponent} from "./user-common/customer-reservation/customer-reservation.component";
import {ReservationGuard} from "./guard/reservation.guard";
import {OrderComponent} from "./user-common/order/order.component";
import {OrderFoodComponent} from "./user-common/order-food/order-food.component";
import {OrderFormComponent} from "./user-common/order-form/order-form.component";

const routes: Routes = [
  {
    path: "",
    component: MainPageComponent,
    pathMatch: 'full'
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
        component: NewsFeedComponent
      }
    ]
  },
  {
    path: "**",
    component: PageNotFoundComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {scrollPositionRestoration: 'enabled', anchorScrolling: 'enabled'})],
  exports: [RouterModule]
})
export class AppRoutingModule {}
