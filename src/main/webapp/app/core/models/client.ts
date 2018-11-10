export class Client {
  id: number;
  time: Date;
  doctor: string;
  name: string;
  birthDay: Date;
  address: string;
  police: number;
  numberTicket: string;
  cost: number;

  constructor(client: Partial<Client>) {
    this.id = client.id;
    this.time = client.time;
    this.doctor = client.doctor;
    this.name = client.name;
    this.birthDay = client.birthDay;
    this.address = client.address;
    this.police = client.police;
    this.numberTicket = client.numberTicket;
    this.cost = client.cost;
  }
}
