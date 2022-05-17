export interface Reservation {
  restaurantId: any;
  name: string;
  surname: string;
  phoneNr: string;
  email: string;
  fromHour: Date;
  peopleNr: number;
  tableIds: number[];
}
