import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { DashboardComponent } from './view/dashboard/dashboard.component';
import { ManageCustomerComponent } from './view/manage-customer/manage-customer.component';
import { ManageItemComponent } from './view/manage-item/manage-item.component';
import {Route, RouterModule, Routes} from "@angular/router";

const routes:Routes =
[
  {
    path:'customers',
    component:ManageCustomerComponent
  },
  {

  }
]
@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    ManageCustomerComponent,
    ManageItemComponent,
  ],
  imports: [
    RouterModule,
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
