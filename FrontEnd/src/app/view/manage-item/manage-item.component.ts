import {Component, OnInit} from '@angular/core';
import {Item} from '../../dto/item';
import {ItemService} from '../../service/item.service';

@Component({
  selector: 'app-manage-item',
  templateUrl: './manage-item.component.html',
  styleUrls: ['./manage-item.component.css']
})

export class ManageItemComponent implements OnInit {
  items: Item[] = [];
  codei: string;
  sItem: Item = new Item('', '', 0, 0);
  selectItem: Item = new Item('', '', 0, 0);

  constructor(private itemService: ItemService) {
  }

  ngOnInit() {
    this.itemService.getAllItems().subscribe(items => {
      this.items = items;
    });
  }

  getCode(id: HTMLInputElement) {
    // console.log(id.value);
    this.codei = id.value;
    this.itemService.getSelectedItem(this.codei).subscribe(item => {
      this.sItem = item;
    });
  }
  deleteItem(): void {
    if (confirm('Are you sure you want to delete this thing from the database?')) {
      // Delete it!
      this.itemService.deleteItem(this.selectItem.Code).subscribe(resp => {
        if (resp) {
          alert('Customer was deleted');
          this.items.splice(this.items.indexOf(this.selectItem), 1);
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
