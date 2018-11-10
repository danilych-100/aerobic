import { DatePipe } from '@angular/common';
import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { BodyType } from '../models/Car/body-type';
import { Car } from '../models/Car/car';
import { CarModel } from '../models/Car/car-model';
import { Make } from '../models/Car/make';
import { Pagination } from '../models/Car/pagination';
import { ParamsTableActions } from '../models/Car/params-table-actions';

import { ICarDto, ICarPostDto } from './dto-cars';
import { IPaginationApiDto } from './pagination-api-dto';

/**
 * Stores a parsing function associated with reports that converts data from the server or from the client.
 */
@Injectable({
  providedIn: 'root',
})
export class MapperCarsService {

  /**
   * Parse pagination of api.
   *
   * @param paginationFromApi Pagination sent from API.
   * @return Data converted to a class with pagination from the pagination from API and makes indexing pages from zero.
   */
  public parseToPagination(paginationFromApi: IPaginationApiDto): Pagination {
    const pagination = new Pagination();
    pagination.total = paginationFromApi.total;
    pagination.count = paginationFromApi.count;
    pagination.perPage = paginationFromApi.per_page;
    pagination.currentPage = paginationFromApi.current_page - 1;
    pagination.totalPages = paginationFromApi.total_pages - 1;
    return pagination;
  }

  /**
   * Parse to car from ICarDto received from api.
   *
   * @param carFromApi car sent from API.
   * @return Data converted to a class  Car.
   */
  public parseToCar(carFromApi: ICarDto): Car {
    const car = new Car();
    car.id = carFromApi.id;
    car.make = new Make(carFromApi.make);
    car.model = new CarModel(carFromApi.car_model.id, carFromApi.car_model.name, carFromApi.car_model.make_id);
    car.bodyType = new BodyType(carFromApi.body_type.id, carFromApi.body_type.name);
    car.mileage = carFromApi.mileage;
    car.description = carFromApi.description;
    car.year = carFromApi.year;
    car.created = new Date(carFromApi.created_at);
    car.updated = new Date(carFromApi.updated_at);
    return car;
  }

  /**
   * Parse to ICarDto from car.
   *
   * @param car derived from the code.
   * @return car that we can send to the server.
   */
  public parseCartoCarDto(car: Car): ICarPostDto {
    return {
      make_id: car.make.id,
      body_type_id: car.bodyType.id,
      car_model_id: car.model.id,
      mileage: car.mileage,
      id: car.id,
      description: car.description,
      year: car.year,
      created_at: new DatePipe('en-US').transform(car.created, 'yyyy-mm-dd hh:mm:ss'),
      updated_at: new DatePipe('en-US').transform(car.updated, 'yyyy-mm-dd hh:mm:ss'),
    };
  }

  /**
   * Parse SortParams "order By" in the car field on the server.
   *
   * @param params value  ParamsActionTable orderBy.
   * @return params = Value params order_by which you need to send to the server.
   */
  public parseSortParamsToDtoParams(params: string): string {
    const dictValidSortParams: { [params: string]: string } = {
      'id': 'id',
      'make': 'make_id',
      'carModel': 'car_model_id',
      'bodyType': 'body_type_id',
      'mileage': 'mileage',
      'description': 'description',
      'year': 'year',
      'created': 'created_at',
      'updated': 'updated_at',
    };
    return dictValidSortParams[params];
  }

  /**
   * Parse SortParams "order By" in the car field on the server.
   *
   * @param paramsTableActions  ParamsActionTable which need to be changed to valid for the server HttpParams.
   * @return httpParams = valid for the server HttpParams(page,keyword,order_by,sort_order).
   */
  public parseParamsActionTableToHttpParamsDto(paramsTableActions: ParamsTableActions): HttpParams {
    let httpParams: HttpParams = new HttpParams();
    if (paramsTableActions) {
      if (paramsTableActions.page) {

        httpParams = httpParams.set('page', (paramsTableActions.page).toString());
      }
      if (paramsTableActions.keyword) {
        httpParams = httpParams.set('keyword', paramsTableActions.keyword);
      }
      if (paramsTableActions.sortParams.sortOrder) {
        httpParams = httpParams.set('order_by', this.parseSortParamsToDtoParams(paramsTableActions.sortParams.orderBy));
        httpParams = httpParams.set('sort_order', paramsTableActions.sortParams.sortOrder);
      }
    }
    return httpParams;
  }

}
