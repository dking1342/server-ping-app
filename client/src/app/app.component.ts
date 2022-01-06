import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { BehaviorSubject, catchError, map, Observable, of, startWith } from 'rxjs';
import { State } from './enums/state';
import { Status } from './enums/status';
import { AppState } from './interfaces/app-state';
import { CustomResponse } from './interfaces/custom-response';
import { Server } from './interfaces/server';
import { ServerService } from './services/server.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  
  public appState$!: Observable<AppState<CustomResponse>>;
  readonly DataState = State;
  readonly Status = Status;
  private filterSubject = new BehaviorSubject<string>('');
  filterStatus$ = this.filterSubject.asObservable();
  private dataSubject = new BehaviorSubject<CustomResponse>({
    timestamp: new Date(),
    statusCode: 0,
    status: '',
    reason:'',
    message:'',
    developerMessage:'',
    data: []
  });
  private isLoading = new BehaviorSubject<boolean>(false);
  isLoading$ = this.isLoading.asObservable();
  
  constructor(
    private serverService: ServerService,
  ) {}

  ngOnInit():void {
    this.appState$ = 
      this.serverService.servers$
        .pipe(
          map(res => {
            this.dataSubject.next(res);
            return { 
              dataState: State.LOADED,
              appData: {
                ...res,
                data: res.data.reverse()
              }
            }
          }),
          startWith({
            dataState: State.LOADING
          }),
          catchError((error: string) => {
            return of({
              dataState: State.ERROR,
              error
            })
          })
        );
  }

  pingServer(ipAddress:string):void{
    this.filterSubject.next(ipAddress);
    this.appState$ = this.serverService.ping$(ipAddress)
      .pipe(
        map(res => {
          this.dataSubject.value.data[
            this.dataSubject.value.data.findIndex(server => server.id === res.data[0].id )
          ] = res.data[0];
          this.filterSubject.next('');
          return { 
            dataState: State.LOADED,
            appData: this.dataSubject.value
          }
        }),
        startWith({
          dataState: State.LOADED,
          appData: this.dataSubject.value
        }),
        catchError((error: string) => {
          this.filterSubject.next('');
          return of({
            dataState: State.ERROR,
            error
          })
        })
      )
  }

  saveServer(serverForm:NgForm):void{
    this.isLoading.next(true);
    this.appState$ = this.serverService.save$(<Server>serverForm.value)
      .pipe(
        map(res => {
          this.dataSubject.next(
            {
              ...res,
              data:[...res.data, ...this.dataSubject.value.data]
            }
          );
          document.getElementById("closeModal")?.click();
          this.isLoading.next(false);
          serverForm.resetForm({ status:this.Status.SERVER_DOWN});
          return { 
            dataState: State.LOADED,
            appData: this.dataSubject.value
          }
        }),
        startWith({
          dataState: State.LOADED,
          appData: this.dataSubject.value
        }),
        catchError((error: string) => {
          this.isLoading.next(false);
          document.getElementById("closeModal")?.click();
          return of({
            dataState: State.ERROR,
            error
          })
        })
      )
  }

  filterServers(status:any):void{
    this.appState$ = this.serverService.filter$(status.value,this.dataSubject.value)
      .pipe(
        map(res => {
          return { 
            dataState: State.LOADED,
            appData: res
          }
        }),
        startWith({
          dataState: State.LOADED,
          appData: this.dataSubject.value
        }),
        catchError((error: string) => {
          return of({
            dataState: State.ERROR,
            error
          })
        })
      )
  }  

  deleteServer(serverId:number):void{
    this.appState$ = this.serverService.delete$(serverId)
      .pipe(
        map(res => {
          this.dataSubject.next(
            {
              ...res,
              data: this.dataSubject.value.data.filter(s => s.id !== serverId)
            }
          );
          return { 
            dataState: State.LOADED,
            appData: this.dataSubject.value
          }
        }),
        startWith({
          dataState: State.LOADED,
          appData: this.dataSubject.value
        }),
        catchError((error: string) => {
          return of({
            dataState: State.ERROR,
            error
          })
        })
      )
  }  
}
