import {NgbTimeStruct} from "@ng-bootstrap/ng-bootstrap";

export class TimeUtility {
  public static getUtcDAte(date: Date): number {
    return Date.UTC(date.getFullYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes());
  }

  public static mapNgbTimeStructToDate(time: NgbTimeStruct): Date {
    let now = new Date();
    return new Date(Date.UTC(now.getFullYear(), now.getMonth(), now.getDate(),
      time.hour, time.minute, time.second));
  }
}
