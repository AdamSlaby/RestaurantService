import {NewsShortInfo} from "./news-short-info";

export interface NewsListView {
  maxPage: number;
  totalElements: number;
  news: NewsShortInfo[];
}
