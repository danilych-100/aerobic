import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DoctorsService } from '../../../core/services/doctors.service';
import { Observable } from 'rxjs/Rx';
import { Doctor } from '../../../core/models/doctor';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DateAdapter } from '@angular/material';

export class Command {
    public region: string;
    public name: string;
    public ageCategory: string;
    public nomination: string;
    public memberCount: number;
    public phoneNumber: string;
    public email: string;
    public members: CommandMember[];
    public coaches: CommandCoach[];

    constructor() {
        this.members = [];
        this.coaches = [];
    }
}

export class CommandCoach {
    public name: string;
    public birthDate: Date;
    public passportSeries: number;
    public passportNumber: number;
    public passportDesc: string;
}

export class CommandMember {
    public name: string;
    public birthDate: Date;
    public passportSeries: number;
    public passportNumber: number;
    public passportDesc: string;
    public quality: string;
}

@Component({
    selector: 'app-command-reg',
    templateUrl: './commandReg.component.html',
    styleUrls: ['./commandReg.component.scss']
})
export class CommandRegistrationComponent implements OnInit {
    options: FormGroup;

    private command: Command;

    private currentCoach: CommandCoach;
    private currentMember: CommandMember;

    private regions = ['Свердловская область', 'Пермская область', 'Московская область', 'Магаданская область'];

    private categories = ['6—8', '9—11', '12—14', '15—17', '18+'];

    private nominations = ['Индивидуальные', 'Смешанные пары', 'Трио', 'Группы'];

    constructor(fb: FormBuilder, private dateAdapter: DateAdapter<Date>) {
        this.options = fb.group({
            color: 'primary',
            fontSize: [18, Validators.min(10)]
        });
        this.dateAdapter.setLocale('ru');
    }

    ngOnInit() {
        this.command = new Command();
        this.currentCoach = new CommandCoach();
        this.currentMember = new CommandMember();
    }

    getFontSize() {
        return Math.max(10, this.options.value.fontSize);
    }
}
