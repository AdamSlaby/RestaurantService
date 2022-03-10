import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PageNotFoundComponent} from "./user-common/page-not-found/page-not-found.component";
import {MainPageComponent} from "./user-common/main-page/main-page.component";

const routes: Routes = [
  {
    path: "",
    component: MainPageComponent,
    pathMatch: 'full'
  },
  {
    path: "**",
    component: PageNotFoundComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
