import {AfterViewInit, ChangeDetectorRef, Component, HostListener, OnInit} from '@angular/core';
import {
  faArrowRightFromBracket,
  faBars,
  faBoxesStacked,
  faCartShopping,
  faChartLine,
  faHouseChimney,
  faInfo,
  faNewspaper,
  faPeopleGroup,
  faUser,
  faUtensils,
  faXmark,
  faFileInvoice,
  faCalendarDays
} from "@fortawesome/free-solid-svg-icons";
import {Router} from "@angular/router";
import {RestaurantShortInfo} from "../../model/restaurant/restaurant-short-info";

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
  faInfo = faInfo;
  faFileInvoice = faFileInvoice;
  faCalendarDays = faCalendarDays;
  contentHeader!: string;
  isCollapsed!: boolean;
  isNavCollapsed!: boolean;
  innerWidth!: number;
  innerHeight!: number;
  contentHeight!: number;
  navItemWidth!: number;
  restaurantId!: any;
  restaurants: RestaurantShortInfo[] = [
    {
      restaurantId: 1,
      city: 'Kielce',
      street: 'al. XI wieków Kielc'
    },
    {
      restaurantId: 2,
      city: 'Warszawa',
      street: 'Jagiellońska'
    },
    {
      restaurantId: 3,
      city: 'Kraków',
      street: 'Warszawska'
    }
  ]

  constructor(private cd: ChangeDetectorRef, private router: Router) {
    this.router.events.subscribe(val => {
      let url = this.router.url;
      this.contentHeader = MainNavComponent.getHeader(url);
    });
  }

  ngOnInit(): void {
    this.isNavCollapsed = true;
    this.innerWidth = window.innerWidth;
    this.innerHeight = window.innerHeight;
    this.restaurantId = localStorage.getItem('restaurantId');
  }

  ngAfterViewInit() {
    this.showMenu();
    this.setContentHeight();
    this.setMaxNavWidth();
  }

  @HostListener('window:resize', ['$event'])
  onResize($event: any) {
    this.innerWidth = window.innerWidth;
    this.innerHeight = window.innerHeight;
    this.resizeMenu();
    this.setContentHeight();
  }

  private setContentHeight() {
    let header = document.getElementById('header');
    if (header) {
      this.contentHeight = Math.floor(this.innerHeight - header.getBoundingClientRect().height - 1);
      this.cd.detectChanges();
    }
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

  private setMaxNavWidth() {
    setTimeout(() => {
      let ul = document.querySelector('ul');
      let maxWidth = 0;
      if (ul) {
        let children = ul.children;
        for (let i = 0; i < children.length; i++) {
          let span = children[i].children[0].children[1];
          if (span) {
            if (maxWidth < span.getBoundingClientRect().width)
              maxWidth = span.getBoundingClientRect().width;
          }
        }
      }
      this.navItemWidth = Math.ceil(maxWidth);
      this.cd.detectChanges();
    }, 500);
  }

  private static getHeader(url: string): string {
    switch (url) {
      case '/admin/dashboard': return 'Panel główny';
      case '/admin/orders': return 'Zamówienia';
      case '/admin/employees': return 'Pracownicy';
      case '/admin/supply': return 'Zaopatrzenie';
      case '/admin/news': return 'Aktualności';
      case '/admin/menu': return 'Menu';
      case '/admin/statistics': return 'Statystyki';
      case '/admin/restaurant': return 'Restauracja';
      case '/admin/invoices': return 'Faktury';
      case '/admin/reservations': return 'Rezerwacje';
      default: return '';
    }
  }

  changeRestaurantId() {
    localStorage.setItem('restaurantId', this.restaurantId + '');
  }

  isAdmin() {
    return localStorage.getItem('role') === 'ADMIN';
  }
}
