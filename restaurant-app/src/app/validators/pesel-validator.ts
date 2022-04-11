import {AbstractControl, ValidationErrors, ValidatorFn} from "@angular/forms";

export function personIdValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value)
      return {invalidPersonId: true};
    let wages: number[] = [1, 3, 7, 9, 1, 3, 7, 9, 1, 3];
    let pesel = control.value;
    let sum = 0;
    for (let i = 0; i < wages.length; i++)
      sum += Number.parseInt(pesel[i]) * wages[i];
    sum %= 10;
    let isInvalid = Number.parseInt(pesel[10]) !== (10 - sum);
    return isInvalid ? {invalidPersonId: true} : null;
  };
}
