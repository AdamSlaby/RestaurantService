export interface ReservationInfo {
  id: number;
  tableIds: number[];
  name: string;
  surname: string;
  email: string;
  phoneNr: string;
  fromHour: Date;
  toHour: Date;
}
