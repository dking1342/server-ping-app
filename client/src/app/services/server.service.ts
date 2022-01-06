import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { Status } from '../enums/status';
import { CustomResponse } from '../interfaces/custom-response';
import { Server } from '../interfaces/server';

@Injectable({
  providedIn: 'root'
})
export class ServerService {

  private readonly apiUrl = "http://localhost:8080";

  constructor(
    private http: HttpClient,
  ) { }

  // get all servers- procedural way
  // getServers():Observable<CustomResponse>{
  //   return this.http.get<CustomResponse>('http://localhost:8080/server/list')
  // }

  // using reactive way
  servers$ = <Observable<CustomResponse>>this.http
    .get<CustomResponse>(`${this.apiUrl}/server/list`)
    .pipe(
      tap(console.log),
      catchError(this.handleError)
    );

  save$ = (server: Server) => <Observable<CustomResponse>>this.http
    .post<CustomResponse>(`${this.apiUrl}/server/save`,server)
    .pipe(
      tap(console.log),
      catchError(this.handleError)
    );

  ping$ = (ipAddress: string) => <Observable<CustomResponse>>this.http
    .get<CustomResponse>(`${this.apiUrl}/server/ping/${ipAddress}`)
    .pipe(
      tap(console.log),
      catchError(this.handleError)
    );

  filter$ = (status: Status, response: CustomResponse) => <Observable<CustomResponse>>
    new Observable<CustomResponse>(
      subscriber => {
        console.log(response);
        subscriber.next(
          status === Status.ALL 
            ? { 
                ...response, 
                message: `Server filtered by ${status} status`
              } 
            : {
              ...response, 
              message: 
                Array.isArray(response.data) && response.data.filter(server => server.status === status).length > 0 
                ? `Servers filtered by ${status === Status.SERVER_UP ? "SERVER UP" : "SERVER DOWN"} status` 
                :  `No servers of ${status} found`,
              data: Array.isArray(response.data) && response.data.filter(server => server.status === status).length > 0
                ? response.data.filter(server => server.status === status)
                : response.data
            }
        );
        subscriber.complete();
      }
    )
    .pipe(
      tap(console.log),
      catchError(this.handleError)
    );

  delete$ = (serverId:number) => <Observable<CustomResponse>>this.http
    .delete<CustomResponse>(`${this.apiUrl}/server/delete/${serverId}`)
    .pipe(
      tap(console.log),
      catchError(this.handleError)
    );

  private handleError(handleError: HttpErrorResponse): Observable<never> {
    console.log(handleError);
    return throwError(() => new Error(`Error code: ${handleError.status}`));
  };

}
