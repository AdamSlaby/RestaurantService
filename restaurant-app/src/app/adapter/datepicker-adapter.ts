import {Injectable} from '@angular/core';
import {NgbDateAdapter, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {DateUtility} from "../utility/date-utility";

@Injectable()
export class NgbDateToStringAdapter extends NgbDateAdapter<String> {

  fromModel(value: string | null): NgbDateStruct | null {
    if (!value) {
      return null;
    }
    let date = new Date(value);
    return {
      year: date.getFullYear(),
      month: date.getMonth(),
      day: date.getDate(),
    } as NgbDateStruct;
  }

  toModel(date: NgbDateStruct | null): string | null {
    return date != null ? DateUtility.mapNgbDateStructToDate(date) : null;
  }
}
