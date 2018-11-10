export class Payment {
  public bankcardNumber: number;
  public holder: string;
  public date: string;
  public cvc: number;
  public numberTicket: string;
  public amount: number;

  constructor(payment?: Partial<Payment>) {
  }
}
