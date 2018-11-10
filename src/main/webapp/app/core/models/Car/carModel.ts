export class CarModel {
  public id: number;
  public name: string;
  public makeId: number;

  constructor(id: number, name: string, makeId: number) {
    this.id = id;
    this.name = name;
    this.makeId = makeId;
  }
}
