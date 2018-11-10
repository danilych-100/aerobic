import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { Command } from 'app/client/components/commandreg/commandReg.component';

@Injectable({ providedIn: 'root' })
export class RegisterCommandService {
    constructor(private http: HttpClient) {}

    register(command: Command): Observable<any> {
        return this.http.post(SERVER_API_URL + 'api/registerCommand', command);
    }

    getCommands(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/getCommands');
    }
}
