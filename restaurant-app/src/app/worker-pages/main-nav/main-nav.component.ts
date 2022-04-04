import {AfterViewInit, ChangeDetectorRef, Component, HostListener, Input, OnInit} from '@angular/core';
import {faUser, faHouseChimney, faBars, faXmark,faBoxesStacked, faNewspaper, faCartShopping, faUtensils, faChartLine,
  faArrowRightFromBracket, faPeopleGroup} from "@fortawesome/free-solid-svg-icons";
import {Router} from "@angular/router";

@Component({
  selector: 'app-main-nav',
  templateUrl: './main-nav.component.html',
  styleUrls: ['./main-nav.component.scss']
})
export class MainNavComponent implements OnInit, AfterViewInit {
  faBars = faBars;
  faXmark = faXmark;
  faArrowRightFromBracket = faArrowRightFromBracket;
  faPeopleGroup = faPeopleGroup;
  faUser = faUser;
  faHouseChimney = faHouseChimney;
  faBoxesStacked = faBoxesStacked;
  faNewspaper = faNewspaper;
  faCartShopping = faCartShopping;
  faUtensils = faUtensils;
  faChartLine = faChartLine;
  contentHeader!: string;
  isCollapsed!: boolean;
  isNavCollapsed!: boolean;
  innerWidth!: number;
  innerHeight!: number;

  constructor(private cd: ChangeDetectorRef, private router: Router) {
    this.router.events.subscribe(val => {
      let url = this.router.url;
      this.contentHeader = this.getHeader(url);
    });
  }

  ngOnInit(): void {
    this.isNavCollapsed = true;
    this.innerWidth = window.innerWidth;
    this.innerHeight = window.innerHeight;
  }

  ngAfterViewInit() {
    this.showMenu();
  }

  @HostListener('window:resize', ['$event'])
  onResize($event: any) {
    this.innerWidth = window.innerWidth;
    this.innerHeight = window.innerHeight;
    this.resizeMenu();
  }

  showMenu() {
    this.isNavCollapsed = !this.isNavCollapsed;
    if (this.innerWidth > 1200) {
      this.isCollapsed = this.isNavCollapsed;
    } else {
      if (this.isNavCollapsed)
        this.isCollapsed = true;
      else
        this.isCollapsed = true;
    }
    this.cd.detectChanges();
  }

  private resizeMenu() {
    this.isNavCollapsed = true;
    this.isCollapsed = this.isNavCollapsed;
  }

  private getHeader(url: string): string {
    switch (url) {
      case '/admin/dashboard': return 'Panel główny';
      case '/admin/orders': return 'Zamówienia';
      case '/admin/employees': return 'Pracownicy';
      case '/admin/supply': return 'Zaopatrzenie';
      case '/admin/news': return 'Aktualności';
      case '/admin/menu': return 'Menu';
      case '/admin/statistics': return 'Statystyki';
      default: throw new Error('Page not found');
    }
  }
}
