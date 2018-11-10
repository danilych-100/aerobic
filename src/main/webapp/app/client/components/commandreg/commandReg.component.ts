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
    passSeries = new FormControl('', [Validators.required, Validators.minLength(4), Validators.maxLength(4)]);
    passNumber = new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]);
    passSeriesMember = new FormControl('', [Validators.required, Validators.minLength(4), Validators.maxLength(4)]);
    passNumberMember = new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]);
    inpNameCoach = new FormControl('', [Validators.required]);
    inpNameMember = new FormControl('', [Validators.required]);
    inpDateCoach = new FormControl('', [Validators.required]);
    inpDateMember = new FormControl('', [Validators.required]);
    inpPassDescCoach = new FormControl('', [Validators.required]);
    inpPassDescMember = new FormControl('', [Validators.required]);
    inpNameTeam = new FormControl('', [Validators.required]);
    inpMemberCountTeam = new FormControl('', [Validators.required]);
    inpRegionTeam = new FormControl('', [Validators.required]);
    inpNominationTeam = new FormControl('', [Validators.required]);
    inpCategoryTeam = new FormControl('', [Validators.required]);
    phone = new FormControl('', [Validators.required]);
    quality = new FormControl('', [Validators.required]);

    getMailErrorMessage() {
        return this.email.hasError('email') ? 'Не корректный email' : '';
    }
    getPhoneErrorMessage() {
        return '';
    }
    getQualityErrorMessage() {
        return this.quality.hasError('required') ? 'Поле не должно быть пустым' : '';
    }
    getPassSeriesErrorMessage() {
        return this.passSeries.hasError('minlength') || this.passSeries.hasError('maxlength') ? 'Серия паспорта должна быть 4 символа' : '';
    }
    getPassNumberErrorMessage() {
        return this.passNumber.hasError('minlength') || this.passNumber.hasError('maxlength')
            ? 'Номер паспорта должен быть 6 символов'
            : '';
    }
    getPassSeriesMemberErrorMessage() {
        return this.passSeriesMember.hasError('minlength') || this.passSeriesMember.hasError('maxlength')
            ? 'Серия паспорта должна быть 4 символа'
            : '';
    }
    getPassNumberMemberErrorMessage() {
        return this.passNumberMember.hasError('minlength') || this.passNumberMember.hasError('maxlength')
            ? 'Номер паспорта должен быть 6 символов'
            : '';
    }
    getInpDateCoachErrorMessage() {
        return this.inpDateCoach.hasError('required') ? 'Поле не должно быть пустым' : '';
    }
    getInpDateMemberErrorMessage() {
        return this.inpDateMember.hasError('required') ? 'Поле не должно быть пустым' : '';
    }
    getInpNameCoachErrorMessage() {
        return this.inpNameCoach.hasError('required') ? 'Поле не должно быть пустым' : '';
    }
    getInpNameMemberErrorMessage() {
        return this.inpNameMember.hasError('required') ? 'Поле не должно быть пустым' : '';
    }
    getInpPassDescCoachErrorMessage() {
        return this.inpPassDescCoach.hasError('required') ? 'Поле не должно быть пустым' : '';
    }
    getInpPassDescMemberErrorMessage() {
        return this.inpPassDescMember.hasError('required') ? 'Поле не должно быть пустым' : '';
    }
    getInpNameTeamErrorMessage() {
        return this.inpNameTeam.hasError('required') ? 'Поле не должно быть пустым' : '';
    }
    getInpMemberCountTeamErrorMessage() {
        return this.inpMemberCountTeam.hasError('required') ? 'Поле не должно быть пустым' : '';
    }
    getInpRegionTeamErrorMessage() {
        return this.inpRegionTeam.hasError('required') ? 'Поле не должно быть пустым' : '';
    }
    getInpNominationTeamErrorMessage() {
        return this.inpNominationTeam.hasError('required') ? 'Поле не должно быть пустым' : '';
    }
    getInpCategoryTeamErrorMessage() {
        return this.inpCategoryTeam.hasError('required') ? 'Поле не должно быть пустым' : '';
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

    resetMainForm() {
        this.inpRegionTeam = new FormControl('', [Validators.required]);
        this.inpNameTeam = new FormControl('', [Validators.required]);
        this.inpCategoryTeam = new FormControl('', [Validators.required]);
        this.inpNominationTeam = new FormControl('', [Validators.required]);
        this.inpMemberCountTeam = new FormControl('', [Validators.required]);
        this.phone = new FormControl('', [Validators.required]);
        this.email = new FormControl('', [Validators.required, Validators.email]);
        this.resetCoachForm();
        this.resetMemberForm();
    }
    resetCoachForm() {
        this.inpDateCoach = new FormControl('', [Validators.required]);
        this.inpNameCoach = new FormControl('', [Validators.required]);
        this.passSeries = new FormControl('', [Validators.required, Validators.minLength(4), Validators.maxLength(4)]);
        this.passNumber = new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]);
        this.inpPassDescCoach = new FormControl('', [Validators.required]);
    }

    resetMemberForm() {
        this.inpDateMember = new FormControl('', [Validators.required]);
        this.inpNameMember = new FormControl('', [Validators.required]);
        this.passSeriesMember = new FormControl('', [Validators.required, Validators.minLength(4), Validators.maxLength(4)]);
        this.passNumberMember = new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]);
        this.quality = new FormControl('', [Validators.required]);
        this.inpPassDescMember = new FormControl('', [Validators.required]);
    }

    addCoach() {
        this.resetCoachForm();

        const newCoach = new CommandCoach();
        newCoach.name = this.currentCoach.name;
        newCoach.birthDate = this.currentCoach.birthDate;
        newCoach.passportDesc = this.currentCoach.passportDesc;
        newCoach.passportNumber = this.currentCoach.passportNumber;
        newCoach.passportSeries = this.currentCoach.passportSeries;
        this.command.coaches.push(newCoach);
        this.currentCoach = new CommandCoach();
    }

    removeCoach(index: number) {
        this.command.coaches.splice(index, 1);
    }

    removeMember(index: number) {
        this.command.members.splice(index, 1);
    }

    addMember() {
        this.resetMemberForm();

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
