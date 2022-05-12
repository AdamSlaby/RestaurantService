import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NgbCalendar, NgbDateStruct, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {faEye, faPenToSquare, faPlus, faXmark} from "@fortawesome/free-solid-svg-icons";
import {NewsListView} from "../../model/news/news-list-view";
import {SortEvent} from "../../model/sort-event";
import {HtmlUtility} from "../../utility/html-utility";
import { NewsFilters } from 'src/app/model/news/news-filters';

@Component({
  selector: 'app-news-page',
  templateUrl: './news-page.component.html',
  styleUrls: ['./news-page.component.scss']
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
  newsList: NewsListView = {
    totalElements: 110,
    news: [
      {
        id: 1,
        employeeId: 1,
        title: 'Tydzień meksykański tylko u nas',
        date: new Date(),
      },
    ]
  }

  constructor(private cd: ChangeDetectorRef,
              private modalService: NgbModal) {}

  ngOnInit(): void {
    let newsShortInfo = this.newsList.news[0];
    for (let i = 1; i < 10; i++) {
      this.newsList.news.push(Object.assign({}, newsShortInfo));
    }
    this.pageNr = 1;
    this.previousPage = 1;
  }

  open(content: any) {
    this.modalService.open(content, {}).result.then(() => {}).catch(() => {});
  }

  filterNews() {
    //todo
    let filters = this.filters
  }

  resetFilters() {
    //todo
    this.chosenNewsId = null;
    this.chosenEmployeeId = null;
    this.chosenTitle = null;
    this.chosenDate = null;
    let filters = this.filters;
  }

  onSort($event: SortEvent) {
    //todo
    let filters = this.filters
    filters.sortEvent = event;
  }

  loadPage(page: number) {
    let filters = this.filters;
    filters.pageNr = page - 1;
    if (this.previousPage !== this.pageNr) {
      this.previousPage = this.pageNr;
    }
  }

  editNews(id: number) {
    //todo
    this.selectedNewsId = id;
    this.showNewsDetails = true;
    setTimeout(() => {
      HtmlUtility.scrollIntoView('newsForm');
    }, 1);
  }

  removeNews(modal: any) {
    //todo
  }

  openRemoveModal(id: number, removeNewsForm: any) {
    //todo
    this.selectedNewsId = id;
    this.open(removeNewsForm);
  }

  createNews() {
    //todo
    this.selectedNewsId = -1;
    this.showNewsDetails = true;
    setTimeout(() => {
      HtmlUtility.scrollIntoView('newsForm');
    }, 1);
  }

  closeNewsDetails() {
    this.showNewsDetails = false;
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
