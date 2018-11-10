import {Component, OnInit, ViewChild} from '@angular/core';
import {MatDialogRef, MatStepper} from '@angular/material';
import {BehaviorSubject, Subject} from 'rxjs/Rx';
import {FullClientsData} from '../../../core/models/full-clients-data';
import {Record} from '../../../core/models/record';
import {Doctor} from '../../../core/models/doctor';
import {Payment} from '../../../core/models/payment';

@Component({
  selector: 'app-stepper',
  templateUrl: './stepper.component.html',
  styleUrls: ['./stepper.component.scss'],
})
export class StepperComponent implements OnInit {

  public fullClientData: FullClientsData = new FullClientsData();

  @ViewChild('stepper') stepper: MatStepper;

  /**
   * Current date.
   */
  public dateValue: Date = new Date();
  public timeValue: String = new Date().toLocaleTimeString();

  constructor(private dialogRef: MatDialogRef<StepperComponent>) {
  }

  ngOnInit() {
  }

  submitRecord(record: Record) {
    this.fullClientData.record = record;
    this.stepper.next();
  }

  addDoctor(doctor: Doctor) {
    this.fullClientData.doctor = doctor;
    console.log(this.fullClientData);
    this.stepper.next();
  }

  public onChangeDate({value}): void {
    this.dateValue = value;
  }

  addDate() {
    console.log(this.timeValue);
    this.dateValue.setHours(parseInt(this.timeValue.split(':')[0], 10));
    this.dateValue.setMinutes(parseInt(this.timeValue.split(':')[1], 10));
    this.fullClientData.date = this.dateValue;
    console.log(this.fullClientData);
    this.stepper.next();
  }

  addPayment(payment: Payment) {
    this.fullClientData.payment = payment;
    console.log(this.fullClientData);
  }
}
