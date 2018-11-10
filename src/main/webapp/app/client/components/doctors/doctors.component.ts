import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DoctorsService} from '../../../core/services/doctors.service';
import {Observable} from 'rxjs/Rx';
import {Doctor} from '../../../core/models/doctor';

@Component({
  selector: 'app-doctors',
  templateUrl: './doctors.component.html',
  styleUrls: ['./doctors.component.scss'],
})
export class DoctorsComponent implements OnInit {

  @Input()
  public enableActions: boolean;
  doctors: Observable<Doctor[]>;
  @Output()
  public choseDoctor: EventEmitter<Doctor> = new EventEmitter<Doctor>();

  constructor(private doctorsService: DoctorsService) {
  }

  ngOnInit() {
    this.doctors = this.doctorsService.getDocotrs();
  }

  onChoseDoctor(doctor: Doctor) {
    this.choseDoctor.emit(doctor);
  }
}
