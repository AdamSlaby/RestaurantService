import {AfterViewInit, ChangeDetectorRef, Component, HostListener, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent implements OnInit, AfterViewInit {
  innerWidth!: number;
  innerHeight!: number;
  sectionHeight!: string;
  loginForm = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]],
  });
  loading = false;
  errors: Map<string, string> = new Map<string, string>();

  constructor(private fb: FormBuilder, private cd: ChangeDetectorRef) { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    this.innerHeight = window.innerHeight;
    this.innerWidth = window.innerWidth
    this.setSectionHeight();
  }

  @HostListener('window:resize', ['$event'])
  onResize($event: any) {
    this.innerHeight = window.innerHeight;
    this.innerWidth = window.innerWidth
    this.setSectionHeight();
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    //todo
  }

  setSectionHeight() {
    if (this.innerHeight > 841 && this.innerWidth > 1500) {
      let header = document.getElementById('header');
      let footer = document.getElementById('footer');
      if (header && footer) {
        this.sectionHeight =  Math.ceil(this.innerHeight -
          (header.getBoundingClientRect().height + footer.getBoundingClientRect().height)) + 'px';
      } else {
        this.sectionHeight = 'auto';
      }
    } else {
      this.sectionHeight = 'auto';
    }
    this.cd.detectChanges();
  }
}
