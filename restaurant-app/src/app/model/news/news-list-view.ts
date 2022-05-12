import {NewsShortInfo} from "./news-short-info";

export interface NewsListView {
  totalElements: number;
  news: NewsShortInfo[];
}
