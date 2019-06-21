import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Customer} from '../../dto/customer';
import {CustomerService} from '../../service/customer.service';
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-manage-customer',
  templateUrl: './manage-customer.component.html',
  styleUrls: ['./manage-customer.component.css']
})
export class ManageCustomerComponent implements OnInit {
  customers: Customer[] = [];
  sCustomer: Customer = new Customer('', '', '');
  cid: string;
  selectedCustomer: Customer = new Customer('', '', '');
  txtId: ElementRef;
  frmCustomers: NgForm;

  // @ViewChild('txtd') txtId: ElementRef;
  // @ViewChild ('frmCustomer') frmCustomers: NgForm;


  constructor(private customerService: CustomerService) {
  }

  ngOnInit() {
    this.customerService.getAllCustomers().subscribe(customers => {
      this.customers = customers;
    });
  }

  newCustomer(): void {
    this.txtId.nativeElement.focus();
  }

  tableRow_Click(customer: Customer) {
    this.selectedCustomer = Object.assign({}, customer);
  }

  getID(id: HTMLInputElement) {
    // console.log(id.value);
    this.cid = id.value;
    this.customerService.getSelectedCustomer(this.cid).subscribe(customer => {
      this.sCustomer = customer;
    });
  }

  saveCustomer(): void {
    if (!this.frmCustomers.invalid) {

      this.customerService.saveCustomer(this.selectedCustomer)
        .subscribe(resp => {
          if (resp) {
            alert('Customer has been saved successfully');
            this.customers.push(this.selectedCustomer);
          } else {
            alert('Failed to save the customer');
          }
        });

    } else {
      alert('Invalid Data, Please Correct...!');
    }
  }
  deleteCustomer(): void {
    if (confirm('Are you sure you want to delete this thing from the database?')) {
      // Delete it!
      this.customerService.deleteCustomer(this.selectedCustomer.id).subscribe(resp => {
        if (resp) {
          alert('Customer was deleted');
          this.customers.splice(this.customers.indexOf(this.selectedCustomer), 1);
        //  this will not work
        //  after delete execution => goes options request & it has 204 response status
        } else {
          alert('Failed to delete');
        //  Why this appears after correct deletion
        }
      });
    } else {
      // Do nothing!
    }
  }

}
