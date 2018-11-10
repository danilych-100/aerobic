export class Record {
  public nameClient: string;
  public police: Number;
  public birthDay: Date;
  public gender: string;

  constructor(nameClient = '', police = 0, birthDay = new Date(), gender = '') {
    this.nameClient = nameClient;
    this.police = police;
    this.birthDay = birthDay;
    this.gender = gender;
  }
}
