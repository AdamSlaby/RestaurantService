import {AfterViewInit, Component, OnInit} from '@angular/core';
import {News} from "../../model/news";

@Component({
  selector: 'app-news-feed',
  templateUrl: './news-feed.component.html',
  styleUrls: ['./news-feed.component.scss']
})
export class NewsFeedComponent implements OnInit, AfterViewInit {
  newsPage!: number
  newsFeed: News[] = [
    {
      newsId: 1,
      imageUrl: 'assets/mexican_food.jpg',
      title: 'Tydzień meksykański tylko u nas',
      content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. In rutrum dictum eros eu vestibulum. Donec tincidunt sem egestas tortor varius consectetur ut sed nisi. Praesent nunc elit, ornare non blandit aliquam, viverra a justo. Nunc mollis ante sit amet lorem gravida, at consequat velit lobortis. Maecenas auctor ante vitae massa.[...]',
      date: new Date(),
    },
    {
      newsId: 1,
      imageUrl: 'assets/mexican_food.jpg',
      title: 'Tydzień meksykański tylko u nas',
      content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. In rutrum dictum eros eu vestibulum. Donec tincidunt sem egestas tortor varius consectetur ut sed nisi. Praesent nunc elit, ornare non blandit aliquam, viverra a justo. Nunc mollis ante sit amet lorem gravida, at consequat velit lobortis. Maecenas auctor ante vitae massa.[...]',
      date: new Date(),
    },
    {
      newsId: 1,
      imageUrl: 'assets/mexican_food.jpg',
      title: 'Tydzień meksykański tylko u nas',
      content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. In rutrum dictum eros eu vestibulum. Donec tincidunt sem egestas tortor varius consectetur ut sed nisi. Praesent nunc elit, ornare non blandit aliquam, viverra a justo. Nunc mollis ante sit amet lorem gravida, at consequat velit lobortis. Maecenas auctor ante vitae massa.[...]',
      date: new Date(),
    },
    {
      newsId: 1,
      imageUrl: 'assets/mexican_food.jpg',
      title: 'Tydzień meksykański tylko u nas',
      content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. In rutrum dictum eros eu vestibulum. Donec tincidunt sem egestas tortor varius consectetur ut sed nisi. Praesent nunc elit, ornare non blandit aliquam, viverra a justo. Nunc mollis ante sit amet lorem gravida, at consequat velit lobortis. Maecenas auctor ante vitae massa.[...]',
      date: new Date(),
    },
  ]

  constructor() { }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    let id = 'news-' + (this.newsFeed.length - 1).toString();
    let newsDiv = document.getElementById(id);
    if (newsDiv)
      newsDiv.classList.remove('border-bottom');
  }

  loadMore() {
    //call method to receive next page of news from backend
  }
}
