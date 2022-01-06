import { Server } from "./server";

export interface CustomResponse {
    timestamp: Date;
    statusCode: number;
    status: string;
    reason:string;
    message:string;
    developerMessage:string;
    data: Server[];
}
