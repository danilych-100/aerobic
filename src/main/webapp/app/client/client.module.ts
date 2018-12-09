import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatDatepickerModule,
    MatDialogModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatNativeDateModule,
    MatPaginatorIntl,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatSelectModule,
    MatSnackBarModule,
    MatSortModule,
    MatStepperModule,
    MatTableModule,
    MatTabsModule,
    MatTooltipModule
} from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { ClientRoutingModule } from './client-routing.module';
import { DialogDeactivationComponent } from './components/dialog-deactivation/dialog-deactivation.component';
import { FormCarComponent } from './components/form-car/form-car.component';
import { LoginComponent } from './components/login/login.component';
import { TableComponent } from './components/table/table.component';
import { CarsMatPaginatorIntl } from './intl/cars-mat-paginator-intl';
import { DoctorsComponent } from './components/doctors/doctors.component';
import { MakeAppointmentComponent } from './components/make-appointment/make-appointment.component';
import { StepperComponent } from './components/stepper/stepper.component';
import { BankCardComponent } from './components/bank-card/bank-card.component';
import { CreditCardDirective } from './directives/credit-card.directive';
import { MainComponent } from './components/main/main.component';
import { CommandRegistrationComponent } from 'app/client/components/commandreg/commandReg.component';
import { SharedService } from 'app/client/components/shared/shared.service';
import { TabModule } from 'angular-tabs-component';
import { MembersFilterPipe } from 'app/client/components/commandreg/members.filter';
import { CovalentFileModule } from '@covalent/core';
import { RequestModalComponent } from 'app/client/components/table/modals/request_modal.component';
import { JhiLoginModalComponent } from 'app/shared';
import { CommandModalComponent } from 'app/client/components/table/modals/command_modal.component';

/**
 * Standard module.
 */
@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        BrowserAnimationsModule,
        MatButtonModule,
        MatTableModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatPaginatorModule,
        MatCardModule,
        MatSortModule,
        MatProgressSpinnerModule,
        MatInputModule,
        ReactiveFormsModule,
        ClientRoutingModule,
        MatCheckboxModule,
        MatRadioModule,
        MatStepperModule,
        MatFormFieldModule,
        MatListModule,
        MatSelectModule,
        MatDialogModule,
        MatSnackBarModule,
        MatIconModule,
        MatExpansionModule,
        MatTooltipModule,
        MatTabsModule,
        TabModule,
        CovalentFileModule
    ],
    providers: [{ provide: MatPaginatorIntl, useClass: CarsMatPaginatorIntl }],
    declarations: [
        TableComponent,
        FormCarComponent,
        LoginComponent,
        DialogDeactivationComponent,
        DoctorsComponent,
        MakeAppointmentComponent,
        StepperComponent,
        BankCardComponent,
        CreditCardDirective,
        MainComponent,
        CommandRegistrationComponent,
        MembersFilterPipe,
        RequestModalComponent,
        CommandModalComponent
    ],
    entryComponents: [DialogDeactivationComponent, LoginComponent, StepperComponent, RequestModalComponent, CommandModalComponent]
})
export class ClientModule {}
