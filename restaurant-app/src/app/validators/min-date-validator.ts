import {AbstractControl, ValidationErrors, ValidatorFn} from "@angular/forms";
import {NgbCalendar} from "@ng-bootstrap/ng-bootstrap";

export function minDateValidator(calendar: NgbCalendar): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value)
      return {invalidDate: true};
    let today = calendar.getToday();
    let isInvalid = today.after(control.value);
    return isInvalid ? {invalidDate: true} : null;
  };
}
