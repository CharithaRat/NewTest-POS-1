import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { DashboardComponent } from './view/dashboard/dashboard.component';
import { ManageCustomerComponent } from './view/manage-customer/manage-customer.component';
import { ManageItemComponent } from './view/manage-item/manage-item.component';
import {Route, RouterModule, Routes} from "@angular/router";
import { ManageOrderComponent } from './view/manage-order/manage-order.component';

const routes:Routes =
[
  {
    path:'customers',
    component:ManageCustomerComponent
  },
  {
    path:'items',
    component:ManageItemComponent
  },
  {
    path:'dashboard',
    component:DashboardComponent
  },
  {
    path:'orders',
    component:ManageOrderComponent
  },
  {
    path:'',
    pathMatch:'full',
    redirectTo:'dashboard'
  }
]
@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    ManageCustomerComponent,
    ManageItemComponent,
    ManageOrderComponent,
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
