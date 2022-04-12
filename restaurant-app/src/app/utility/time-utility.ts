export class TimeUtility {
  public static getUtcDAte(date: Date): number {
    return Date.UTC(date.getFullYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes());
  }
}
