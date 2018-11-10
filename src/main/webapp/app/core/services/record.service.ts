import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {MapperCarsService} from './mapper-cars.service';
import {Record} from '../models/record';

@Injectable({
  providedIn: 'root',
})
export class RecordService {
  /**
   * Injection HttpClient and MapperCarsService.
   *
   * @param httpClient standard Angular.
   */
  constructor(private httpClient: HttpClient) {

  }

  public saveRecord(record: Record) {

  }
}
