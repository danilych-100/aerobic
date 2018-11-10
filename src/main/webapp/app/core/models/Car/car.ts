import { BodyType } from './body-type';
import { CarModel } from './car-model';
import { Make } from './make';

/**
 *  Car model displayed in the table.
 */
export class Car {
  /**
   * ID car.
   */
  public id: number;

  /**
   * Year production car.
   */
  public year: number;
  /**
   * Mileage car.
   */
  public mileage: number;
  /**
   * Description car.
   */
  public description: string;

  /**
   * Date of creation of information about  car.
   */
  public created: Date;

  /**
   * Date  updated of information about  car.
   */
  public updated: Date;
  /**
   * carModel car.
   */
  public model: CarModel;
  /**
   * bodyType car.
   */
  public bodyType: BodyType;

  /**
   * make car.
   */
  public make: Make;
  /**
   * Init car.
   */
  carModel: any
  constructor() {
    this.make = new Make();
    this.bodyType = new BodyType();
    this.model = new CarModel();
  }
}
