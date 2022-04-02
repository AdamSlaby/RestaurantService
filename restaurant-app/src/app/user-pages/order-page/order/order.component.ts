import {Component, HostListener, OnInit} from '@angular/core';
import {faBasketShopping} from "@fortawesome/free-solid-svg-icons";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DishOrderView} from "../../../model/dish-order-view";
import {DishType} from "../../../model/type";
import {BasketService} from "../../../service/basket.service";

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {
  orderImgUrl: string = 'assets/order_image.jpg';
  innerWidth!: number;
  innerHeight!: number;
  heightDif!: string;
  faBasketShopping = faBasketShopping;
  dishes: DishOrderView[] = [
    {
      id: 10,
      name: 'Ogórkowa',
      imageUrl: 'assets/soup.jpg',
      type: DishType.SOUP,
      ingredients: '30g makaronu',
      amount: 1,
      price: 21.32,
    },
    {
      id: 6,
      name: 'Pomidorowa',
      imageUrl: 'assets/soup.jpg',
      type: DishType.SOUP,
      ingredients: '30g makaronu',
      amount: 1,
      price: 21.32,
    },
    {
      id: 2,
      name: 'Pierogi',
      imageUrl: 'assets/soup.jpg',
      type: DishType.MAIN_DISH,
      ingredients: 'z kapustą i mięsem',
      amount: 1,
      price: 25.00,
    },
    {
      id: 3,
      name: 'Śledź',
      imageUrl: 'assets/soup.jpg',
      type: DishType.FISH,
      ingredients: 'Śledź 300g, frytki, zestaw surówek',
      amount: 1,
      price: 18.23,
    },
    {
      id: 4,
      name: 'Sałatka grecka',
      imageUrl: 'assets/soup.jpg',
      type: DishType.SALAD,
      ingredients: 'i kurczakiem i serem pleśniowym',
      amount: 1,
      price: 28.23,
    },
    {
      id: 5,
      name: 'TIRAMISU',
      imageUrl: 'assets/soup.jpg',
      type: DishType.DESSERT,
      ingredients: 'bita śmietana',
      amount: 1,
      price: 28.23,
    },
    {
      id: 7,
      name: 'Mirinda 1L',
      imageUrl: 'assets/soup.jpg',
      type: DishType.BEVERAGE,
      ingredients: '',
      amount: 1,
      price: 5.23,
    },
  ]

  constructor(private modalService: NgbModal, private basketService: BasketService) {
  }

  ngOnInit(): void {
    this.innerWidth = window.innerWidth;
    this.innerHeight = window.innerHeight;
    this.basketService.addDishes(this.dishes);
  }

  open(content: any) {
    this.modalService.open( content,{ariaLabelledBy: 'modal-basic-title'})
      .result.then((result) => {});
  }

  @HostListener('window:resize', ['$event'])
  onResize($event: any) {
    this.innerWidth = window.innerWidth;
    this.innerHeight = window.innerHeight;
    this.setBasketHeight();
  }

  setBasketHeight() {
    this.heightDif = this.innerHeight + 'px';
  }
}
