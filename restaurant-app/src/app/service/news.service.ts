import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { News } from '../model/news/news';
import { NewsFilters } from '../model/news/news-filters';
import { NewsInfo } from '../model/news/news-info';
import { NewsListView } from '../model/news/news-list-view';
import { GeneralService } from './general.service';

@Injectable({
  providedIn: 'root'
})
export class NewsService {
  private COMMON_URL = `${GeneralService.BASE_URL}/news`;
  private GET_NEWS_INFO = `${this.COMMON_URL}/info/`;
  private GET_NEWS_CLIENT = `${this.COMMON_URL}/list/info`;
  private GET_NEWS = `${this.COMMON_URL}/details/`;
  private GET_NEWS_LIST = `${this.COMMON_URL}/list`;
  private ADD_NEWS = `${this.COMMON_URL}/`;
  private PATCH_NEWS = `${this.COMMON_URL}/`;
  private DELETE_NEWS = `${this.COMMON_URL}/`;

  constructor(private http: HttpClient) { }

  getNewsInfo(newsId: number): Observable<NewsInfo> {
    return this.http.get<NewsInfo>(this.GET_NEWS_INFO + newsId);
  }

  getNewsClientList(pageNr: number): Observable<NewsInfo[]> {
    return this.http.get<NewsInfo[]>(this.GET_NEWS_CLIENT + "?pageNr=" + pageNr);
  }

  getNews(newsId: number): Observable<News> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.get<News>(this.GET_NEWS + newsId, {headers});
  }

  getNewsList(filters: NewsFilters): Observable<NewsListView> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<NewsListView>(this.GET_NEWS_LIST, filters, {headers});
  }

  addNews(data: FormData): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.post<any>(this.ADD_NEWS, data, {headers});
  }

  patchNews(data: FormData, newsId: number): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.patch<any>(this.PATCH_NEWS + newsId, data, {headers});
  }

  deleteNews(newsId: number): Observable<any> {
    const headers = new HttpHeaders({Authorization: 'Bearer ' + localStorage.getItem('accessToken')});
    return this.http.delete<any>(this.DELETE_NEWS + newsId, {headers});
  }
}
