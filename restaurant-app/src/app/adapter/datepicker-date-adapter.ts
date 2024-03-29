import {Injectable} from '@angular/core';
import {NgbDateAdapter, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {DateUtility} from "../utility/date-utility";

@Injectable()
export class NgbDateToDateAdapter extends NgbDateAdapter<Date> {

  fromModel(value: Date | null): NgbDateStruct | null {
    if (!value) {
      return null;
    }
    if (!(value instanceof Date)) 
      return value;
    return {
      year: value.getFullYear(),
      month: value.getMonth() + 1,
      day: value.getDate(),
    } as NgbDateStruct;
  }

  toModel(date: NgbDateStruct | null): Date | null {
    return date != null ? new Date(DateUtility.mapNgbDateStructToDateString(date)) : null;
  }
}
