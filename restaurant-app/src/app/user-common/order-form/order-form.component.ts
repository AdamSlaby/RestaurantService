import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-order-form',
  templateUrl: './order-form.component.html',
  styleUrls: ['./order-form.component.scss']
})
export class OrderFormComponent implements OnInit, AfterViewInit {
  errors: Map<string, string> = new Map<string, string>();
  loading = false;
  orderForm = this.fb.group({
    firstName: ['', [Validators.required, Validators.pattern("^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32}$")]],
  });

  constructor(private fb: FormBuilder, private route: ActivatedRoute) { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    this.route.fragment.subscribe(fragment => {
      let orderForm = document.getElementById('orderForm');
      if (orderForm)
        orderForm.scrollIntoView();
    })
  }

  get f() {
    return this.orderForm.controls;
  }

  onSubmit() {
    return false;
  }
}
