import {Component, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {NgForm} from '@angular/forms';
import {Car} from '../../../core/models/Car/car';
import {Observable} from 'rxjs/Rx';
import {RecordDictionaryFeatureCar} from '../../../core/models/Car/record-dictionary-feature-car';
import {ActivatedRoute, Router} from '@angular/router';
import {DictionaryCarsService} from '../../../core/services/dictionary-cars.service';
import {CarsService} from '../../../core/services/cars.service';
import {MatDialog, MatDialogConfig, MatDialogRef, MatSelectChange, MatSnackBar} from '@angular/material';
import {DialogDeactivationComponent} from '../dialog-deactivation/dialog-deactivation.component';
import {RecordService} from '../../../core/services/record.service';
import {Record} from '../../../core/models/record';
import {LoginService} from '../../../core/services/login.service';

@Component({
  selector: 'app-make-appointment',
  templateUrl: './make-appointment.component.html',
  styleUrls: ['./make-appointment.component.scss'],
})
export class MakeAppointmentComponent implements OnInit {
  /**
   * Car Form.
   */
  @ViewChild('makeForm')
  public makeForm: NgForm;
  private isFormSubmitted = false;
  /**
   * car which we fill.
   */
  public record: Record = new Record();
  public error: String;

  @Output()
  public submitRecord: EventEmitter<Record> = new EventEmitter<Record>();
  /**
   * @param activatedRoute Angular.
   * @param router Angular.
   * @param dictionaryCarsServise Service for obtaining dictionaries associated with car.
   * @param carsServise Service for sending cars.
   * @param dialog Material.
   * @param snackBar Material.
   */
  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private recordService: RecordService,
              private dialog: MatDialog,
              public snackBar: MatSnackBar) {

  }

  /**
   * Receives a car from the resolver and loads dictionary.
   */
  public ngOnInit(): void {
  }

  /**
   * Send car and depending on the success create error or snack bar.
   */
  public onSubmit(): void {
    this.isFormSubmitted = true;
    this.submitRecord.emit(this.record);
    // this.recordService.saveRecord(this.record).subscribe(
    //   () => {
    //     this.isFormSubmitted = true;
    //     this.snackBar.open('the car is sent');
    //   }, httpErrorResponse => this.error = `${httpErrorResponse.error} status server ${httpErrorResponse.status}`,
    // );
  }

  /**
   * Allows to leave the component if form submitted or users didn`t enter data,else create modal window with confirm navigate.
   */
  public canDeactivate(): Observable<boolean> {
    if (this.error) {
      return Observable.of(true);
    }
    if (this.makeForm.touched && !this.isFormSubmitted) {
      const dialogDeactivate = this.dialog.open(DialogDeactivationComponent, {
        width: '400px',
        height: '350px',
      } as MatDialogConfig<any>);
      return dialogDeactivate.afterClosed().map(
        value => value as boolean,
      );
    }
    return Observable.of(true);
  }

}
