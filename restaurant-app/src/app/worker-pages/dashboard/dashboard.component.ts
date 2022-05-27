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
import {OrderType} from "../../model/order-type";
import {Chart} from "../../model/chart/chart";
import {ChartData} from "../../model/chart/chart-data";

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
  lastUpdateTime!: Date;
  chosenDish!: ActiveOrder;
  view!: [number, number];
  ordersAmount: ChartData[] = [
    {
      name: "10:00",
      value: 5,
    },
    {
      name: "11:00",
      value: 2,
    },
    {
      name: "12:00",
      value: 3,
    },
    {
      name: "13:00",
      value: 10,
    },
    {
      name: "14:00",
      value: 11,
    },
    {
      name: "15:00",
      value: 7,
    },
    {
      name: "16:00",
      value: 8,
    },
    {
      name: "17:00",
      value: 9,
    },
    {
      name: "18:00",
      value: 10,
    },
    {
      name: "19:00",
      value: 20,
    },
    {
      name: "20:00",
      value: 22,
    },
  ]
  chartData: Chart[] = [
    {
      Xlabel: 'Godziny',
      Ylabel: 'Liczba zamówień',
      name: "Liczba zamówień w ciągu dnia",
      series: []
    }
  ];
  dishes: ActiveOrder[] = [
    {
      id: 1,
      dishesInfo: [
        {
          name: 'Śledź po węgiersku',
          amount: 1,
        },
        {
          name: 'Śledź po węgiersku',
          amount: 1,
        },
      ],
      orderType: OrderType.ONLINE,
      orderDate: new Date(),
    },
    {
      id: 2,
      dishesInfo: [
        {
          name: 'Śledź po węgiersku',
          amount: 1,
        },
        {
          name: 'Śledź po węgiersku',
          amount: 1,
        },
      ],
      orderType: OrderType.RESTAURANT,
      orderDate: new Date(),
    },
    {
      id: 3,
      dishesInfo: [
        {
          name: 'Śledź po węgiersku',
          amount: 1,
        }
      ],
      orderType: OrderType.RESTAURANT,
      orderDate: new Date(),
    },
  ]

  constructor(private modalService: NgbModal,
              private zone: NgZone, private cd: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.lastUpdateTime = new Date();
    this.chartData[0].series = this.ordersAmount;
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

  open(content: any, dish: ActiveOrder) {
    this.chosenDish = dish;
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
    });
  }

  refreshDailyIncome() {
    //todo
  }

  completeOrder(modal: any) {
    //todo
    modal.close();
  }

  ngOnDestroy() {
    let orderChart = document.getElementById('orderChart');
    if (orderChart)
      this.observer.unobserve(orderChart);
  }
}
