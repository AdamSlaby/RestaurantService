import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'day'
})
export class DayPipe implements PipeTransform {

  transform(dayNr: number, ...args: unknown[]): unknown {
    switch (dayNr) {
      case 1: return 'Poniedziałek';
      case 2: return 'Wtorek';
      case 3: return 'Środa';
      case 4: return 'Czwartek';
      case 5: return 'Piątek';
      case 6: return 'Sobota';
      case 7: return 'Niedziela';
      default: throw new Error('Invalid day number');
    }
  }

}
