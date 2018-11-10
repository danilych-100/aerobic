/**
 * Make  car.
 */
export class Make {
  /**
   * ID make.
   */
  public id: number;
  /**
   * Name make.
   */
  public name: string;

  /**
   * Init make.
   */
  constructor(make: { id?: number, name?: string } = {}) {
    this.id = make.id;
    this.name = make.name;
  }
}
