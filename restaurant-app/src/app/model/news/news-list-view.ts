import {NewsShortInfo} from "./news-short-info";

export interface NewsListView {
  maxPage: number;
  news: NewsShortInfo[];
}
