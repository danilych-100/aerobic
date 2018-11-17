import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AccountService } from 'app/core';

@Injectable({ providedIn: 'root' })
export class AuthGuardService implements CanActivate {
    constructor(private accountService: AccountService, private router: Router) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        let isAuth;
        return this.accountService
            .get()
            .toPromise()
            .then(response => {
                if (response.status == 401) {
                    this.router.navigate(['/'], {
                        queryParams: {
                            return: state.url
                        }
                    });
                    return false;
                }
                return true;
            });
    }
}
