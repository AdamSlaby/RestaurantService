import {AfterViewInit, Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import { NewsService } from 'src/app/service/news.service';
import {NewsInfo} from "../../../model/news/news-info";

@Component({
  selector: 'app-news-info',
  templateUrl: './news-info.component.html',
  styleUrls: ['./news-info.component.scss']
})
export class NewsInfoComponent implements OnInit, AfterViewInit {
  newsId!: number;
  news!: NewsInfo

  constructor(private route: ActivatedRoute, private newsService: NewsService) {
    this.route.params.subscribe( params =>  this.newsId = params['id']);
  }

  ngOnInit(): void {
    this.newsService.getNewsInfo(this.newsId).subscribe(data => {
      this.news = data;
      this.news.date = new Date(this.news.date);
    }, error => {
      console.error(error);
    })
  }

  ngAfterViewInit() {
    this.route.fragment.subscribe((fragment) => {
      if (fragment) {
        let element = document.getElementById(fragment);
          setTimeout(() => {
            if (element)
            element.scrollIntoView({behavior: "smooth"});
          }, 100)
      }
    })
  }
}
