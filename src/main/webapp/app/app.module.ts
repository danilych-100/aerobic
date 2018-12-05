import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { Ng2Webstorage } from 'ngx-webstorage';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { MedicSharedModule } from 'app/shared';
import { MedicCoreModule } from 'app/core';
import { MedicAppRoutingModule } from './app-routing.module';
import { MedicHomeModule } from './home/home.module';
import { MedicAccountModule } from './account/account.module';
import { MedicEntityModule } from './entities/entity.module';
import { AppComponent } from './app.component';
import { ClientModule } from './client/client.module';
import * as moment from 'moment';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent, NavbarComponent, FooterComponent, PageRibbonComponent, ErrorComponent } from './layouts';
import { NgxPermissionsModule, NgxPermissionsService } from 'ngx-permissions';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatPaginatorIntl } from '@angular/material';
import { MatPaginatorIntlRu } from 'app/client/components/table/table.component';

@NgModule({
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        MedicAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        MedicSharedModule,
        MedicCoreModule,
        MedicHomeModule,
        MedicAccountModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        MedicEntityModule,
        ClientModule,
        NgxPermissionsModule.forRoot()
    ],
    declarations: [NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent, AppComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true
        },
        {
            provide: MatPaginatorIntl,
            useClass: MatPaginatorIntlRu
        }
    ],
    bootstrap: [AppComponent]
})
export class MedicAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
    }
}
