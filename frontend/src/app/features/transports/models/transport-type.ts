export const TRANSPORT_TYPES = [
  { value: 'FLIGHT', label: 'Flight', icon: 'flight' },
  { value: 'BUS', label: 'Bus', icon: 'directions_bus' },
  { value: 'TRAIN', label: 'Train', icon: 'directions_transit' },
  { value: 'CAR', label: 'Car Drive', icon: 'directions_car' },
  { value: 'BOAT', label: 'Boat Cruise', icon: 'directions_boat' },
  { value: 'OTHER', label: 'Other', icon: 'explore' },
] as const;

export type TransportType = (typeof TRANSPORT_TYPES)[number]['value'];
