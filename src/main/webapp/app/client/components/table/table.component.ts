import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatPaginator, MatPaginatorIntl, MatSort, MatTableDataSource, PageEvent, Sort } from '@angular/material';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { debounceTime, distinctUntilChanged, map, startWith, takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs/Subject';

import { ParamsTableActions } from '../../../core/models/Car/params-table-actions';
import { CarsDatasourceService } from '../../../core/services/cars.datasource.service';
import { CarsService } from '../../../core/services/cars.service';
import { RegisterCommandService } from 'app/client/components/commandreg/register.service';
import { Command, CommandCoach, CommandMember, CommandRequest } from 'app/client/components/commandreg/commandReg.component';
import { merge } from 'rxjs';
import { REGIONS } from 'app/client/components/commandreg/regions';

/**
 * Component with table car.
 */

export class CommandRequestAdmin {
    public id: number;
    public name: string;
    public commandName: string;
    public region: string;
    public ageCategory: string;
    public nomination: string;
    public musicFileName: string;
}

export class MatPaginatorIntlRu extends MatPaginatorIntl {
    itemsPerPageLabel = 'Заявок на странице:';
    nextPageLabel = 'Следующая страница';
    previousPageLabel = 'Предыдущая страница';

    getRangeLabel = (page: number, pageSize: number, length: number) => {
        if (length == 0 || pageSize == 0) {
            return `0 из ${length}`;
        }
        length = Math.max(length, 0);
        const startIndex = page * pageSize;
        const endIndex = startIndex < length ? Math.min(startIndex + pageSize, length) : startIndex + pageSize;
        return `${startIndex + 1} - ${endIndex} из ${length}`;
    };
}

@Component({
    selector: 'app-table',
    templateUrl: './table.component.html',
    styleUrls: ['./table.component.scss']
})
export class TableComponent implements OnInit {
    private regions = REGIONS;
    private categories = ['6—8', '9—11', '12—14', '15—17', '18+'];
    private nominations = ['Индивидуальные', 'Смешанные пары', 'Трио', 'Группы'];
    dataSource: MatTableDataSource<CommandRequestAdmin>;
    displayedColumns: string[] = ['name', 'commandName', 'region', 'ageCategory', 'nominations', 'musicFileName'];

    resultsLength = 0;
    isLoadingResults = true;

    selectingRegion: string;
    selectingCategory: string;
    selectingNominations: string;

    selectingRegionCleared = false;
    selectingCategoryCleared = false;
    selectingNominationsCleared = false;

    @ViewChild(MatPaginator)
    paginator: MatPaginator;
    @ViewChild(MatSort)
    sort: MatSort;

    public requests: CommandRequestAdmin[];

    constructor(private carsService: CarsService, private registerCommandService: RegisterCommandService) {
        this.registerCommandService.getAllRequests().subscribe(
            response => {
                console.log(response);
                this.resultsLength = response.length;
                this.requests = response;
                this.isLoadingResults = false;
                this.dataSource = new MatTableDataSource(response);
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
                this.dataSource.filterPredicate = function(data, filter: string): boolean {
                    console.log('filtering');
                    console.log(filter);
                    if (filter == 'clear') {
                        return true;
                    }
                    const fieldName = filter.split('$')[0];
                    const fieldValue = filter.split('$')[1];
                    return data[fieldName].toLowerCase() == fieldValue.toLowerCase();
                };
            },
            err => {
                console.log(err);
            }
        );
    }

    applyFilter(filterValue: string) {
        this.dataSource.filter = filterValue.trim().toLowerCase();
    }

    applyFilterByName(filterValue: string, filterFieldName: string) {
        switch (filterFieldName) {
            case 'region':
                if (!this.selectingRegionCleared) {
                    this.dataSource.filter = `${filterFieldName}$${filterValue}`;
                }
                this.selectingRegionCleared = false;
                break;
            case 'ageCategory':
                if (!this.selectingCategoryCleared) {
                    this.dataSource.filter = `${filterFieldName}$${filterValue}`;
                }
                this.selectingCategoryCleared = false;
                break;
            case 'nomination':
                if (!this.selectingNominationsCleared) {
                    this.dataSource.filter = `${filterFieldName}$${filterValue}`;
                }
                this.selectingNominationsCleared = false;
                break;
        }
    }

    selectRow(row) {
        console.log(row);
    }

    removeFilterByName(name) {
        switch (name) {
            case 'region':
                this.selectingRegionCleared = true;
                break;
            case 'ageCategory':
                this.selectingCategoryCleared = true;
                break;
            case 'nomination':
                this.selectingNominationsCleared = true;
                break;
        }

        this.dataSource.filter = 'clear';
    }

    public ngOnInit(): void {
        this.openTab(1);

        // If the user changes the sort order, reset back to the first page.
        /*this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

        merge(this.sort.sortChange, this.paginator.page)
            .pipe(
                startWith({}),
                (() => {
                    this.isLoadingResults = true;
                    return this.exampleDatabase!.getRepoIssues(
                        this.sort.active, this.sort.direction, this.paginator.pageIndex);
                }),
                map(data => {
                    // Flip flag to show that loading has finished.
                    this.isLoadingResults = false;
                    this.isRateLimitReached = false;
                    this.resultsLength = data.total_count;

                    return data.items;
                })
            ).subscribe(data => {
                this.data = data
            });*/
    }

    openTab(index) {
        document.getElementById('tabContent_1').style.display = 'none';
        document.getElementById('tabContent_2').style.display = 'none';
        const tab1 = document.getElementById('tab1');
        if (tab1.classList.contains('active')) {
            tab1.classList.remove('active');
            if (index === 1) {
                tab1.classList.add('active');
            }
        } else if (index === 1) {
            tab1.classList.add('active');
        }
        const tab2 = document.getElementById('tab2');
        if (tab2.classList.contains('active')) {
            tab2.classList.remove('active');
            if (index === 2) {
                tab2.classList.add('active');
            }
        } else if (index === 2) {
            tab2.classList.add('active');
        }

        document.getElementById(`tabContent_${index}`).style.display = 'block';
    }
}
