import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';
import {Doctor} from '../models/doctor';

@Injectable({
  providedIn: 'root',
})
export class DoctorsService {

  constructor() {
  }

  public getDocotrs(): Observable<Doctor[]> {
    const doctors = [
      new Doctor({
        id: 100,
        name: 'Анна Сергеевна Ляпика',
        position: 'Терапевт',
        description: 'Заболевания почек и мочевыводительной системы, желудочно-кишечного тракта, сердечно-сосудистой и бронхо-легочной систем, гериатрия, восстановительное лечение, профилактическое консультирование',
        image: 'https://myblue.bluecrossma.com/sites/g/files/csphws1086/files/inline-images/Doctor%20Image%20Desktop.png',
      }),
        new Doctor({
            id: 200,
            name: 'Игорь василич вязменский',
            position: 'Проктолог',
            description: 'Лечение заболеваний, которые могут быть излечены в амбулаторных условиях; ведением беременности у женщины, патронажами к новорожденному; ведение детей, измерение их антропомотрических показателей',
            image: 'https://myblue.bluecrossma.com/sites/g/files/csphws1086/files/inline-images/Doctor%20Image%20Desktop.png',
        }),
        new Doctor({
            id: 300,
            name: 'Валерий Семенович Штольц',
            position: 'Дантист',
            description: 'Проведение объективного исследования больного по всем органам и системам, выявление общих и специфических признаков заболевания; оценка тяжести состояния больного; обоснование и формулировка  клинического диагноза, проведение дифференциальной диагностики, определение плана и тактики ведения больного; назначение медикаментозного и других методов лечения; оценка трудоспособности больного',
            image: 'https://myblue.bluecrossma.com/sites/g/files/csphws1086/files/inline-images/Doctor%20Image%20Desktop.png',
        })
    ];
    return Observable.of(doctors);
  }
}
