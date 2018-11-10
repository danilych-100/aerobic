import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

/**
 * Guard store logic prohibition and permission following a link.
 */
@Injectable({
  providedIn: 'root',
})
export class EnsureAuthenticatedGuard implements CanActivate {

  /**
   * @param router for the opportunity navigation.
   */
  constructor(private router: Router) {
  }

  /**
   * Method is started when try to navigate to the component with this guard.
   *
   * @return prohibition and permission following a link depending on the has token in Local Storage.
   */
  public canActivate(): boolean {
    if (localStorage.getItem('token')) {
      return true;
    } else {
      this.router.navigateByUrl('/login');
      return false;
    }
  }
}
