import {Address} from "./address";

export interface Employee {
  name: string;
  surname: string;
  workstationId: number;
  phoneNr: string;
  pesel: string;
  accountNr: string;
  salary: number;
  active: boolean;
  employmentDate: Date | any;
  dismissalDate: Date | any;
  address: Address;
  restaurantId: number;
}
