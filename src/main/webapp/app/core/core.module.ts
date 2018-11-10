import { NgModule, LOCALE_ID } from '@angular/core';
import { DatePipe, registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Title } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import locale from '@angular/common/locales/en';

import { CarResolver } from './services/car.resolver';
import { CarsDatasourceService } from './services/cars.datasource.service';
import { CarsService } from './services/cars.service';
import { DeactivateFormGuard } from './services/deactivate-form.guard';
import { EnsureAuthenticatedGuard } from './services/ensure-authenticated.guard';
import { LoginService } from './services/login.service';
import { MapperCarsService } from './services/mapper-cars.service';


@NgModule({
    imports: [
        CommonModule,
        HttpClientModule
    ],
    exports: [],
    declarations: [],
    providers: [
        Title,
        {
            provide: LOCALE_ID,
            useValue: 'en'
        },
        DatePipe,
        CarResolver,
        CarsService,
        CarsDatasourceService,
        MapperCarsService,
        LoginService,
        EnsureAuthenticatedGuard,
        DeactivateFormGuard,
    ]
})
export class MedicCoreModule {
    constructor() {
        registerLocaleData(locale);
    }
}
