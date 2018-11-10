import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    MatSidenavModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatDividerModule
} from '@angular/material';
import { NgxPermissionsModule } from 'ngx-permissions';
import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { MedicSharedLibsModule, MedicSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { HasNoAuthorityDirective } from 'app/shared/auth/has-no-authority.directive';

@NgModule({
    imports: [
        MedicSharedLibsModule,
        MedicSharedCommonModule,
        CommonModule,
        FormsModule,
        MatButtonModule,
        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        MatProgressSpinnerModule,
        MatInputModule,
        MatSidenavModule,
        MatToolbarModule,
        MatTabsModule,
        MatTabsModule,
        MatSidenavModule,
        MatProgressBarModule,
        MatDividerModule
    ],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, HasNoAuthorityDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [
        MedicSharedCommonModule,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        HasNoAuthorityDirective,
        FormsModule,
        MatFormFieldModule,
        MatTabsModule,
        MatSidenavModule,
        MatToolbarModule,
        NgxPermissionsModule,
        MatInputModule,
        MatButtonModule,
        MatSidenavModule,
        MatSelectModule,
        MatProgressBarModule
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MedicSharedModule {}
