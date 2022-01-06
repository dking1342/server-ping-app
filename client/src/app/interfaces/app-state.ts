import { State } from "../enums/state";

export interface AppState<T> {
    dataState : State;
    appData?: T;
    error?: string;
}
