/**
 * Pagination  table car.
 */
export class Pagination {

  /**
   * Total number item(cars) in the table.
   */
  public total: number;
  /**
   * Total amount available now on the page items(car) .
   */
  public count: number;
  /**
   * Total amount on the page items(car) .
   */
  public perPage: number;
  /**
   * Get current page in the table.
   */
  public currentPage: number;
  /**
   * Total number of pages in the table.
   */
  public totalPages: number;
  links: any;
}
