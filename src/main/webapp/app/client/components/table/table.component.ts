import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { PageEvent, Sort } from '@angular/material';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { debounceTime, distinctUntilChanged, takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs/Subject';

import { ParamsTableActions } from '../../../core/models/Car/params-table-actions';
import { CarsDatasourceService } from '../../../core/services/cars.datasource.service';
import { CarsService } from '../../../core/services/cars.service';

/**
 * Component with table car.
 */
@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss'],
})
export class TableComponent implements OnInit, OnDestroy {
  /**
   * Object class that implements CDK Data Source, stores observable pagination,error,loading table.
   */
  public dataSource: CarsDatasourceService;
  private $actionsChangeTable: BehaviorSubject<ParamsTableActions>;
  private carHttpParams: ParamsTableActions;
  private $ngUnsubscribe: Subject<void> = new Subject<void>();
  /**
   * matHeaderRowDef enumeration of column names that we want to display Table.
   */
  public displayedColumns: string[] = [
    'time',
    'doctor',
    'name',
    'birthDay',
    'address',
    'police',
    'numberTicket',
    'cost',
  ];
  /**
   * search Field to sort the table.
   */
  public searchField: FormControl;

  /**
   * The server from which data is received into the table.
   */
  constructor(private carsService: CarsService) {
  }

  /**
   * Create CarsDatasourceService which provides data to a table, who receives them at $actionsChangeTable emit change of carsService.
   */
  public ngOnInit(): void {
    this.carHttpParams = new ParamsTableActions();
    this.$actionsChangeTable = new BehaviorSubject<ParamsTableActions>(this.carHttpParams);
    this.$actionsChangeTable.pipe(takeUntil(this.$ngUnsubscribe));
    this.createSearchInput();
    this.dataSource = new CarsDatasourceService(this.carsService, this.$actionsChangeTable);
  }

  /**
   When the user clicks the sort buttons,$actionsChangeTable emit carHttpParams with  sort changes data.
   */
  public sortTableChange(eventSort: Sort): void {
    this.carHttpParams.page = 1;
    this.carHttpParams.sortParams.sortOrder = eventSort.direction;
    this.carHttpParams.sortParams.orderBy = eventSort.active;
    this.$actionsChangeTable.next(this.carHttpParams);
  }

  /**
   When the user change page table,$actionsChangeTable emit carHttpParams with  page changes data.
   */
  public changePage(eventPageIndex: PageEvent): void {
    this.carHttpParams.page = eventPageIndex.pageIndex + 1;
    this.$actionsChangeTable.next(this.carHttpParams);
  }

  /**
   When the user change search field table,$actionsChangeTable emit carHttpParams with  search changes data.
   */
  public createSearchInput(): void {
    this.searchField = new FormControl();
    this.searchField.valueChanges.pipe(
      takeUntil(this.$ngUnsubscribe),
      debounceTime(150),
      distinctUntilChanged(),
    ).subscribe(
      (value: string) => {
        this.carHttpParams.page = 1;
        this.carHttpParams.keyword = value;
        this.$actionsChangeTable.next(this.carHttpParams);
      },
    );
  }

  /**
   Unsubscribe.
   */
  public ngOnDestroy(): void {
    this.$ngUnsubscribe.next();
    this.$ngUnsubscribe.complete();
  }
}
