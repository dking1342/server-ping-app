import { Status } from "../enums/status";

export interface Server {
    id: number;
    ipAddress: string;
    name: string;
    memory: string;
    type: string;
    imageUrl:string;
    status:Status;
}
