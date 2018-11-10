import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DoctorsService } from '../../../core/services/doctors.service';
import { Observable } from 'rxjs/Rx';
import { Doctor } from '../../../core/models/doctor';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
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
    optionsMain: FormGroup;
    optionsCoach: FormGroup;
    optionsMember: FormGroup;

    private command: Command;

    private currentCoach: CommandCoach;
    private currentMember: CommandMember;

    private regions = ['Свердловская область', 'Пермская область', 'Московская область', 'Магаданская область'];
    private categories = ['6—8', '9—11', '12—14', '15—17', '18+'];
    private nominations = ['Индивидуальные', 'Смешанные пары', 'Трио', 'Группы'];
    private qualities = [
        'Мастер спорта',
        'Кандидат в мастера спорта',
        'I разряд',
        'II разряд',
        'III разряд',
        'I юношеский разряд',
        'II юношеский разряд',
        'III юношеский разряд'
    ];

    email = new FormControl('', [Validators.required, Validators.email]);
    inp = new FormControl('', [Validators.required]);

    getMailErrorMessage() {
        return this.email.hasError('required') ? 'Поле не должно быть пустым' : this.email.hasError('email') ? 'Not a valid email' : '';
    }
    getInpErrorMessage() {
        return this.email.hasError('required') ? 'You must enter a value' : '';
    }
    constructor(private fb: FormBuilder, private dateAdapter: DateAdapter<Date>) {
        this.optionsMain = this.fb.group({
            color: 'primary',
            fontSize: [16, Validators.min(10)]
        });
        this.optionsCoach = this.fb.group({
            color: 'primary',
            fontSize: [16, Validators.min(10)]
        });
        this.optionsMember = this.fb.group({
            color: 'primary',
            fontSize: [16, Validators.min(10)]
        });
        this.dateAdapter.setLocale('ru');
    }

    ngOnInit() {
        this.command = new Command();
        this.currentCoach = new CommandCoach();
        this.currentMember = new CommandMember();
    }

    addCoach(coachForm: FormGroupDirective) {
        console.log(coachForm.form);
        const newCoach = new CommandCoach();
        newCoach.name = this.currentCoach.name;
        newCoach.birthDate = this.currentCoach.birthDate;
        newCoach.passportDesc = this.currentCoach.passportDesc;
        newCoach.passportNumber = this.currentCoach.passportNumber;
        newCoach.passportSeries = this.currentCoach.passportSeries;
        this.command.coaches.push(newCoach);
        this.currentCoach = new CommandCoach();
    }

    addMember() {
        const newMember = new CommandMember();
        newMember.name = this.currentMember.name;
        newMember.birthDate = this.currentMember.birthDate;
        newMember.passportDesc = this.currentMember.passportDesc;
        newMember.passportNumber = this.currentMember.passportNumber;
        newMember.passportSeries = this.currentMember.passportSeries;
        newMember.quality = this.currentMember.quality;
        this.command.members.push(newMember);
        this.currentMember = new CommandMember();
    }

    getFontSize() {
        return Math.max(10, this.optionsMain.value.fontSize);
    }
}
