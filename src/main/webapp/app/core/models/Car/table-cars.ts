import { Pagination } from './pagination';
/**
 * Table data plus pagination table.
 */
export class TableCars<T> {
  /**
   * Table data.
   */
 public items: T[];
  /**
   * Pagination table.
   */
 public pagination: Pagination;

}
