import { EventEmitter, Injectable } from '@angular/core';
import { NavigationExtras, Router, UrlTree } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class RouterService {

  protected navigateListener = new EventEmitter<any>();

  constructor(private router: Router) {
  }

  public subscribe(generatorOrNext?: any, error?: any, complete?: any) {
    return this.navigateListener.subscribe(generatorOrNext, error, complete);
  }

  public navigate(commands: any[], extras?: NavigationExtras): Promise<boolean> {
    this.navigateListener.next();
    return this.router.navigate(commands, extras);
  }

  public navigateByUrl(url: string | UrlTree, extras?: NavigationExtras): Promise<boolean> {
    this.navigateListener.next();
    return this.router.navigateByUrl(url, extras);
  }

}
