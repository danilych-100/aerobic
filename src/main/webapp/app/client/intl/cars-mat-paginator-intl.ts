import { Injectable } from '@angular/core';
import {MatPaginatorIntl} from '@angular/material';

/**
 * Replace the tips of the paginator.
 */

@Injectable()
  export class CarsMatPaginatorIntl extends MatPaginatorIntl {
  /**
   * Next page hint in paginator .
   */
  public nextPageLabel = 'Next Cars';
  /**
   * Previous page hint in paginator .
   */
  public previousPageLabel = 'Previous Cars';
}
