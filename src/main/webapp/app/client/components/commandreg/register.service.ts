import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { Command } from 'app/client/components/commandreg/commandReg.component';
import { DonwloadFileRequest } from 'app/client/components/table/table.component';

@Injectable({ providedIn: 'root' })
export class RegisterCommandService {
    constructor(private http: HttpClient) {}

    update(command: Command): Observable<any> {
        return this.http.post(SERVER_API_URL + 'api/updateCommand', command);
    }

    saveFileToRequest(file: File, requestId: string, requestName: string, musicFileName: string): Observable<any> {
        let formdata: FormData = new FormData();

        formdata.append('file', file);
        formdata.append('requestId', requestId);
        formdata.append('requestName', requestName);
        formdata.append('musicFileName', musicFileName);
        return this.http.post(SERVER_API_URL + 'api/saveFileToRequest', formdata);
    }

    getCommands(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/getCommands');
    }

    getCommandForCurrentUser(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/getCommandForCurrentUser');
    }

    getAllRequests(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/getAllRequests');
    }

    getAllCommandUserInfo(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/getAllCommandUserInfo');
    }

    getRequestInfo(requestId: number): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/getRequestInfo?requestId=' + requestId);
    }

    getCommand(commandId: number): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/getCommand?commandId=' + commandId);
    }

    saveDownloadedMusicFile(donwloadFileRequest: DonwloadFileRequest): Observable<any> {
        return this.http.post(SERVER_API_URL + 'api/saveDownloadedMusicFile', donwloadFileRequest);
    }

    downloadFileMusic(donwloadFileRequest: DonwloadFileRequest): Observable<any> {
        return this.http.post(SERVER_API_URL + 'api/downloadFileMusic', donwloadFileRequest);
    }
}
