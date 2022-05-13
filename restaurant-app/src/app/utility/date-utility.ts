import {NgbDateStruct, NgbTimeStruct} from "@ng-bootstrap/ng-bootstrap";

export class DateUtility {
  public static getUtcDAte(date: Date): number {
    return Date.UTC(date.getFullYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes());
  }

  public static mapNgbTimeStructToDate(time: NgbTimeStruct): Date {
    let now = new Date();
    return new Date(Date.UTC(now.getFullYear(), now.getMonth(), now.getDate(),
      time.hour, time.minute, time.second));
  }

  public static mapNgbDateStructToDateString(date: NgbDateStruct): string {
    return new Date(Date.UTC(date.year, date.month - 1, date.day)).toUTCString();
  }
}
