import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './user-common/header/header.component';
import { FooterComponent } from './user-common/footer/footer.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {CommonModule} from "@angular/common";
import { PageNotFoundComponent } from './user-common/page-not-found/page-not-found.component';
import { MainPageComponent } from './user-common/main-page/main-page.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    PageNotFoundComponent,
    MainPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    CommonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
