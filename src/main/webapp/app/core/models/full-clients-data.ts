import {Doctor} from './doctor';
import {Client} from './client';
import {Record} from './record';
import {Payment} from './payment';

export class FullClientsData {
  public doctor: Doctor;
  public client: Client;
  public record: Record;
  public payment: Payment;
  public date: Date;
  constructor(){
    this.date = new Date();
    this.record = new Record();
    this.client = new Client({});
    this.doctor = new Doctor({});
  }
}
