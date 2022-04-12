import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NgbCalendar, NgbDateStruct, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {faEye, faPenToSquare, faPlus, faXmark} from "@fortawesome/free-solid-svg-icons";
import {NewsListView} from "../../model/news/news-list-view";
import {SortEvent} from "../../model/sort-event";
import {HtmlUtility} from "../../utility/html-utility";

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
  chosenNews!: number;
  titleKeyword!: string;
  selectedNewsId!: number;
  previousPage!: number;
  pageNr!: number;
  pageSize!: number;
  chosenEmployeeId: any;
  showNewsDetails: boolean = false;
  chosenDate!: NgbDateStruct | any;
  minDate = this.calendar.getToday();
  newsList: NewsListView = {
    maxPage: 10,
    news: [
      {
        id: 1,
        employeeId: 1,
        title: 'Tydzień meksykański tylko u nas',
        date: new Date(),
      },
    ]
  }

  constructor(private cd: ChangeDetectorRef, private calendar: NgbCalendar,
              private modalService: NgbModal) {}

  ngOnInit(): void {
    let newsShortInfo = this.newsList.news[0];
    for (let i = 1; i < 10; i++) {
      this.newsList.news.push(Object.assign({}, newsShortInfo));
    }
    this.pageNr = 1;
    this.pageSize = 10;
  }

  open(content: any) {
    this.modalService.open(content, {}).result.then(() => {});
  }

  getNewsById() {
    //todo
  }

  getNewsByTitle() {
    //todo
  }

  getNewsByDate() {
    //todo
    console.log(this.chosenDate);
  }

  getNewsByEmployeeId() {
    //todo
  }

  resetFilters() {
    //todo
    this.chosenDate = undefined;
  }

  onSort($event: SortEvent) {
    //todo
  }

  loadPage(page: number) {
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

  removeNews(removeNewsForm: any) {
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
}
