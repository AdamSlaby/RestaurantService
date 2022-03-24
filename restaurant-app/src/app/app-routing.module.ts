import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PageNotFoundComponent} from "./user-common/page-not-found/page-not-found.component";
import {MainPageComponent} from "./user-common/main-page/main-page.component";
import {NewsComponent} from "./user-common/news/news.component";
import {NewsInfoComponent} from "./user-common/news-info/news-info.component";
import {NewsFeedComponent} from "./user-common/news-feed/news-feed.component";
import {MenuComponent} from "./user-common/menu/menu.component";
import {ContactComponent} from "./user-common/contact/contact.component";
import {ReservationComponent} from "./user-common/reservation/reservation.component";

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
    path: "reservation",
    component: ReservationComponent,
  },
  {
    path: "news",
    component: NewsComponent,
    children: [
      {
        path: "info/:id",
        component: NewsInfoComponent,
      },
      {
        path: 'feed',
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
