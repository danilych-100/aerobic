import { Injectable } from '@angular/core';

import {Car} from '../models/Car/car';
import {CarModel} from '../models/Car/carModel';
import {Links} from '../models/Car/links';
import {Make} from '../models/Car/make';
import {Pagination} from '../models/Car/pagination';

/**
 * Class stores parsing functions that converts data from the server to classes client.
 * Can store in the class itself for example Car or as it is better called?
 */
@Injectable({
  providedIn: 'root',
})
export class MapperService {

  constructor() { }
  /**
   * Parse pagination of api.
   *
   * @param paginationFromApi Pagination sent from API.
   * @return Data converted to a class with pagination from the pagination from API.
   */
  parseToPagination(paginationFromApi: Object): Pagination {
    const pagination = new Pagination();
    pagination.total = paginationFromApi['total'];
    pagination.count = paginationFromApi['count'];
    pagination.perPage = paginationFromApi['per_page'];
    pagination.currentPage = paginationFromApi['current_page'] - 1;
    pagination.totalPages = paginationFromApi['total_pages'] - 1;
    pagination.links = new Links(paginationFromApi['links'].previous, paginationFromApi['links'].next);
    return pagination;
  }
  /**
   * Parse car of api.
   *
   * @param carFromApi car sent from API.
   * @return Data converted to a class with car from the car from API.
   */
  parseToCar(carFromApi: Object[]): Car {
    const car = new Car();
    car.id = carFromApi['id'];
    car.make = new Make({id:carFromApi['make'].id, name: carFromApi['make'].name});
    car.carModel = new CarModel(carFromApi['car_model'].id, carFromApi['car_model'].name, carFromApi['car_model'].make_id);
    car.bodyType = new Make({id: carFromApi['body_type'].id, name :carFromApi['body_type'].name});
    car.mileage = carFromApi['mileage'];
    car.description = carFromApi['description'];
    car.year = carFromApi['year'];
    car.created = carFromApi['created_at'];
    car.updated = carFromApi['updated_at'];

    return car;
  }
}
