import { TransportType } from '../../../shared/models/transport-type';

export interface TransportRequest {
  type: TransportType;
  carrier: string;
  departureLocation: string;
  departureAddress: string;
  arrivalLocation: string;
  arrivalAddress: string;
  departureDateTime: string;
  arrivalDateTime: string;
  bookingNumber: string;
  price: number;
  notes: string;
}
