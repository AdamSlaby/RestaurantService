import {Component, HostListener, OnInit} from '@angular/core';
import {faBasketShopping} from "@fortawesome/free-solid-svg-icons";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import { MenuService } from 'src/app/service/menu.service';
import {DishOrderView} from "../../../model/dish/dish-order-view";
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
  dishes!: DishOrderView[];

  constructor(private modalService: NgbModal, private basketService: BasketService,
              private menuService: MenuService) {
  }

  ngOnInit(): void {
    this.innerWidth = window.innerWidth;
    this.innerHeight = window.innerHeight;
    this.menuService.getDisheOrderViewsFromMenu().subscribe(data => {
      this.dishes = data;
      this.basketService.addDishes(this.dishes);
    }, error => {
      console.error(error);
    })
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
