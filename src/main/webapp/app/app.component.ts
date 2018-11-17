import { Component } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router, Event } from '@angular/router';

import { LoginComponent } from './client/components/login/login.component';
import { HttpClient } from '@angular/common/http';
import { MakeAppointmentComponent } from './client/components/make-appointment/make-appointment.component';
import { StepperComponent } from './client/components/stepper/stepper.component';
import { AccountService, LoginService } from 'app/core';
import { SharedService } from 'app/client/components/shared/shared.service';

/**
 * The component is responsible for navigation and the download indicator.
 */
@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {
    /**
     * Does the resolver load.
     */
    public loading: boolean;

    /**
     * Navigation links.
     */
    public navLinks = [
        // {path: '/table', label: 'Table'},
        // {path: '/formCar', label: 'Car Form'},
        { path: '/main', label: 'Главная' }
    ];

    public isNeedShowRegistrLink = false;

    public currentUserName: string;
    /**
     * At the Navigation start show loading when the resolver loads data hide loading.
     *
     * @param router Angular router.
     * @param router.events Events navigation.
     * @param dialog Material dialog.
     */
    public constructor(
        private router: Router,
        private dialog: MatDialog,
        private account: AccountService,
        private loginService: LoginService,
        private http: HttpClient,
        ss: SharedService
    ) {
        this.router.events.subscribe((event: Event) => {
            switch (true) {
                case event instanceof NavigationStart: {
                    this.loading = true;
                    break;
                }
                case event instanceof NavigationEnd:
                case event instanceof NavigationCancel:
                case event instanceof NavigationError: {
                    this.loading = false;
                    break;
                }
                default: {
                    break;
                }
            }
        });
        ss.getEmittedValue().subscribe(item => {
            this.isNeedShowRegistrLink = item;
            if (item) {
                this.navLinks.push({
                    path: '/commandRegistration',
                    label: 'Регистрация комманды'
                });
            } else {
                this.navLinks.pop();
            }
        });
        if (this.currentUserName == null) {
            setInterval(() => this.getCurrentAccountName(), 5000);
        }
    }

    /**
     *Open Login Modal window.
     */
    public openLogin(): void {
        const dialogLogin = this.dialog.open(LoginComponent, {
            width: '250px',
            height: '350px'
        } as MatDialogConfig<any>);
    }

    public openMakeAppointment(): void {
        this.dialog.open(StepperComponent, {
            width: '1200px',
            height: '800px'
        } as MatDialogConfig<any>);
    }

    logout() {
        this.loginService.logout();
        this.router.navigate(['']);
    }

    getCurrentAccountName() {
        return Promise.resolve(
            this.account
                .get()
                .toPromise()
                .then(response => {
                    const account = response.body;
                    this.currentUserName = account.lastName + ' ' + account.firstName;
                })
        );
    }
}
