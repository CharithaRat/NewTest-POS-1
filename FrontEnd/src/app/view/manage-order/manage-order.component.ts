import { Component, OnInit } from '@angular/core';
import {Order} from '../../dto/order';
import {OrderService} from '../../service/order.service';
import {Customer} from "../../dto/customer";
import {Item} from "../../dto/item";
import {CustomerService} from "../../service/customer.service";
import {ItemService} from "../../service/item.service";

@Component({
  selector: 'app-manage-order',
  templateUrl: './manage-order.component.html',
  styleUrls: ['./manage-order.component.css']
})
export class ManageOrderComponent implements OnInit {
orders: Order [] = [];
customers: Customer [] = [];
items: Item [] = [];
  constructor(private orderService: OrderService, private customerService: CustomerService, private itemService: ItemService) { }

  ngOnInit() {
    this.orderService.getAllOrders().subscribe(orders => {
      this.orders = orders;
    });

    this.customerService.getAllCustomers().subscribe(customers => {
      this.customers = customers;
    });

    this.itemService.getAllItems().subscribe(items => {
      this.items = items;
    });
  }

}
