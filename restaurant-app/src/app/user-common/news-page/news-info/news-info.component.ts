import {AfterViewInit, Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {News} from "../../../model/news";

@Component({
  selector: 'app-news-info',
  templateUrl: './news-info.component.html',
  styleUrls: ['./news-info.component.scss']
})
export class NewsInfoComponent implements OnInit, AfterViewInit {
  newsId!: number;
  news: News = {
    newsId: 1,
    imageUrl: 'assets/mexican_food.jpg',
    title: 'Tydzień meksykański tylko u nas',
    content: '\n' +
      'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse a maximus nisl, eu ultricies nibh. Nam id tortor porta, aliquam eros congue, pellentesque nisi. Pellentesque ullamcorper dolor nec neque luctus facilisis. Donec dignissim nibh non lorem iaculis interdum. Nullam interdum sit amet ante sed mollis. Vestibulum vel lobortis risus. Fusce consectetur turpis vitae vulputate vulputate. Phasellus consequat at tortor vitae lacinia. Duis efficitur libero eget ante porta, a finibus sem congue. Donec at augue mauris. Mauris et porttitor mi. In eu ultrices quam, in egestas nunc. Aliquam convallis quam sed nisl pretium tempor. Nam condimentum ornare justo, a tempus est pharetra nec. Phasellus tincidunt, ex in pharetra blandit, nunc est pulvinar massa, sit amet dictum nibh erat eleifend sapien. Phasellus tincidunt ullamcorper nisl, et sollicitudin magna consectetur eget.<br><br>' +
      'Pellentesque erat mi, congue a ipsum consectetur, mattis molestie purus. Pellentesque non aliquam quam. Quisque at lorem id eros pretium porta sed id felis. Vestibulum sit amet lectus pellentesque, gravida nibh eu, malesuada risus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Ut vel tellus ut sem ultrices imperdiet. Phasellus ornare, mi eu semper condimentum, ligula diam sollicitudin lorem, ac vestibulum erat est id magna. Maecenas feugiat tincidunt nisl, id mattis odio porttitor dapibus. Morbi rhoncus ultrices mauris, ac porta felis. Phasellus convallis nec eros quis egestas. Donec ultricies id magna vel lacinia. Nulla ac tempor dolor, sed hendrerit dolor. Nullam hendrerit turpis purus, sit amet pulvinar felis tempus et. Aliquam ut enim imperdiet, dignissim lacus a, euismod ante. Proin id mi ullamcorper, bibendum augue ac, dignissim dui. Integer varius libero maximus odio accumsan, et elementum justo faucibus.<br><br>' +
      'Etiam vitae efficitur est. Praesent in nibh eget arcu porttitor dapibus. In hac habitasse platea dictumst. Praesent sed turpis massa. Nam rhoncus eget velit a ultrices. Nulla convallis, sem in condimentum vehicula, erat nisi viverra nulla, ac ullamcorper arcu tortor vehicula ante. Pellentesque sagittis iaculis sagittis.',
    date: new Date(),
  };

  constructor(private route: ActivatedRoute) {
    this.route.params.subscribe( params =>  this.newsId = params['id']);
  }

  ngOnInit(): void {
    // call to backend to receive full news info
  }

  ngAfterViewInit() {
    this.route.fragment.subscribe((fragment) => {
      let element = document.querySelector('#' + fragment);
      if (element)
        element.scrollIntoView();
    })
  }
}
