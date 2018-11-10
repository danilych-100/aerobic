import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { catchError, finalize, map, retryWhen, switchMap, takeUntil } from 'rxjs/operators';
import {Observable} from 'rxjs/Rx';

import { Car } from '../models/Car/car';

import { CarsService } from './cars.service';

/**
 * Resolve does not support observable error state.
 * Can pass the error through the interface or make a component(error) and redirect there.
 */
interface ResolvedData {
  car?: Car;
  error?: any;
}

/**
 * Resolve components that need a car.
 */
@Injectable({
  providedIn: 'root',
})
export class CarResolver implements Resolve<Observable<ResolvedData>> {
  /**
   * Race condition Switch map don`t work because Observable should be completed.
   */
  private activatedRouteParams$: BehaviorSubject<number>;
  /**
   * @param carService from where do we get the car.
   */
  constructor(private carService: CarsService) {
  }

  /**
   * method is called when go to the link.
   * There is a race condition in that snapshot.
   *
   * @param route from where do we get the car.
   * @return Observable with a car or with an error why could not get it.
   */
  public resolve(route: ActivatedRouteSnapshot): Observable<ResolvedData> {

    return Observable.of(null);
    // if (route.queryParams.id) {
    //   return this.carService.getCar(route.queryParams.id).pipe(
    //     map(car => {
    //       return { car: car };
    //     }),
    //     catchError(err => Observable.of({ error: err })),
    //   );
    // }
    // return Observable.of({ car: new Car() });
  }
}
