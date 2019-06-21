import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/internal/Observable';
import {Item} from '../dto/item';

@Injectable({
  providedIn: 'root'
})

export class ItemService {

  constructor(private http: HttpClient) {
  }

  getAllItems(): Observable<Item[]> {
    return this.http.get<Item[]>('http://localhost:8080/pos/items');
  }
}
