import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DoctorsService } from '../../../core/services/doctors.service';
import { Observable } from 'rxjs/Rx';
import { Doctor } from '../../../core/models/doctor';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { DateAdapter, MatSelectionList, MatSelectionListChange } from '@angular/material';
import { REGIONS } from './regions';
import { RegisterCommandService } from './register.service';

export class Command {
    public userId: number;
    public region: string;
    public name: string;
    public memberCount: number;
    public phoneNumber: string;
    public email: string;
    public members: CommandMember[];
    public coaches: CommandCoach[];
    public requests: CommandRequest[];

    constructor() {
        this.members = [];
        this.coaches = [];
        this.requests = [];
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
    public gender = 'male';
    public passportSeries: number;
    public passportNumber: number;
    public passportDesc: string;
    public birthCertificateNumber: number;
    public birthCertificateDesc: string;
    public quality: string;
}

export class CommandRequest {
    public name: string;
    public ageCategory: string;
    public nomination: string;
    public music: File;
    public members: CommandMember[];
    public coaches: CommandCoach[];

    constructor() {
        this.members = [];
        this.coaches = [];
    }
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
    private currentCommandRequest: CommandRequest;

    private regions = REGIONS;
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

    maxDate = new Date();
    email = new FormControl('', [Validators.email]);
    birthCertificateDesc = new FormControl('', [Validators.maxLength(100)]);
    birthCertificateNumber = new FormControl('', [Validators.maxLength(100), Validators.pattern('^\\d*$')]);
    passSeries = new FormControl('', [Validators.minLength(4), Validators.maxLength(4), Validators.pattern('^\\d*$')]);
    passNumber = new FormControl('', [Validators.minLength(6), Validators.maxLength(6), Validators.pattern('^\\d*$')]);
    passSeriesMember = new FormControl('', [Validators.minLength(4), Validators.maxLength(4), Validators.pattern('^\\d*$')]);
    passNumberMember = new FormControl('', [Validators.minLength(6), Validators.maxLength(6), Validators.pattern('^\\d*$')]);
    inpNameCoach = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    inpNameMember = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    inpDateCoach = new FormControl('', [Validators.required]);
    inpDateMember = new FormControl('', [Validators.required]);
    inpPassDescCoach = new FormControl('', [Validators.maxLength(100)]);
    inpPassDescMember = new FormControl('', [Validators.maxLength(100)]);
    inpNameTeam = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    inpMemberCountTeam = new FormControl('', [Validators.required, Validators.min(1), Validators.max(20)]);
    inpRegionTeam = new FormControl('', [Validators.required]);
    inpNominationTeam = new FormControl('', [Validators.required]);
    inpCategoryTeam = new FormControl('', [Validators.required]);
    phone = new FormControl('', [Validators.required, Validators.pattern('^\\d{10}$')]);
    quality = new FormControl('', [Validators.required]);

    getMailErrorMessage() {
        return this.email.hasError('email') ? 'Не корректный email' : '';
    }
    getPhoneErrorMessage() {
        return this.phone.hasError('pattern') ? 'Нужно ввести 10 цифр номера телефона' : '';
    }
    getQualityErrorMessage() {
        return this.quality.hasError('required') ? 'Поле не должно быть пустым' : '';
    }
    getInpBirthCertificateNumberErrorMessage() {
        return this.birthCertificateNumber.hasError('pattern')
            ? 'Поле должно содержать цифры'
            : this.birthCertificateNumber.hasError('maxlength')
                ? 'Поле должно содержать не более 100 символов'
                : '';
    }
    getInpBirthCertificateDescErrorMessage() {
        return this.birthCertificateDesc.hasError('maxlength') ? 'Поле должно содержать не более 100 символов' : '';
    }
    getPassSeriesErrorMessage() {
        return this.passSeries.hasError('minlength') || this.passSeries.hasError('maxlength')
            ? 'Серия паспорта должна быть 4 символа'
            : this.passSeries.hasError('pattern')
                ? 'Поле должно состоять из цифр'
                : '';
    }
    getPassNumberErrorMessage() {
        return this.passNumber.hasError('minlength') || this.passNumber.hasError('maxlength')
            ? 'Номер паспорта должен быть 6 символов'
            : this.passNumber.hasError('pattern')
                ? 'Поле должно состоять из цифр'
                : '';
    }
    getPassSeriesMemberErrorMessage() {
        return this.passSeriesMember.hasError('minlength') || this.passSeriesMember.hasError('maxlength')
            ? 'Серия паспорта должна быть 4 символа'
            : this.passSeriesMember.hasError('pattern')
                ? 'Поле должно состоять из цифр'
                : '';
    }
    getPassNumberMemberErrorMessage() {
        return this.passNumberMember.hasError('minlength') || this.passNumberMember.hasError('maxlength')
            ? 'Номер паспорта должен быть 6 символов'
            : this.passNumberMember.hasError('pattern')
                ? 'Поле должно состоять из цифр'
                : '';
    }
    getInpDateCoachErrorMessage() {
        return this.inpDateCoach.hasError('required') ? 'Поле не должно быть пустым' : '';
    }
    getInpDateMemberErrorMessage() {
        return this.inpDateMember.hasError('required') ? 'Поле не должно быть пустым' : '';
    }
    getInpNameCoachErrorMessage() {
        return this.inpNameCoach.hasError('required')
            ? 'Поле не должно быть пустым'
            : this.inpNameCoach.hasError('maxlength')
                ? 'ФИО должно быть не более 100 символов'
                : '';
    }
    getInpNameMemberErrorMessage() {
        return this.inpNameMember.hasError('required')
            ? 'Поле не должно быть пустым'
            : this.inpNameMember.hasError('maxlength')
                ? 'ФИО должно быть не более 100 символов'
                : '';
    }
    getInpPassDescCoachErrorMessage() {
        return this.inpPassDescCoach.hasError('required')
            ? 'Поле не должно быть пустым'
            : this.inpPassDescCoach.hasError('maxlength')
                ? 'Поле должно быть не более 100 символов'
                : '';
    }
    getInpPassDescMemberErrorMessage() {
        return this.inpPassDescMember.hasError('required')
            ? 'Поле не должно быть пустым'
            : this.inpPassDescMember.hasError('maxlength')
                ? 'Поле должно быть не более 100 символов'
                : '';
    }
    getInpNameTeamErrorMessage() {
        return this.inpNameTeam.hasError('required')
            ? 'Поле не должно быть пустым'
            : this.inpNameTeam.hasError('maxlength')
                ? 'Название должно быть не более 100 символов'
                : '';
    }
    getInpMemberCountTeamErrorMessage() {
        return this.inpMemberCountTeam.hasError('required')
            ? 'Поле не должно быть пустым'
            : this.inpMemberCountTeam.hasError('min')
                ? 'Кол-во участников должно быть больше 0'
                : this.inpMemberCountTeam.hasError('max')
                    ? 'Кол-во участников должно быть не больше 20'
                    : '';
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

    constructor(private fb: FormBuilder, private dateAdapter: DateAdapter<Date>, private registerCommandService: RegisterCommandService) {
        this.optionsMain = this.fb.group({
            color: 'primary',
            fontSize: [16, Validators.min(10)]
        });
        this.optionsCoach = this.fb.group({
            color: 'primary',
            fontSize: [16, Validators.min(10)],
            picker: { disabled: true, value: '' }
        });
        this.optionsMember = this.fb.group({
            color: 'primary',
            fontSize: [16, Validators.min(10)]
        });
        this.dateAdapter.setLocale('ru');
    }

    ngOnInit() {
        this.command = new Command();
        this.registerCommandService.getCommandForCurrentUser().subscribe(
            response => {
                this.command = response;
                console.log(response);
            },
            err => {
                console.log(err);
            }
        );
        this.currentCoach = new CommandCoach();
        this.currentMember = new CommandMember();
        this.currentCommandRequest = new CommandRequest();
        this.openTab(1);
    }

    getNormilizedName(count: number) {
        if (count % 10 === 1 && count % 100 !== 11) {
            return ' участник';
        }
        if ((count % 10 === 2 || count % 10 === 3 || count % 10 === 4) && count % 100 !== 12 && count % 100 !== 13 && count % 100 !== 14) {
            return ' участника';
        }
        return ' участников';
    }

    resetMainForm() {
        this.inpRegionTeam = new FormControl('', [Validators.required]);
        this.inpNameTeam = new FormControl('', [Validators.required, Validators.maxLength(100)]);
        this.inpCategoryTeam = new FormControl('', [Validators.required]);
        this.inpNominationTeam = new FormControl('', [Validators.required]);
        this.inpMemberCountTeam = new FormControl('', [Validators.required, Validators.min(1), Validators.max(20)]);
        this.phone = new FormControl('', [Validators.required, Validators.pattern('^\\d{10}$')]);
        this.email = new FormControl('', [Validators.required, Validators.email]);
        this.resetCoachForm();
        this.resetMemberForm();
    }
    resetCoachForm() {
        this.inpDateCoach = new FormControl('', [Validators.required]);
        this.inpNameCoach = new FormControl('', [Validators.required, Validators.maxLength(100)]);
        this.passSeries = new FormControl('', [Validators.pattern('^\\d*$'), Validators.minLength(4), Validators.maxLength(4)]);
        this.passNumber = new FormControl('', [Validators.pattern('^\\d*$'), Validators.minLength(6), Validators.maxLength(6)]);
        this.inpPassDescCoach = new FormControl('', [Validators.maxLength(100)]);
    }

    resetMemberForm() {
        this.inpDateMember = new FormControl('', [Validators.required]);
        this.inpNameMember = new FormControl('', [Validators.required, Validators.maxLength(100)]);
        this.passSeriesMember = new FormControl('', [Validators.pattern('^\\d*$'), Validators.minLength(4), Validators.maxLength(4)]);
        this.passNumberMember = new FormControl('', [Validators.pattern('^\\d*$'), Validators.minLength(6), Validators.maxLength(6)]);
        this.quality = new FormControl('', [Validators.required]);
        this.inpPassDescMember = new FormControl('', [Validators.maxLength(100)]);
        this.birthCertificateDesc = new FormControl('', [Validators.maxLength(100)]);
        this.birthCertificateNumber = new FormControl('', [Validators.maxLength(100), Validators.pattern('^\\d*$')]);
    }

    addCommandRequest() {
        const newRequest = new CommandRequest();
        newRequest.name = this.currentCommandRequest.name;
        newRequest.nomination = this.currentCommandRequest.nomination;
        newRequest.ageCategory = this.currentCommandRequest.ageCategory;
        newRequest.coaches = this.currentCommandRequest.coaches;
        newRequest.members = this.currentCommandRequest.members;
        newRequest.music = this.currentCommandRequest.music;
        this.command.requests.push(newRequest);
        this.currentCommandRequest = new CommandRequest();
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

    addMember() {
        this.resetMemberForm();

        const newMember = new CommandMember();
        newMember.name = this.currentMember.name;
        newMember.birthDate = this.currentMember.birthDate;
        newMember.passportDesc = this.currentMember.passportDesc;
        newMember.passportNumber = this.currentMember.passportNumber;
        newMember.passportSeries = this.currentMember.passportSeries;
        newMember.birthCertificateNumber = this.currentMember.birthCertificateNumber;
        newMember.birthCertificateDesc = this.currentMember.birthCertificateDesc;
        newMember.quality = this.currentMember.quality;
        newMember.gender = this.currentMember.gender;
        this.command.members.push(newMember);
        this.currentMember = new CommandMember();
    }

    removeCoach(index: number) {
        this.command.coaches.splice(index, 1);
    }

    removeMember(index: number) {
        this.command.members.splice(index, 1);
    }

    removeRequest(index: number) {
        this.command.requests.splice(index, 1);
    }

    isOk: boolean;

    flushCommand() {
        this.registerCommandService.update(this.command).subscribe(
            () => {
                this.isOk = true;
            },
            () => {
                this.isOk = false;
            }
        );
    }

    getCommands() {
        this.registerCommandService.getCommands().subscribe(
            res => {
                console.log(res);
            },
            () => {
                this.isOk = false;
            }
        );
    }

    getCountOfMembersByNomination(nomination: string) {
        if (nomination === 'Индивидуальные') {
            return ' одного участника';
        }
        if (nomination === 'Смешанные пары') {
            return ' двух участников';
        }
        if (nomination === 'Трио') {
            return ' трех участников';
        }
        return ' несколько участников';
    }

    getNominationError(nomination: string) {
        if (nomination === 'Индивидуальные') {
            return 'Должен быть добавлен 1 участник';
        }
        if (nomination === 'Смешанные пары') {
            return 'Должно быть добавлено 2 участника';
        }
        if (nomination === 'Трио') {
            return 'Должно быть добавлено 3 участника';
        }
        return 'Должно быть добавлено более 3 участников';
    }

    isMembersAddedCorrect(nomination: string, countAdded: number) {
        if (nomination === 'Индивидуальные') {
            return countAdded === 1;
        }
        if (nomination === 'Смешанные пары') {
            return countAdded === 2;
        }
        if (nomination === 'Трио') {
            return countAdded === 3;
        }
        return countAdded > 3;
    }

    isMemberInLimitRequest(addedMembers: CommandMember[]) {
        if (addedMembers.length === 0 || this.command.requests.length < 3) {
            return true;
        }
        console.log(addedMembers);
        return (
            addedMembers.filter(addedMember => {
                return (
                    this.command.requests.filter(request => {
                        return (
                            request.members.filter(mem => {
                                return mem.name === addedMember.name;
                            }).length > 1
                        );
                    }).length > 3
                );
            }).length > 1
        );
    }

    getErrorMember(addedMembers: CommandMember[]) {
        return addedMembers.find(addedMember => {
            return (
                this.command.requests.filter(request => {
                    return (
                        request.members.filter(mem => {
                            return mem.name === addedMember.name;
                        }).length > 1
                    );
                }).length > 3
            );
        });
    }

    foundErrorMember: CommandMember;

    onSelectionMember(event: MatSelectionListChange) {
        if (event.option.selected) {
            this.currentCommandRequest.members.push(event.option.value);
        } else {
            const index = this.currentCommandRequest.members.findIndex(elem => {
                return JSON.stringify(elem) === JSON.stringify(event.option.value);
            });
            this.currentCommandRequest.members.splice(index, 1);
        }

        this.foundErrorMember = null;
        if (this.command.requests.length > 1) {
            this.foundErrorMember = this.currentCommandRequest.members.find(addedMember => {
                return (
                    this.command.requests.filter(request => {
                        return (
                            request.members.filter(mem => {
                                return JSON.stringify(mem) === JSON.stringify(addedMember);
                            }).length > 1
                        );
                    }).length > 3
                );
            });
        }
    }
    onSelectionCoach(event: MatSelectionListChange) {
        if (event.option.selected) {
            this.currentCommandRequest.coaches.push(event.option.value);
        } else {
            const index = this.currentCommandRequest.coaches.findIndex(elem => {
                return JSON.stringify(elem) === JSON.stringify(event.option.value);
            });
            this.currentCommandRequest.coaches.splice(index, 1);
        }
    }

    isMemsListEmpty(mems) {
        return mems.options && mems.options.length === 0;
    }

    openTab(index) {
        document.getElementById('tabContent_1').style.display = 'none';
        document.getElementById('tabContent_2').style.display = 'none';
        document.getElementById('tabContent_3').style.display = 'none';
        document.getElementById('tabContent_4').style.display = 'none';
        const tab1 = document.getElementById('tab1');
        if (tab1.classList.contains('active')) {
            tab1.classList.remove('active');
            if (index === 1) {
                tab1.classList.add('active');
            }
        } else if (index === 1) {
            tab1.classList.add('active');
        }
        const tab2 = document.getElementById('tab2');
        if (tab2.classList.contains('active')) {
            tab2.classList.remove('active');
            if (index === 2) {
                tab2.classList.add('active');
            }
        } else if (index === 2) {
            tab2.classList.add('active');
        }
        const tab3 = document.getElementById('tab3');
        if (tab3.classList.contains('active')) {
            tab3.classList.remove('active');
            if (index === 3) {
                tab3.classList.add('active');
            }
        } else if (index === 3) {
            tab3.classList.add('active');
        }
        const tab4 = document.getElementById('tab4');
        if (tab4.classList.contains('active')) {
            tab4.classList.remove('active');
            if (index === 4) {
                tab4.classList.add('active');
            }
        } else if (index === 4) {
            tab4.classList.add('active');
        }
        document.getElementById(`tabContent_${index}`).style.display = 'block';
    }

    selectEvent(file: File): void {
        this.currentCommandRequest.music = file;
    }

    uploadEvent(file: File): void {}

    cancelEvent(): void {
        this.currentCommandRequest.music = null;
    }

    getFontSize() {
        return Math.max(10, this.optionsMain.value.fontSize);
    }
}
