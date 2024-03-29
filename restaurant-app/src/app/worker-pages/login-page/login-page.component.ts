import {AfterViewInit, ChangeDetectorRef, Component, HostListener, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {Credentials} from "../../model/credentials";
import {EmployeeService} from "../../service/employee.service";
import {LoginResponse} from "../../model/login-response";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent implements OnInit, AfterViewInit {
  innerWidth!: number;
  innerHeight!: number;
  sectionHeight!: string;
  loginResponse!: LoginResponse;
  loginForm = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]],
  });
  loading = false;
  errors: Map<string, string> = new Map<string, string>();

  constructor(private fb: FormBuilder, private cd: ChangeDetectorRef,
              private employeeService: EmployeeService) { }

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
    this.errors.clear();
    let credentials: Credentials = {
      username: this.loginForm.get('username')?.value,
      password: this.loginForm.get('password')?.value
    }
    this.loading = true;
    this.employeeService.login(credentials).subscribe(data => {
      this.loading = false;
      this.loginResponse = data;
      localStorage.setItem("accessToken", this.loginResponse.accessToken);
      localStorage.setItem("refreshToken", this.loginResponse.refreshToken);
      localStorage.setItem("role", this.loginResponse.role);
      localStorage.setItem("fullName", this.loginResponse.fullName);
      localStorage.setItem("restaurantId", this.loginResponse.restaurantId.toString());
      localStorage.setItem("employeeId", this.loginResponse.employeeId.toString());
      location.assign("/admin");
    }, error => {
      this.errors = new Map(Object.entries(error.error));
      this.loginForm.markAsPristine();
      this.loading = false;
      console.error(error);
    })
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
