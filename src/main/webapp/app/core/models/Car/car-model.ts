/**
 *  Model  car.
 */
export class CarModel {
  /**
   * ID.
   */
  public id: number;
  /**
   * Name.
   */
  public name: string;
  /**
   * ID make who produces this Car Model.
   */
  public makeId: number;
  /**
   * Init car model.
   */
  constructor(id?: number, name?: string, makeId?: number) {
    this.id = id;
    this.name = name;
    this.makeId = makeId;
  }
}
