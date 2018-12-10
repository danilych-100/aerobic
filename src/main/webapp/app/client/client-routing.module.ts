import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { CarResolver } from '../core/services/car.resolver';
import { DeactivateFormGuard } from '../core/services/deactivate-form.guard';
import { EnsureAuthenticatedGuard } from '../core/services/ensure-authenticated.guard';

import { FormCarComponent } from './components/form-car/form-car.component';
import { LoginComponent } from './components/login/login.component';
import { TableComponent } from './components/table/table.component';
import { DoctorsComponent } from './components/doctors/doctors.component';
import { StepperComponent } from './components/stepper/stepper.component';
import { MainComponent } from './components/main/main.component';
import { CommandRegistrationComponent } from 'app/client/components/commandreg/commandReg.component';
import { AuthGuardService } from 'app/client/auth/auth-guard.service';

const routes: Routes = [
    {
        path: 'table',
        component: TableComponent
    },
    {
        path: 'commandRegistration',
        component: CommandRegistrationComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'main',
        component: MainComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ClientRoutingModule {}
