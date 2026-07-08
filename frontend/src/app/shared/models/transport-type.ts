export const TRANSPORT_TYPES = ['FLIGHT', 'BUS', 'TRAIN', 'CAR', 'BOAT', 'OTHER'] as const;

export type TransportType = (typeof TRANSPORT_TYPES)[number];
