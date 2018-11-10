import {Component, OnInit} from '@angular/core';
import {LoginComponent} from '../login/login.component';
import {MatDialog, MatDialogConfig} from '@angular/material';
import {StepperComponent} from '../stepper/stepper.component';
import {LoginModalService, Principal} from "app/core";
import {NgbModalRef} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit {
    modalRef: NgbModalRef;

    constructor(private dialog: MatDialog, private loginModalService: LoginModalService) {
    }

    ngOnInit() {
    }

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

    public openMakeAppointment(): void {
        this.dialog.open(StepperComponent,
            {
                width: '1200px',
                height: '800px',
                panelClass: 'dialog-no-padding-panel',
            } as MatDialogConfig<any>);
    }
}
