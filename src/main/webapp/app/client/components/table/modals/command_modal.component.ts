import { Component, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { MatTableDataSource } from '@angular/material';
import { RegisterCommandService } from 'app/client/components/commandreg/register.service';
import { CommandRequestAdmin, DonwloadFileRequest } from 'app/client/components/table/table.component';
import { Command, CommandCoach, CommandMember } from 'app/client/components/commandreg/commandReg.component';
import { SERVER_API_URL } from 'app/app.constants';

@Component({
    selector: 'command-modal',
    templateUrl: './command_modal.component.html'
})
export class CommandModalComponent implements AfterViewInit {
    commandId: number;
    command: Command;

    constructor(private registerCommandService: RegisterCommandService, public activeModal: NgbActiveModal) {}

    cancel() {
        this.activeModal.dismiss('cancel');
    }

    ngAfterViewInit(): void {
        this.registerCommandService.getCommand(this.commandId).subscribe(
            response => {
                this.command = response;
            },
            err => {
                console.log(err);
            }
        );
    }

    downloadMusicFile(request) {
        window.open(SERVER_API_URL + 'api/downloadMusicFile?id=' + request.id + '&commandName=' + this.command.name, '_blank');
    }
}
