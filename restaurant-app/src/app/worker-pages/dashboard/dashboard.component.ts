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
import {ActiveOrder} from "../../model/active-order";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DailyOrdersAmount} from "../../model/daily-orders-amount";

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
  ordersAmount: DailyOrdersAmount[] = [
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
  ]
  chartData: any[] = [
    {
      name: "Liczba zamówień w ciągu dnia",
      series: []
    }
  ];
  dishes: ActiveOrder[] = [
    {
      id: 1,
      dishInfo: [
        {
          name: 'Śledź po węgiersku',
          amount: 1,
        },
        {
          name: 'Śledź po węgiersku',
          amount: 1,
        },
      ],
      orderDate: new Date(),
    },
    {
      id: 2,
      dishInfo: [
        {
          name: 'Śledź po węgiersku',
          amount: 1,
        },
        {
          name: 'Śledź po węgiersku',
          amount: 1,
        },
      ],
      orderDate: new Date(),
    },
    {
      id: 3,
      dishInfo: [
        {
          name: 'Śledź po węgiersku',
          amount: 1,
        }
      ],
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
