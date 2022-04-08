import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'bankAccount'
})
export class BankAccountPipe implements PipeTransform {
  transform(accountNr: string): string {
    let resultAccount = accountNr.replaceAll(' ', '');
    for (let i = 2; i < resultAccount.length; i += 4) {
      resultAccount = resultAccount.slice(0, i) + ' ' + resultAccount.slice(i);
      i++;
    }
    return resultAccount;
  }
}
