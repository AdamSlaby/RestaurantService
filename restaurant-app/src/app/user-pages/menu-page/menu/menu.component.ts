import {AfterViewInit, Component, HostListener, OnInit} from '@angular/core';
import {Dish} from "../../../model/dish/dish";
import {ActivatedRoute} from "@angular/router";

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
  menu: Dish[] = [
    {
      id: 6,
      name: 'Barszcz z uszkami',
      type: 'Zupy',
      ingredients: '30g uszek',
      price: 22.32,
      isBest: true,
    },
    {
      id: 6,
      name: 'Pomidorowa',
      type: 'Zupy',
      ingredients: '30g makaronu',
      price: 21.32,
      isBest: true,
    },
    {
      id: 1,
      name: 'Zupa szczawiowa',
      type: 'Zupy',
      ingredients: 'z jajkiem i kiełbasą',
      price: 20.32,
      isBest: false,
    },
    {
      id: 2,
      name: 'Pierogi',
      type: 'Dania główne',
      ingredients: 'z kapustą i mięsem',
      price: 25.00,
      isBest: false,
    },
    {
      id: 3,
      name: 'Śledź',
      type: 'Ryby',
      ingredients: 'Śledź 300g, frytki, zestaw surówek',
      price: 18.23,
      isBest: false,
    },
    {
      id: 4,
      name: 'Sałatka grecka',
      type: 'Sałatki',
      ingredients: 'i kurczakiem i serem pleśniowym',
      price: 28.23,
      isBest: false,
    },
    {
      id: 5,
      name: 'TIRAMISU',
      type: 'Desery',
      ingredients: 'bita śmietana',
      price: 28.23,
      isBest: false,
    },
    {
      id: 7,
      name: 'Mirinda 1L',
      type: 'Napoje',
      ingredients: '',
      price: 5.23,
      isBest: false,
    },
  ]

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    for (let i = 2; i < 8; i++) {
      let dish = {...this.menu[i]};
      for (let j = 1; j <= 8; j++) {
        this.menu.push(dish);
      }
    }
  }

  ngAfterViewInit() {
    this.route.fragment.subscribe(fragment => {
        let element = document.querySelector("#" + fragment);
          setTimeout(() => {
            if (element) {
              element.scrollIntoView();
            }
          }, 1)
      }
    );
    this.innerWidth = window.innerWidth;
  }

  @HostListener('window:resize', ['$event'])
  onResize($event: any) {
    this.innerWidth = window.innerWidth;
  }

  getDishes(type: string, colNr: number) {
    let dishes = this.menu.filter(el => el.type === type);
    let amountInCol = Math.ceil(dishes.length / 2.0);
    let firstIndex = colNr * amountInCol;
    let secondIndex = firstIndex + amountInCol;
    return dishes.slice(firstIndex, secondIndex);
  }

  isSectionActive(fragment: string) {
    let image = document.getElementById(fragment);
    let content = document.getElementById(fragment + 'Content');
    if (image && content)
      return window.scrollY >= window.scrollY + image.getBoundingClientRect().top &&
        window.scrollY < window.scrollY + content.getBoundingClientRect().bottom;
    return false;
  }

  onScroll() {
    let images: HTMLElement[] = [];
    let links: HTMLElement[] = [];
    let imagesDivIds = ['soup', 'main-dish', 'fish', 'salad', 'dessert', 'beverage'];
    let linkIds = ['soupLink', 'mainDishLink', 'fishLink', 'saladLink', 'dessertLink', 'beverageLink'];
    for (let i = 0; i < imagesDivIds.length; i++) {
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
