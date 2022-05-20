import {AfterViewInit, ChangeDetectorRef, Component, HostListener, OnInit} from '@angular/core';
import {Dish} from "../../../model/dish/dish";
import {ActivatedRoute} from "@angular/router";
import { MenuService } from 'src/app/service/menu.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit, AfterViewInit {
  innerWidth!: number;
  soupImg: string = 'assets/soup-image.jpg';
  mainDishImg: string = 'assets/main_dish_image.jpg';
  fishImg: string = 'assets/fish_image.jpg';
  saladImg: string = 'assets/salad-image.jpg';
  dessertImg: string = 'assets/dessert_image.jpg';
  beverageImg: string = 'assets/beverage-picture.jpg'
  menu!: Dish[];
  activeFragment!: string;

  constructor(private route: ActivatedRoute, private cd: ChangeDetectorRef,
              private menuService: MenuService) {
  }

  ngOnInit(): void {

    this.menuService.getDishesFromMenu().subscribe(data => {
      this.menu = data;
    }, error => {
      console.error(error);
    });
  }

  ngAfterViewInit() {
    this.route.fragment.subscribe(fragment => {
        let element = document.querySelector("#" + fragment);
          setTimeout(() => {
            if (element && fragment) {
              element.scrollIntoView();
              this.activeFragment = fragment;
            }
          }, 100)
      }
    );
    this.innerWidth = window.innerWidth;
    this.cd.detectChanges();
  }

  @HostListener('window:resize', ['$event'])
  onResize($event: any) {
    this.innerWidth = window.innerWidth;
    this.cd.detectChanges();
  }

  getDishes(type: string, colNr: number) {
    if (this.menu) {
      let dishes = this.menu.filter(el => el.type === type);
      let amountInCol = Math.ceil(dishes.length / 2.0);
      let firstIndex = colNr * amountInCol;
      let secondIndex = firstIndex + amountInCol;
      return dishes.slice(firstIndex, secondIndex);
    }
    return [];
  }

  isSectionActive(fragment: string) {
    let image = document.getElementById(fragment);
    let content = document.getElementById(fragment + 'Content');
    if (image && content)
      if (window.scrollY >= window.scrollY + image.getBoundingClientRect().top &&
        window.scrollY < window.scrollY + content.getBoundingClientRect().bottom) {
          this.activeFragment = fragment;
          this.cd.detectChanges();
        }
  }

  onScroll() {
    let images: HTMLElement[] = [];
    let links: HTMLElement[] = [];
    let imagesDivIds = ['soup', 'main-dish', 'fish', 'salad', 'dessert', 'beverage'];
    let linkIds = ['soupLink', 'mainDishLink', 'fishLink', 'saladLink', 'dessertLink', 'beverageLink'];
    for (let i = 0; i < imagesDivIds.length; i++) {
      this.isSectionActive(imagesDivIds[i]);
      let image = document.getElementById(imagesDivIds[i]);
      let link = document.getElementById(linkIds[i]);
      if (image)
        images.push(image);
      if (link)
        links.push(link);
    }
    for (let image of images) {
      if (this.checkSideNavOverlapImage(image, links[0], links[links.length - 1])) {
        for (let link of links) {
          link.classList.remove('btn-outline-grey')
          if (!link.classList.contains('btn-outline-light'))
            link.classList.add('btn-outline-light');
        }
        break;
      } else {
        for (let link of links) {
          link.classList.remove('btn-outline-light')
          if (!link.classList.contains('btn-outline-grey'))
            link.classList.add('btn-outline-grey');
        }
      }
    }
  }

  checkSideNavOverlapImage(imageDiv: HTMLElement, linkTop: HTMLElement, linkBottom: HTMLElement): boolean {
    let topImageBorder = imageDiv.getBoundingClientRect().top;
    let bottomImageBorder = imageDiv.getBoundingClientRect().bottom;
    let topLinkBorder = linkTop.getBoundingClientRect().top;
    let bottomLinkBorder = linkBottom.getBoundingClientRect().bottom;

    return topImageBorder < topLinkBorder && bottomImageBorder > bottomLinkBorder;
  }

  scrollToSection(section: string) {
    let element = document.getElementById(section);
    if (element)
      element.scrollIntoView();
  }
}
