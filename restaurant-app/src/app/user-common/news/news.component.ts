import {AfterViewInit, Component, OnInit} from '@angular/core';
import {News} from "../model/news";
import '@angular/common/locales/global/pl';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss']
})
export class NewsComponent implements OnInit {
  constructor() { }

  ngOnInit(): void {
  }
}
