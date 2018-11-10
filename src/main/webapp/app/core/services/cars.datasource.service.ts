import { CollectionViewer } from '@angular/cdk/collections';
import { DataSource } from '@angular/cdk/table';
import { Injectable } from '@angular/core';
import { catchError, finalize, map, retryWhen, switchMap, takeUntil, tap } from 'rxjs/operators';
import { BehaviorSubject, Observable } from 'rxjs/Rx';
import { Subject } from 'rxjs/Subject';

import { Car } from '../models/Car/car';
import { Pagination } from '../models/Car/pagination';
import { ParamsTableActions } from '../models/Car/params-table-actions';

import { CarsService } from './cars.service';
import {Client} from '../models/client';

/**
 * Class custom Observable-based Angular CDK Data Source.
 * Will not be using the built-in MatTableDataSource because it is use of a client-side data array.
 */
@Injectable({
  providedIn: 'root',
})
export class CarsDatasourceService implements DataSource<Client> {
  private paginationSubject = new BehaviorSubject<Pagination>(null);
  private errorSubject = new BehaviorSubject<Pagination>(null);
  private loadingSubject = new BehaviorSubject<boolean>(true);
  private $ngUnsubscribe: Subject<void> = new Subject<void>();
  /**
   * Observable emit at the table loading.
   */
  public loading$ = this.loadingSubject.asObservable();
  /**
   * provides pagination of the table.
   */
  public pagination$ = this.paginationSubject.asObservable();
  /**
   * provides error on the table.
   */
  public error$ = this.errorSubject.asObservable();

  /**
   * In the constructor service which receives the data in the table requests data occurs after actionsChangeTable emits values.
   *
   * @param carsService Service that sends data to the table in our case CarsService.
   * @param $actionsChangeTable ParamsTableActions come whenever one of three events occurs(change Page, sort change , search).
   */
  constructor(private carsService: CarsService, private $actionsChangeTable: Subject<ParamsTableActions>) {
  }

  /**
   * This method will be called once by the Data Table at table bootstrap time.
   * The Data Table expects this method to return an Observable,
   * and the values of that observable contain the data that the Data Table needs to display.
   *
   * @param collectionViewer provides an Observable that emits information about what data is being displayed
   * @return observable carsService.getCars which is called when $actionsChangeTable emit events,
   * Is going to be emitting the values in the table.
   */
  public connect(collectionViewer: CollectionViewer): Observable<Client[]> {
    return this.$actionsChangeTable.pipe(
      tap(() => this.loadingSubject.next(true)),
      switchMap(value => {
        return this.carsService.getCars(value).pipe(
          tap(tableCar => this.paginationSubject.next(tableCar.pagination)),
          map(tableCar => tableCar.items),
          catchError(error => {
            this.errorSubject.next(error);
            return Observable.of([]);
          }),
          finalize(() => this.loadingSubject.next(false)),
        );
      }),
      /**
       * Fixed an error with takeUntil Leaks when takeUntil to be before switchMap combineLatest.
       */
      takeUntil(this.$ngUnsubscribe),
    );
  }

  /**
   * Complete any observables that we have created internally in this class.
   * This method is called once by the data table at component destruction time.
   */
  public disconnect(collectionViewer: CollectionViewer): void {
    this.loadingSubject.complete();
    this.paginationSubject.complete();
    this.$ngUnsubscribe.next();
    this.$ngUnsubscribe.complete();
  }
}
