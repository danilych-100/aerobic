import { Component, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { MatTableDataSource } from '@angular/material';
import { RegisterCommandService } from 'app/client/components/commandreg/register.service';
import { CommandRequestAdmin, DonwloadFileRequest } from 'app/client/components/table/table.component';
import { CommandCoach, CommandMember } from 'app/client/components/commandreg/commandReg.component';
import { SERVER_API_URL } from 'app/app.constants';

export class RequestInfo {
    public generalInfo: CommandRequestAdmin;
    public phoneNumber: string;
    public mail: string;
    public members: CommandMember[];
    public coaches: CommandCoach[];
}

@Component({
    selector: 'request-modal',
    templateUrl: './request_modal.component.html'
})
export class RequestModalComponent implements AfterViewInit {
    requestId: number;
    requestInfo: RequestInfo;

    constructor(private registerCommandService: RegisterCommandService, public activeModal: NgbActiveModal) {}

    cancel() {
        this.activeModal.dismiss('cancel');
    }

    ngAfterViewInit(): void {
        console.log(this.requestId);
        this.registerCommandService.getRequestInfo(this.requestId).subscribe(
            response => {
                console.log(response);
                this.requestInfo = response;
            },
            err => {
                console.log(err);
            }
        );
    }

    downloadMusicFile() {
        let donwloadFileRequest = new DonwloadFileRequest();
        donwloadFileRequest.commandName = this.requestInfo.generalInfo.commandName;
        donwloadFileRequest.musicFile = this.requestInfo.generalInfo.music;
        donwloadFileRequest.musicFileName = this.requestInfo.generalInfo.musicFileName;
        this.registerCommandService.saveDownloadedMusicFile(donwloadFileRequest).subscribe(
            res => {
                console.log(res.id);
                window.open(SERVER_API_URL + 'api/downloadMusicFile?id=' + res.id, '_blank');
            },
            res => {
                console.log(res);
            }
        );
    }
}
