import {Directive, EventEmitter, Input, Output} from '@angular/core';
import {SortDirection} from "../model/sort-direction";
import {SortEvent} from "../model/sort-event";

@Directive({
  selector: 'th[sortable]',
  host: {
    '[class.asc]': 'direction === sortDirection.ASC',
    '[class.desc]': 'direction === sortDirection.DESC',
    '(click)': 'rotate()'
  }
})
export class NgbdSortableHeaderDirective {
  sortDirection = SortDirection;
  @Input() sortable: string = '';
  @Input() direction: SortDirection = SortDirection.EMPTY;
  @Output() sort = new EventEmitter<SortEvent>();

  rotate() {
    this.direction = this.changeDirection();
    this.sort.emit({column: this.sortable, direction: this.direction});
  }

  changeDirection(): SortDirection {
    if (this.direction === SortDirection.ASC)
      return SortDirection.DESC;
    else if (this.direction === SortDirection.DESC)
      return SortDirection.EMPTY;
    else
      return SortDirection.ASC;
  }
}
