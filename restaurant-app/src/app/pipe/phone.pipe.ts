import { Pipe, PipeTransform } from '@angular/core';
import {CountryCode, parsePhoneNumber} from 'libphonenumber-js';

@Pipe({
  name: 'phone'
})
export class PhonePipe implements PipeTransform {
  transform(phoneNr: string, country: string): unknown {
    try {
      const phoneNumber = parsePhoneNumber(phoneNr + '', country as CountryCode);
      return phoneNumber.formatNational();
    } catch (error) {
      return phoneNr;
    }
  }
}
