import { Component, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
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
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLoginModalComponent } from 'app/shared';
import { RequestModalComponent } from 'app/client/components/table/modals/request_modal.component';
import { CommandModalComponent } from 'app/client/components/table/modals/command_modal.component';
import { SERVER_API_URL } from 'app/app.constants';

/**
 * Component with table car.
 */

export class DonwloadFileRequest {
    public musicFile: string;

    public commandName: string;

    public musicFileName: string;
}

export class CommandRequestAdmin {
    public id: string;
    public name: string;
    public commandName: string;
    public region: string;
    public ageCategory: string;
    public nomination: string;
    public musicFileName: string;
}

export class CommandUserInfo {
    public commandId: number;
    public userName: string;
    public commandName: string;
    public region: string;
    public phoneNumber: string;
    public mail: string;
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
    public regions = REGIONS;
    public categories = ['6—8', '9—11', '12—14', '15—17', '18+'];
    public nominations = ['ИЖ', 'ИМ', 'Смешанные пары', 'Трио', 'Группы', 'Гимнастическая платформа', 'Танцевальная гимнастика'];
    dataSource: MatTableDataSource<CommandRequestAdmin>;
    dataSourceUsers: MatTableDataSource<CommandUserInfo>;
    displayedColumns: string[] = ['commandName', 'region', 'ageCategory', 'nominations', 'musicFileName'];
    displayedColumnsUsers: string[] = ['userName', 'commandName', 'region', 'phoneNumber', 'mail'];

    resultsLength = 0;
    resultsLengthUsers = 0;
    isLoadingResults = true;
    isLoadingResultsUsers = true;

    selectingRegion: string;
    selectingCategory: string;
    selectingNominations: string;

    selectingRegionUsers: string;

    selectingRegionCleared = false;
    selectingCategoryCleared = false;
    selectingNominationsCleared = false;
    selectingNameCleared = false;

    selectingRegionUsersCleared = false;

    modalRef: NgbModalRef;

    @ViewChildren(MatPaginator)
    paginators: QueryList<MatPaginator>;

    @ViewChild('reqSort')
    sort: MatSort;
    @ViewChild('usersSort')
    sortUsers: MatSort;

    public requests: CommandRequestAdmin[];
    public users: CommandUserInfo[];

    constructor(private carsService: CarsService, private registerCommandService: RegisterCommandService, private modalService: NgbModal) {
        this.registerCommandService.getAllRequests().subscribe(
            response => {
                this.resultsLength = response.length;
                this.requests = response;
                this.isLoadingResults = false;
                this.dataSource = new MatTableDataSource(response);
                console.log(this.paginators);
                this.dataSource.paginator = this.paginators.first;
                this.dataSource.sort = this.sort;
                this.dataSource.filterPredicate = function(data, filter: string): boolean {
                    if (filter == 'clear') {
                        return true;
                    }
                    const filters = filter.split(',');
                    let isOk = true;
                    filters.forEach(fil => {
                        const fieldName = fil.split('$')[0];
                        const fieldValue = fil.split('$')[1];

                        if (fieldName == 'commandName' && isOk) {
                            isOk = data[fieldName].toLowerCase().includes(fieldValue.toLowerCase());
                        } else if (isOk) {
                            isOk = data[fieldName].toLowerCase() == fieldValue.toLowerCase();
                        }
                    });
                    return isOk;
                };
            },
            err => {
                console.log(err);
            }
        );

        this.registerCommandService.getAllCommandUserInfo().subscribe(
            response => {
                this.resultsLengthUsers = response.length;
                this.users = response;
                this.isLoadingResultsUsers = false;
                this.dataSourceUsers = new MatTableDataSource(response);
                this.dataSourceUsers.paginator = this.paginators.last;
                this.dataSourceUsers.sort = this.sortUsers;
                this.dataSourceUsers.filterPredicate = function(data, filter: string): boolean {
                    if (filter == 'clear') {
                        return true;
                    }
                    const filters = filter.split(',');
                    let isOk = true;
                    filters.forEach(fil => {
                        const fieldName = fil.split('$')[0];
                        const fieldValue = fil.split('$')[1];

                        if (fieldName == 'name' && isOk) {
                            isOk = data['userName'].toLowerCase().includes(fieldValue.toLowerCase());
                        } else if (isOk) {
                            isOk = data[fieldName].toLowerCase() == fieldValue.toLowerCase();
                        }
                    });
                    return isOk;
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

    setFilter(filterValue: string, filterFieldName: string) {
        if (!this.dataSource.filter || this.dataSource.filter == 'clear') {
            this.dataSource.filter = `${filterFieldName}$${filterValue}`;
        } else {
            let filters = this.dataSource.filter.split(',');
            let isFound = false;
            filters = filters.map(fil => {
                const fieldName = fil.split('$')[0];
                const fieldValue = fil.split('$')[1];
                if (fieldName == filterFieldName) {
                    isFound = true;
                    return `${filterFieldName}$${filterValue}`;
                }
                return fil;
            });

            if (!isFound) {
                filters.push(`${filterFieldName}$${filterValue}`);
            }
            this.dataSource.filter = filters.join(',');
        }
    }

    applyFilterByName(event, filterValue: string, filterFieldName: string) {
        if ((event.source && event.isUserInput) || event.target) {
            this.setFilter(filterValue, filterFieldName);
        }
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
        const filters = this.dataSource.filter.split(',');
        this.dataSource.filter = filters
            .filter(fil => {
                const fieldName = fil.split('$')[0];
                const fieldValue = fil.split('$')[1];
                return fieldName != name;
            })
            .join(',');
    }

    setUsersFilter(filterValue: string, filterFieldName: string) {
        if (!this.dataSourceUsers.filter || this.dataSourceUsers.filter == 'clear') {
            this.dataSourceUsers.filter = `${filterFieldName}$${filterValue}`;
        } else {
            let filters = this.dataSourceUsers.filter.split(',');
            let isFound = false;
            filters = filters.map(fil => {
                const fieldName = fil.split('$')[0];
                const fieldValue = fil.split('$')[1];
                if (fieldName == filterFieldName) {
                    isFound = true;
                    return `${filterFieldName}$${filterValue}`;
                }
                return fil;
            });
            if (!isFound) {
                filters.push(`${filterFieldName}$${filterValue}`);
            }
            this.dataSourceUsers.filter = filters.join(',');
        }
    }

    applyUsersFilterByName(event, filterValue: string, filterFieldName: string) {
        if ((event.source && event.isUserInput) || event.target) {
            this.setUsersFilter(filterValue, filterFieldName);
        }
    }

    removeUsersFilterByName(name) {
        switch (name) {
            case 'region':
                this.selectingRegionUsersCleared = true;
                break;
        }
        const filters = this.dataSourceUsers.filter.split(',');
        this.dataSourceUsers.filter = filters
            .filter(fil => {
                const fieldName = fil.split('$')[0];
                const fieldValue = fil.split('$')[1];
                return fieldName != name;
            })
            .join(',');
    }

    private isOpen = false;

    public openRequestModal(requestId: number): void {
        if (this.isOpen) {
            return;
        }
        this.modalRef = this.modalService.open(RequestModalComponent, { size: 'lg' });
        this.modalRef.componentInstance.requestId = requestId;
        this.isOpen = true;
        this.modalRef.result.then(
            result => {
                this.isOpen = false;
            },
            reason => {
                this.isOpen = false;
            }
        );
    }

    public openCommandModal(commandId: number): void {
        if (this.isOpen) {
            return;
        }
        this.modalRef = this.modalService.open(CommandModalComponent, { size: 'lg' });
        this.modalRef.componentInstance.commandId = commandId;
        this.isOpen = true;
        this.modalRef.result.then(
            result => {
                this.isOpen = false;
            },
            reason => {
                this.isOpen = false;
            }
        );
    }

    private clickTime;

    saveClickTime() {
        this.clickTime = Date.now();
    }

    selectRow(row) {
        const curTime = Date.now();
        var diff = curTime - this.clickTime;
        if (diff > 300) {
            return;
        } else {
            this.openRequestModal(row.id);
        }
    }

    selectRowUsers(row) {
        const curTime = Date.now();
        var diff = curTime - this.clickTime;
        if (diff > 300) {
            return;
        } else {
            this.openCommandModal(row.commandId);
        }
    }

    downloadMusicFile(row) {
        window.open(SERVER_API_URL + 'api/downloadMusicFile?id=' + row.id + '&commandName=' + row.commandName, '_blank');
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

    sortDataUsers(event) {
        this.dataSourceUsers.data.sort((a, b) => {
            const isAsc = event.direction === 'asc';
            switch (event.active) {
                case 'userName':
                    return this.compare(a.userName, b.userName, isAsc);
                case 'commandName':
                    return this.compare(a.commandName, b.commandName, isAsc);
                case 'region':
                    return this.compare(a.region, b.region, isAsc);
                case 'mail':
                    return this.compare(a.mail, b.mail, isAsc);
                case 'phoneNumber':
                    return this.compare(a.phoneNumber, b.phoneNumber, isAsc);
                default:
                    return 0;
            }
        });
    }

    sortData(event) {
        this.dataSource.data.sort((a, b) => {
            const isAsc = event.direction === 'asc';
            switch (event.active) {
                case 'commandName':
                    return this.compare(a.commandName, b.commandName, isAsc);
                case 'region':
                    return this.compare(a.region, b.region, isAsc);
                case 'ageCategory':
                    return this.compare(a.ageCategory, b.ageCategory, isAsc);
                case 'nominations':
                    return this.compare(a.nomination, b.nomination, isAsc);
                default:
                    return 0;
            }
        });
    }

    compare(a: string, b: string, isAsc: boolean) {
        return a.localeCompare(b) * (isAsc ? 1 : -1);
    }
}
