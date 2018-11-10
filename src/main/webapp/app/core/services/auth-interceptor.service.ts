import { HttpClient, HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { catchError, concat, retryWhen, switchMap, take } from 'rxjs/operators';

import { LoginComponent } from '../../client/components/login/login.component';

const NUMBER_OF_RETRIES = 5;

/**
 * Interceptor http request.
 */
@Injectable({
  providedIn: 'root',
})
export class AuthInterceptorService implements HttpInterceptor {

  public constructor(private router: Router, private matDialog: MatDialog, private httpClient: HttpClient) {

  }

  /**
   * Interceptor http request and insert http header authorization if there is a token.
   * Cath http error response and if error status 401 call the dialog and if the user logs in again request.
   */
  public intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let cloned = req.clone();
    const token = localStorage.getItem('token');
    if (token) {
      cloned = req.clone({
        headers: req.headers.set('Authorization', 'Bearer' + token),
      });
    }
    return next.handle(cloned).pipe(
      retryWhen(errors => {
          return errors.pipe(
            switchMap((httpErrorResponse: HttpErrorResponse) => {

              if (httpErrorResponse.status === 503) {
                return Observable.of(true);
              }
              return Observable.throwError(httpErrorResponse);
            }),
            take(NUMBER_OF_RETRIES),
            concat(Observable.throwError(`please wait, server error 503`)),
          );
        },
      ),
      catchError(err => {
          return this.handleHttpErrorResponse(err, cloned);
        },
      ),
    );
  }

  /**
   * Handle http error.
   *
   * @param httpErrorResponse response with an error other than 503.
   * @param requestWithError the request with which the error occurred.
   * @return Observable with a handled error so far only 401.
   */
  public handleHttpErrorResponse(httpErrorResponse: HttpErrorResponse, requestWithError: HttpRequest<any>): Observable<any> {
    if (httpErrorResponse.status === 401) {
      localStorage.removeItem('token');
      const loginDialog = this.matDialog.open(LoginComponent, {
        width: '400px',
        height: '350px',
      } as MatDialogConfig<any>);
      return loginDialog.afterClosed().pipe(
        switchMap(
          loggedOn => {
            if (loggedOn) {
              return this.httpClient.request(requestWithError);
            }
            return Observable.throwError(httpErrorResponse);
          }));
    }
    return Observable.throwError(httpErrorResponse);
  }
}
