import {AfterViewInit, Component, OnInit} from '@angular/core';
import { NewsService } from 'src/app/service/news.service';
import {NewsInfo} from "../../../model/news/news-info";

@Component({
  selector: 'app-news-feed',
  templateUrl: './news-feed.component.html',
  styleUrls: ['./news-feed.component.scss']
})
export class NewsFeedComponent implements OnInit, AfterViewInit {
  newsPage!: number
  pageNr!: number;
  newsFeed!: NewsInfo[];
  isNewsListEmpty: boolean = false;

  constructor(private newsService: NewsService) { }

  ngOnInit(): void {
    this.pageNr = 0;
    this.getNewsList();
  }

  ngAfterViewInit(): void {
    if (this.newsFeed)
      this.removeBorder();
  }

  getNewsList() {
    this.newsService.getNewsClientList(this.pageNr).subscribe(data => {
      if (this.newsFeed) {
        this.newsFeed = this.newsFeed.concat(data);
      } else
        this.newsFeed = data;
      if (data.length === 0) {
        this.isNewsListEmpty = true;
      } else {
        this.newsFeed.forEach(el => el.date = new Date(el.date));
        this.isNewsListEmpty = false;
      }
      console.log(data);
      this.removeBorder();
    }, errror => {
      console.error(errror);
    });
  }

  loadMore() {
    this.pageNr++;
    this.getNewsList();
  }

  removeBorder() {
    let id = 'news-' + (this.newsFeed.length - 1).toString();
    let newsDiv = document.getElementById(id);
    if (newsDiv)
      newsDiv.classList.remove('border-bottom');
  }
}
