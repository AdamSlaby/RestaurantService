import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss']
})
export class NewsComponent implements OnInit{
  newsImg: string = 'assets/news_image.jpg';
  constructor() { }

  ngOnInit(): void {
  }
}
