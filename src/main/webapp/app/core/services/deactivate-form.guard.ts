import { Injectable } from '@angular/core';
import { CanDeactivate } from '@angular/router';
import {Observable} from 'rxjs/Rx';

/**
 * Interface interface that must implement component with guard and method return may or may not.
 */
export interface CanComponentDeactivate {
  canDeactivate: () => Observable<boolean>;
}
/**
 * Service Deactivate Component.
 */
@Injectable({
  providedIn: 'root',
})
export class DeactivateFormGuard implements CanDeactivate<CanComponentDeactivate> {
  /**
   * The method occurs when the user wants to leave the component.
   *
   * @param component that has a method CanComponentDeactivate from which they want to move.
   * @return observable  with answer canDeactivate or can`t.
   */
  public canDeactivate(component: CanComponentDeactivate): Observable<boolean> {
    return component.canDeactivate();
  }

}
