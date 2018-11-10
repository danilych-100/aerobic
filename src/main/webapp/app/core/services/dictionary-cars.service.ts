import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { concat, map, retryWhen, shareReplay, switchMap, take } from 'rxjs/operators';

import { RecordDictionaryFeatureCar } from '../models/Car/record-dictionary-feature-car';

/**
 * Service communication with http server https://backend-jscamp.saritasa-hosting.com/api/dictionaries.
 */
@Injectable({
  providedIn: 'root',
})
export class DictionaryCarsService {
  private cacheMake$: Observable<RecordDictionaryFeatureCar[]>;
  private cacheBodyType$: Observable<RecordDictionaryFeatureCar[]>;

  /**
   * @param httpClient Servise with http communication.
   */
  constructor(private httpClient: HttpClient) {
  }

  /**
   * Get observable with dictionary make from  can the cache.
   * Cache decided not yet how not to update.
   *
   * @return Observable with  List Make(id,name).
   */
  public getMakes(): Observable<RecordDictionaryFeatureCar[]> {
    if (!this.cacheMake$) {
      this.cacheMake$ = this.httpClient
        .get<RecordDictionaryFeatureCar[]>('https://backend-jscamp.saritasa-hosting.com/api/dictionaries/makes')
        .pipe(
          map((response: any) => response.results),
          shareReplay(1),
        )
      ;
    }

    return this.cacheMake$;
  }

  /**
   * Get observable with dictionary( body type from  can the cache.
   * Cache decided not yet how not to update.
   *
   * @return Observable with  Body Type(id,name).
   */
  public getBodyTypes(): Observable<RecordDictionaryFeatureCar[]> {
    if (!this.cacheBodyType$) {
      this.cacheBodyType$ = this.httpClient
        .get<RecordDictionaryFeatureCar[]>('https://backend-jscamp.saritasa-hosting.com/api/dictionaries/body-types')
        .pipe(
          map((response: any) => response.results),
          shareReplay(1),
        );
    }

    return this.cacheBodyType$;
  }

  /**
   * Get observable with dictionary model.
   *
   * @param idMake loads the models of the given Make.
   * @return Observable with  Model(id,name).
   */
  public getModels(idMake: number): Observable<RecordDictionaryFeatureCar[]> {
    return this.httpClient
      .get<RecordDictionaryFeatureCar[]>(`https://backend-jscamp.saritasa-hosting.com/api/dictionaries/makes/${idMake}/models`)
      .pipe(
        map((response: any) => response.results),
      );
  }
}
