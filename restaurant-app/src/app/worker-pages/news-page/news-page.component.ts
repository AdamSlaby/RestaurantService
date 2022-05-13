import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NgbCalendar, NgbDateAdapter, NgbDateStruct, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {faEye, faPenToSquare, faPlus, faXmark} from "@fortawesome/free-solid-svg-icons";
import {NewsListView} from "../../model/news/news-list-view";
import {SortEvent} from "../../model/sort-event";
import {HtmlUtility} from "../../utility/html-utility";
import { NewsFilters } from 'src/app/model/news/news-filters';
import { NewsService } from 'src/app/service/news.service';
import { NgbDateToDateAdapter } from 'src/app/adapter/datepicker-date-adapter';

@Component({
  selector: 'app-news-page',
  templateUrl: './news-page.component.html',
  styleUrls: ['./news-page.component.scss'],
  providers: [{provide: NgbDateAdapter, useClass: NgbDateToDateAdapter}]
})
export class NewsPageComponent implements OnInit {
  faPlus = faPlus;
  faEye = faEye;
  faPenToSquare = faPenToSquare;
  faXmark = faXmark;
  chosenNewsId!: any;
  chosenTitle!: any;
  chosenEmployeeId: any;
  chosenDate!: NgbDateStruct | any;
  selectedNewsId!: number;
  previousPage!: number;
  pageNr!: number;
  showNewsDetails: boolean = false;
  newsList!: NewsListView;

  constructor(private cd: ChangeDetectorRef, private newsService: NewsService,
              private modalService: NgbModal) {}

  ngOnInit(): void {
    this.pageNr = 1;
    this.previousPage = 1;
    this.getNewsList(this.filters);
  }

  open(content: any) {
    this.modalService.open(content, {}).result.then(() => {}).catch(() => {});
  }

  filterNews() {
    let filters = this.filters
    this.getNewsList(filters);
  }

  resetFilters() {
    this.chosenNewsId = null;
    this.chosenEmployeeId = null;
    this.chosenTitle = null;
    this.chosenDate = null;
    this.getNewsList(this.filters);
  }

  onSort($event: SortEvent) {
    let filters = this.filters
    filters.sortEvent = event;
    this.getNewsList(filters);
  }

  loadPage(page: number) {
    let filters = this.filters;
    filters.pageNr = page - 1;
    if (this.previousPage !== this.pageNr) {
      this.newsService.getNewsList(filters).subscribe(data => {
        this.previousPage = this.pageNr;
        this.newsList = data;
        this.newsList.news.forEach(el => el.date = new Date(el.date));
      }, error => {
        console.error(error);
      })
    }
  }

  editNews(id: number) {
    this.selectedNewsId = id;
    this.showNewsDetails = true;
    setTimeout(() => {
      HtmlUtility.scrollIntoView('newsForm');
    }, 1);
  }

  removeNews(modal: any) {
    this.newsService.deleteNews(this.selectedNewsId).subscribe(data => {
      modal.close();
      this.getNewsList(this.filters);
    }, error => {
      console.error(error);
    });
  }

  openRemoveModal(id: number, removeNewsForm: any) {
    this.selectedNewsId = id;
    this.open(removeNewsForm);
  }

  createNews() {
    this.selectedNewsId = -1;
    this.showNewsDetails = true;
    setTimeout(() => {
      HtmlUtility.scrollIntoView('newsForm');
    }, 1);
  }

  closeNewsDetails() {
    this.showNewsDetails = false;
  }

  getNewsList(filters: NewsFilters) {
    this.newsService.getNewsList(filters).subscribe(data => {
      this.newsList = data;
      this.newsList.news.forEach(el => el.date = new Date(el.date));
      console.log(data);
    }, error => {
      console.error(error);
    });
  }

  get filters() {
    return {
      newsId: this.chosenNewsId,
      employeeId: this.chosenEmployeeId,
      title: this.chosenTitle,
      date: this.chosenDate,
      sortEvent: null,
      pageNr: 0
    } as NewsFilters;
  }
}
