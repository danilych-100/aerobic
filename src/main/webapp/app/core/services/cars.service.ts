import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';

import {map} from 'rxjs/operators';
import {Observable} from 'rxjs/Rx';

import {Car} from '../models/Car/car';
import {ParamsTableActions} from '../models/Car/params-table-actions';
import {TableCars} from '../models/Car/table-cars';

import {ICarDto} from './dto-cars';
import {MapperCarsService} from './mapper-cars.service';
import {Client} from '../models/client';
import {Pagination} from '../models/Car/pagination';

/**
 * Service communication with http server https://backend-jscamp.saritasa-hosting.com/api/cars.
 */
@Injectable({
  providedIn: 'root',
})
export class CarsService {

  /**
   * Injection HttpClient and MapperCarsService.
   *
   * @param httpClient standard Angular.
   * @param mapper Stores methods for conversion from and to DTO.
   */
  constructor(private httpClient: HttpClient, private mapper: MapperCarsService) {

  }

  /**
   * Create http get query with Http params equal to the parameters of the function.
   *
   * @param paramsTableActions -(pagination,sort,filter) cast to Http params.
   * @return Observable with ICarsApi(cars and pagination).
   */
  public getCars(paramsTableActions: ParamsTableActions): Observable<TableCars<Client>> {
    const clients = [
      new Client({
        id: 10000,
        time: new Date(),
        doctor: 'Машкова М.Д',
        name: 'Иванова Л.Б',
        birthDay: new Date(),
        address: 'Ленина 16',
        police: 11111111111111,
        numberTicket: 'dfsas',
        cost: 1000,
      }),
      new Client({
        id: 10000,
        time: new Date(),
        doctor: 'Машкова М.Д',
        name: 'Иванова Л.Б',
        birthDay: new Date(),
        address: 'Ленина 16',
        police: 11111111111111,
        numberTicket: 'dfsas',
        cost: 1000,
      }),
      new Client({
        id: 10000,
        time: new Date(),
        doctor: 'Машкова М.Д',
        name: 'Иванова Л.Б',
        birthDay: new Date(),
        address: 'Ленина 16',
        police: 11111111111111,
        numberTicket: 'dfsas',
        cost: 1000,
      }),
      new Client({
        id: 10000,
        time: new Date(),
        doctor: 'Машкова М.Д',
        name: 'Иванова Л.Б',
        birthDay: new Date(),
        address: 'Ленина 16',
        police: 11111111111111,
        numberTicket: 'dfsas',
        cost: 1000,
      }),
      new Client({
        id: 10000,
        time: new Date(),
        doctor: 'Машкова М.Д',
        name: 'Иванова Л.Б',
        birthDay: new Date(),
        address: 'Ленина 16',
        police: 11111111111111,
        numberTicket: 'dfsas',
        cost: 1000,
      }),
      new Client({
        id: 10000,
        time: new Date(),
        doctor: 'Машкова М.Д',
        name: 'Иванова Л.Б',
        birthDay: new Date(),
        address: 'Ленина 16',
        police: 11111111111111,
        numberTicket: 'dfsas',
        cost: 1000,
      }),
    ];
    const pag = {
      total: clients.length,
      count: 10,
      perPage: 10,
      currentPage: 1,
      totalPages: 1,
    } as Pagination;
    return Observable.of(
      {
        items: clients,
        pagination: pag,
      },
    );
    // return this.httpClient.get<ICarDto[]>('https://backend-jscamp.saritasa-hosting.com/api/cars',
    //   { params: this.mapper.parseParamsActionTableToHttpParamsDto(paramsTableActions) }).pipe(
    //   map((data: any) => {
    //     return <TableCars<Car>>{
    //       items: data.results.map(car => this.mapper.parseToCar(car)),
    //       pagination: this.mapper.parseToPagination(data.pagination),
    //     };
    //   }));
  }

//   /**
//    * Create http get query with  id car.
//    *
//    * @return Observable with Car with this id.
//    */
//   public getCar(id: number): Observable<Car> {
//     return this.httpClient.get<ICarDto>(`https://backend-jscamp.saritasa-hosting.com/api/cars/${id}`).pipe(
//       map((carDto: ICarDto) => {
//         return this.mapper.parseToCar(carDto);
//       }));
//   }
//
//   /**
//    * Create http  send query with  body = car.
//    *
//    * @param car You want to send to API.
//    * @return Observable with Car with this id.
//    */
//   public saveCar(client: Client): Observable<Car> {
//     let method = 'post';
//     let url = 'https://backend-jscamp.saritasa-hosting.com/api/cars';
//     if (localStorage.getItem('token')) {
//       url = 'https://backend-jscamp.saritasa-hosting.com/api/with-auth/cars';
//     }
//     if (car.id) {
//       method = 'put';
//       url = url + `/${car.id}`;
//     }
//     return this.httpClient.request(method, url, {body: this.mapper.parseCartoCarDto(car)}).pipe(
//       map((carDto: ICarDto) => {
//         return this.mapper.parseToCar(carDto);
//       }),
//     );
//   }
// }
}
