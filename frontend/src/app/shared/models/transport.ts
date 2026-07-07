export interface Transport {
  id: number;
  type: string;
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
