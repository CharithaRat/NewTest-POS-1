import { Component, OnInit } from '@angular/core';
import {Item} from "../../dto/item";
import {ItemService} from "../../service/item.service";

@Component({
  selector: 'app-manage-item',
  templateUrl: './manage-item.component.html',
  styleUrls: ['./manage-item.component.css']
})

export class ManageItemComponent implements OnInit {
items:Item[]=[];

  constructor(private itemService:ItemService) {
  }

  ngOnInit() {
    this.itemService.getAllItems().subscribe(items=>{
      this.items = items;
    });
  }

}
