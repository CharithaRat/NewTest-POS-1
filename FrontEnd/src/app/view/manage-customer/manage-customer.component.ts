import { Component, OnInit } from '@angular/core';
import {Customer} from "../../dto/customer";

@Component({
  selector: 'app-manage-customer',
  templateUrl: './manage-customer.component.html',
  styleUrls: ['./manage-customer.component.css']
})
export class ManageCustomerComponent implements OnInit {
customers:Customer[]=[];

  constructor() { }

  ngOnInit() {
  }

}
