import {AfterViewInit, ChangeDetectorRef, Component, HostListener, Input, OnInit} from '@angular/core';
import {faUser, faHouseChimney, faBars, faXmark, faArrowRightFromBracket} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-main-nav',
  templateUrl: './main-nav.component.html',
  styleUrls: ['./main-nav.component.scss']
})
export class MainNavComponent implements OnInit, AfterViewInit {
  faBars = faBars;
  faXmark = faXmark;
  faArrowRightFromBracket = faArrowRightFromBracket;
  isCollapsed!: boolean;
  isNavCollapsed!: boolean;
  innerWidth!: number;
  innerHeight!: number;
  faUser = faUser;
  faHouseChimney = faHouseChimney;

  constructor(private cd: ChangeDetectorRef) { }

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
}
