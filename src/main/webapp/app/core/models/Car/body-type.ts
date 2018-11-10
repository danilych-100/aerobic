/**
 * Body type  car.
 */
export class BodyType {
  /**
   * ID Body-Type.
   */
  public id: number;
  /**
   * Name Body-Type.
   */
  public name: string;
  /**
   * Init Body-Type.
   */
  public constructor(id?: number, name?: string) {
    this.id = id;
    this.name = name;
  }
}
