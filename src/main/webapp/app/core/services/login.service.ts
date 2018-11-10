import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NgxPermissionsService } from 'ngx-permissions';
import { Observable } from 'rxjs/Observable';
import { map, take, tap } from 'rxjs/operators';

/**
 * Service communication with http server https://backend-jscamp.saritasa-hosting.com/api/auth.
 */
@Injectable({
  providedIn: 'root',
})
export class LoginService {

  /**
   * @param httpClient standard angular http communication class.
   * @param permissionService that allows you to manipulate permission.
   */
  constructor(private httpClient: HttpClient, private permissionService: NgxPermissionsService) {
  }

  /**
   * Send password and email and if they are correct save token in Local Storage and load a client role.
   *
   * @param email entered by the user.
   * @param password entered by the user.
   */
  public login(email: string, password: string): Observable<any> {
    return this.httpClient.post('https://backend-jscamp.saritasa-hosting.com/api/auth', { email, password }).pipe(
      tap(response => this.setSession(response.token)),
    );
  }

  /**
   * Save token in Local Storage and load a client role.
   *
   * @param token authentication.
   */
  private setSession(token: string): void {
    localStorage.setItem('token', token);
    this.getRole().then(
      role => this.permissionService.addPermission(role),
      () => this.permissionService.addPermission('GUEST'),
    );
  }

  /**
   * Get the  logged client role.
   */
  public getRole(): Promise<any> {
    return this.httpClient.get('https://backend-jscamp.saritasa-hosting.com/api/profile').pipe(
      map(({ role }: any) => role.name.toUpperCase()),
      take(1),
    ).toPromise();
  }
}
