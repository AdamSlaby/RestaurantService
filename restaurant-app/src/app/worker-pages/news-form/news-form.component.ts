import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {faXmark, faNewspaper} from "@fortawesome/free-solid-svg-icons";
import {NgbCalendar} from "@ng-bootstrap/ng-bootstrap";
import {News} from "../../model/news/news";
import {FormBuilder, NgForm, Validators} from "@angular/forms";
import { NewsService } from 'src/app/service/news.service';

@Component({
  selector: 'app-news-form',
  templateUrl: './news-form.component.html',
  styleUrls: ['./news-form.component.scss']
})
export class NewsFormComponent implements OnInit {
  @ViewChild('form') form!: NgForm;
  @Input() set newsId(value: any) {
    this._newsId = value;
    if (value !== -1) {
      this.newsService.getNews(this._newsId).subscribe(data => {
        this.newsInfo = data;
        this.newsInfo.info.date = new Date(this.newsInfo.info.date);
        this.newsForm.patchValue({
          title: this.newsInfo.info.title,
          content: this.newsInfo.info.content,
        });
      }, error => {
        console.error(error);
      })
    } else {
      setTimeout(() => {
        this.form.resetForm();
      }, 500);
    }
  }
  @Output() closeNewsDetails = new EventEmitter<void>();
  faXmark = faXmark;
  faNewspaper = faNewspaper;
  _newsId!: any;
  errors: Map<string, string> = new Map<string, string>();
  loading = false;
  minDate = this.calendar.getToday();
  newsInfo!: News;
  isSuccessful: boolean = false;
  imageUrl: any;
  news: FormData = new FormData();
  newsForm = this.fb.group({
    image: [null],
    title: ['', [Validators.required, Validators.maxLength(200)]],
    content: ['', [Validators.required, Validators.maxLength(2500)]],
  });

  constructor(private calendar: NgbCalendar, private fb: FormBuilder, 
              private newsService: NewsService) { }

  ngOnInit(): void {
  }

  get f() {
    return this.newsForm.controls;
  }

  onNewsFormSubmit() {
    this.errors.clear();
    let employeeId = '';
    if (localStorage.getItem('employeeId'))
      employeeId = localStorage.getItem('employeeId') as string;
    this.news.set('employeeId', employeeId);
    if (this.newsForm.get('image')?.value) 
      this.news.set('image', this.newsForm.get('image')?.value);
    this.news.set('title', this.newsForm.get('title')?.value);
    this.news.set('content', this.newsForm.get('content')?.value);
    this.loading = true;
    if (this._newsId === -1) {
      this.newsService.addNews(this.news).subscribe(data => {
        this.loading = false;
        this.isSuccessful = true;
      }, error => {
        this.errors = new Map(Object.entries(error.error));
        this.newsForm.markAsPristine();
        this.loading = false;
        this.isSuccessful = false;
        console.error(error);
      });
    } else {
      this.newsService.patchNews(this.news, this._newsId).subscribe(data => {
        this.loading = false;
        this.isSuccessful = true;
      }, error => {
        this.errors = new Map(Object.entries(error.error));
        this.newsForm.markAsPristine();
        this.loading = false;
        this.isSuccessful = false;
        console.error(error);
      });
    }
  }

  closeComponent() {
    this.closeNewsDetails.emit();
  }

  getInvalidControl() {
    let controls = this.f;
    for (let name in controls) {
      if (controls[name].invalid)
        console.log(controls[name]);
    }
    return true;
  }

  uploadPhoto($event: any) {
    if ($event.target.files[0]) {
      this.newsForm.patchValue({
        image: $event.target.files[0],
      });
      const reader = new FileReader();
      reader.onload = () => {
        this.imageUrl = reader.result as string;
      }
      reader.readAsDataURL($event.target.files[0]);
    } else {
      this.newsForm.patchValue({
        image: null,
      });
    }
    console.log(this.newsForm.get('image')?.value);
  }
}
