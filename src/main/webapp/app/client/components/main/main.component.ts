import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { LoginComponent } from '../login/login.component';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { StepperComponent } from '../stepper/stepper.component';
import { LoginModalService, Principal } from 'app/core';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SharedService } from 'app/client/components/shared/shared.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {
    modalRef: NgbModalRef;

    @Output()
    isNeedShowRegistrLink = new EventEmitter<boolean>();

    constructor(
        private router: Router,
        private dialog: MatDialog,
        private loginModalService: LoginModalService,
        private ss: SharedService
    ) {}

    ngOnInit() {}

    /**
     *Open Login Modal window.
     */
    public openLogin(): void {
        this.modalRef = this.loginModalService.open();
        /*  const dialogLogin = this.dialog.open(LoginComponent, {
            width: '250px',
            height: '350px',
        } as MatDialogConfig<any>);*/
    }

    register() {
        this.router.navigate(['/register']);
    }

    public openMakeAppointment(): void {
        this.dialog.open(StepperComponent, {
            width: '1200px',
            height: '800px',
            panelClass: 'dialog-no-padding-panel'
        } as MatDialogConfig<any>);
    }
}
