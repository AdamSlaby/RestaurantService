import {AfterViewInit, ChangeDetectorRef, Component, HostListener, OnInit} from '@angular/core';
import {DishOrderView} from "../../model/dish-order-view";
import {DishType} from "../../model/type";
import {faMagnifyingGlass, faMinus, faPlus, faXmark, faBasketShopping} from "@fortawesome/free-solid-svg-icons";
import {Order} from "../../model/order";

@Component({
  selector: 'app-order-food',
  templateUrl: './order-food.component.html',
  styleUrls: ['./order-food.component.scss']
})
export class OrderFoodComponent implements OnInit, AfterViewInit {
  showSearchInput: boolean = false;
  type = DishType;
  deliveryFee: number = 5;
  fullPrice!: number;
  minimalPrice!: number;
  searchPhrase: string = '';
  innerWidth!: number;
  innerHeight!: number;
  faMagnifyingGlass = faMagnifyingGlass;
  faBasketShopping = faBasketShopping;
  showScroll: boolean = false;
  basketHeight!: number;
  heightDif!: number;
  faXmark = faXmark;
  faMinus = faMinus;
  faPlus = faPlus;
  searchedDishes: DishOrderView[] = [];
  basket: Map<number, Order> = new Map<number, Order>();
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

  constructor() {
  }

  ngOnInit(): void {
    for (let i = 0; i < 6; i++) {
      let orderView = this.dishes[i];
      for (let j = 1; j <= 7; j++) {
        let dishCopy = {...orderView};
        this.dishes.push(dishCopy);
      }
    }
    this.getDishesByType(this.type.SOUP);
    this.minimalPrice = 30.00;
    this.innerWidth = window.innerWidth;
    this.innerHeight = window.innerHeight;
  }

  ngAfterViewInit() {
    let basket = document.getElementById('basket');
    if (basket)
      this.basketHeight = basket.getBoundingClientRect().height
  }

  @HostListener('window:resize', ['$event'])
  onResize($event: any) {
    this.innerWidth = window.innerWidth;
    this.innerHeight = window.innerHeight;
    this.isContentTooLong();
  }

  onSearchClick() {
    this.showSearchInput = true;
  }

  onCloseClick() {
    this.showSearchInput = false;
  }

  getDishesByType(type: DishType) {
    this.searchedDishes = this.dishes.filter(dish => dish.type === type)
  }

  search() {
    if (this.searchPhrase.length > 2) {
      this.searchedDishes = this.dishes
        .filter(dish => dish.name.toLowerCase().includes(this.searchPhrase.toLowerCase()));
    }
  }

  getDishById(id: number): DishOrderView {
    return this.dishes.filter(dish => dish.id === id)[0];
  }

  onScroll() {
    let element = document.getElementById('basket');
    if (element && window.innerWidth > 1400) {
      if (window.scrollY >= 735 && !element.classList.contains('position-fixed')) {
        element.classList.add('position-fixed', 'top-0', 'end-0');
      } else if (window.scrollY < 735 && element.classList.contains('position-fixed')) {
        element.classList.remove('position-fixed', 'top-0', 'end-0');
      }
    }
  }

  addDish(order: Order) {
    let price = order.price / order.amount;
    order.amount++;
    order.price += price;
    this.setFullPrice();
  }

  removeDish(order: Order) {
    if (order.amount == 1) {
      this.basket.delete(order.dishId);
    } else {
      let price = order.price / order.amount;
      order.amount--;
      order.price -= price;
    }
    this.setFullPrice();
  }

  addOrder(dish: DishOrderView) {
    if (this.basket.has(dish.id)) {
      let order = this.basket.get(dish.id);
      if (order) {
        let price = order.price / order.amount;
        order.amount += dish.amount;
        order.price += price;
      }
    } else {
      this.basket.set(dish.id, {
        dishId: dish.id,
        amount: dish.amount,
        price: (dish.price * dish.amount),
      });
    }
    this.setFullPrice();
    this.isContentTooLong();
  }

  setFullPrice() {
    let sum = 0;
    for (let order of this.basket.values())
      sum += order.price;
    this.fullPrice = sum + this.deliveryFee;
  }

  isContentTooLong() {
    let basket = document.getElementById('basket');
    if (basket && this.innerWidth >= 1400 && window.scrollY >= 735) {
      this.heightDif = this.innerHeight - this.basketHeight;
      this.showScroll =  this.heightDif < 0;
      this.heightDif = this.innerHeight;
    } else
      this.showScroll = false;
  }
}
