import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Payment} from '../../../core/models/payment';

@Component({
  selector: 'app-bank-card',
  templateUrl: './bank-card.component.html',
  styleUrls: ['./bank-card.component.scss'],
})
export class BankCardComponent implements OnInit {
  public payment: Payment = new Payment();
  @Output()
  public onPay = new EventEmitter<Payment>();

  constructor() {
  }

  ngOnInit() {
  }

  public onSubmit() {
    this.onPay.emit(this.payment);
  }

}
