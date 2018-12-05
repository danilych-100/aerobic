import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { Command } from 'app/client/components/commandreg/commandReg.component';

@Injectable({ providedIn: 'root' })
export class RegisterCommandService {
    constructor(private http: HttpClient) {}

    update(command: Command): Observable<any> {
        return this.http.post(SERVER_API_URL + 'api/updateCommand', command);
    }

    getCommands(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/getCommands');
    }

    getCommandForCurrentUser(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/getCommandForCurrentUser');
    }

    getAllCommands(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/getAllCommands');
    }

    getAllRequests(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/getAllRequests');
    }
}
