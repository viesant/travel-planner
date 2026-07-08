import { TransportType } from './transport-type';

export interface Transport {
  id: number;
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
  tripId: number;
}
