import { Component, OnInit } from '@angular/core';
import {Item} from "../../dto/item";

@Component({
  selector: 'app-manage-item',
  templateUrl: './manage-item.component.html',
  styleUrls: ['./manage-item.component.css']
})
export class ManageItemComponent implements OnInit {
items:Item[]=[];

  constructor() { }

  ngOnInit() {
  }

}
