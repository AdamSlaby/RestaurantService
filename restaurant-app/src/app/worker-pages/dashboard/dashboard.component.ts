import {AfterViewInit, ChangeDetectorRef, Component, NgZone, OnInit} from '@angular/core';
import {
  faArrowsRotate,
  faBowlFood,
  faCartPlus,
  faCheck,
  faClock,
  faCoins,
  faHourglassStart
} from "@fortawesome/free-solid-svg-icons";
import {ActiveOrder} from "../../model/order/active-order";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Chart} from "../../model/chart/chart";
import { OrderService } from 'src/app/service/order.service';
import { StatisticService } from 'src/app/service/statistic.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, AfterViewInit {
  faClock = faClock;
  faCoins = faCoins;
  faArrowsRotate = faArrowsRotate;
  faCartPlus = faCartPlus;
  faBowlFood = faBowlFood;
  faHourglassStart = faHourglassStart;
  faCheck = faCheck;
  observer!:any;
  income!: number;
  ordersAmount!: number;
  mealsAmount!: number;
  activeOrdersAmount!: number;
  lastUpdateTime!: Date;
  chosenDish!: ActiveOrder;
  chosenDishIndex!: number;
  view!: [number, number];
  chartData: Chart[] = [];
  dishes!: ActiveOrder[];

  constructor(private modalService: NgbModal, private orderService: OrderService,
              private zone: NgZone, private cd: ChangeDetectorRef, 
              private statisticService: StatisticService) {
  }

  ngOnInit(): void {
    this.lastUpdateTime = new Date();
    let restaurantId = localStorage.getItem('restaurantId');
    this.statisticService.getOrderAmountFromHours(restaurantId).subscribe(data => {
      this.chartData = [];
      this.chartData.push(data);
    }, error => {
      console.error(error);
    });
    this.orderService.getActiveOrders(restaurantId).subscribe(data => {
      this.dishes = data;
    }, error => {
      console.error(error);
    });
    this.getDailyIncome();
    this.getDailyOrdersAmount();
    this.getDailyMealsAmount();
    this.getDailyActiveOrdersAmount();
  }

  ngAfterViewInit() {
    let orderChart = document.getElementById('orderChart');
    this.view = [this.getElementWidth(orderChart), 400];
    this.cd.detectChanges();
    this.observer = new ResizeObserver(entries => {
      this.zone.run(() => {
        this.view = [entries[0].contentRect.width, 400];
        this.cd.detectChanges();
      });
    });
    if (orderChart)
      this.observer.observe(orderChart);
  }

  getElementWidth(element: any) {
    if (element) {
      return element.getBoundingClientRect().width;
    } else
      return 0;
  }

  open(content: any, dish: ActiveOrder, index: number) {
    this.chosenDish = dish;
    this.chosenDishIndex = index;
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
    });
  }

  getDailyIncome() {
    let restaurantId = localStorage.getItem('restaurantId');
    this.statisticService.getTodayIncome(restaurantId).subscribe(data => {
      this.income = data;
      this.lastUpdateTime = new Date();
    }, error => {
      console.error(error);
    });
  }

  getDailyOrdersAmount() {
    let restaurantId = localStorage.getItem('restaurantId');
    this.statisticService.getTodayDeliveredOrdersAmount(restaurantId).subscribe(data => {
      this.ordersAmount = data;
    }, error => {
      console.error(error);
    });
  }

  getDailyMealsAmount() {
    let restaurantId = localStorage.getItem('restaurantId');
    this.statisticService.getTodayDeliveredMealsAmount(restaurantId).subscribe(data => {
      this.mealsAmount = data;
    }, error => {
      console.error(error);
    });
  }

  getDailyActiveOrdersAmount() {
    let restaurantId = localStorage.getItem('restaurantId');
    this.statisticService.getActiveOrdersAmount(restaurantId).subscribe(data => {
      this.activeOrdersAmount = data;
    }, error => {
      console.error(error);
    })
  }

  completeOrder(modal: any) {
    if (this.chosenDish.orderType === 'Online') {
      this.orderService.completeOnlineOrder(this.chosenDish.id).subscribe(data => {
        this.dishes.splice(this.chosenDishIndex, 1);
        modal.close();
      }, error => {
        console.error(error);
      });
    } else {
      this.orderService.completeRestaurantOrder(this.chosenDish.id).subscribe(data => {
        this.dishes.splice(this.chosenDishIndex, 1);
        modal.close();
      }, error => {
        console.error(error);
      });
    }
  }

  ngOnDestroy() {
    let orderChart = document.getElementById('orderChart');
    if (orderChart)
      this.observer.unobserve(orderChart);
  }
}
