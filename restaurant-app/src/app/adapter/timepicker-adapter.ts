import {Injectable} from '@angular/core';
import {NgbTimeAdapter, NgbTimeStruct} from '@ng-bootstrap/ng-bootstrap';
import {TimeUtility} from "../utility/time-utility";

@Injectable()
export class NgbTimeDateAdapter extends NgbTimeAdapter<Date> {

  fromModel(value: Date | null): NgbTimeStruct | null {
    if (!value) {
      return null;
    }
    return {
      hour: value.getHours(),
      minute: value.getMinutes(),
      second: value.getSeconds(),
    } as NgbTimeStruct;
  }

  toModel(time: NgbTimeStruct | null): Date | null {
    return time != null ? TimeUtility.mapNgbTimeStructToDate(time) : null;
  }
}
