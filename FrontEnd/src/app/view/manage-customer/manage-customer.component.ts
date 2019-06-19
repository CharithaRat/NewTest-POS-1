import { Component, OnInit } from '@angular/core';
import {Customer} from "../../dto/customer";
import {CustomerService} from "../../service/customer.service";

@Component({
  selector: 'app-manage-customer',
  templateUrl: './manage-customer.component.html',
  styleUrls: ['./manage-customer.component.css']
})
export class ManageCustomerComponent implements OnInit {
customers:Customer[]=[];

  constructor(private customerService:CustomerService) {
  }

  ngOnInit() {
    this.customerService.getAllCustomers().subscribe(customers =>{
      this.customers = customers;
    });
  }

}
