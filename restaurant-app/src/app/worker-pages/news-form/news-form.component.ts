import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {faXmark, faNewspaper} from "@fortawesome/free-solid-svg-icons";
import {NgbCalendar} from "@ng-bootstrap/ng-bootstrap";
import {News} from "../../model/news/news";
import {FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'app-news-form',
  templateUrl: './news-form.component.html',
  styleUrls: ['./news-form.component.scss']
})
export class NewsFormComponent implements OnInit {
  @Input() newsId: any;
  @Output() closeNewsDetails = new EventEmitter<void>();
  faXmark = faXmark;
  faNewspaper = faNewspaper;
  errors: Map<string, string> = new Map<string, string>();
  loading = false;
  minDate = this.calendar.getToday();
  newsInfo!: News;
  imageUrl: any;
  news: FormData = new FormData();
  newsForm = this.fb.group({
    image: ['', [Validators.required]],
    title: ['', [Validators.required, Validators.maxLength(200)]],
    content: ['', [Validators.required, Validators.maxLength(2500)]],
  });

  constructor(private calendar: NgbCalendar, private fb: FormBuilder) { }

  ngOnInit(): void {
    if (this.newsId !== -1) {
      this.newsInfo = {
        employeeName: 'Marek Bykowski',
        info: {
          newsId: 1,
          imageUrl: 'assets/mexican_food.jpg',
          title: 'Tydzień meksykański tylko u nas',
          content:
            'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse a maximus nisl, eu ultricies nibh. Nam id tortor porta, aliquam eros congue, pellentesque nisi. Pellentesque ullamcorper dolor nec neque luctus facilisis. Donec dignissim nibh non lorem iaculis interdum. Nullam interdum sit amet ante sed mollis. Vestibulum vel lobortis risus. Fusce consectetur turpis vitae vulputate vulputate. Phasellus consequat at tortor vitae lacinia. Duis efficitur libero eget ante porta, a finibus sem congue. Donec at augue mauris. Mauris et porttitor mi. In eu ultrices quam, in egestas nunc. Aliquam convallis quam sed nisl pretium tempor. Nam condimentum ornare justo, a tempus est pharetra nec. Phasellus tincidunt, ex in pharetra blandit, nunc est pulvinar massa, sit amet dictum nibh erat eleifend sapien. Phasellus tincidunt ullamcorper nisl, et sollicitudin magna consectetur eget.<br><br>' +
            'Pellentesque erat mi, congue a ipsum consectetur, mattis molestie purus. Pellentesque non aliquam quam. Quisque at lorem id eros pretium porta sed id felis. Vestibulum sit amet lectus pellentesque, gravida nibh eu, malesuada risus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Ut vel tellus ut sem ultrices imperdiet. Phasellus ornare, mi eu semper condimentum, ligula diam sollicitudin lorem, ac vestibulum erat est id magna. Maecenas feugiat tincidunt nisl, id mattis odio porttitor dapibus. Morbi rhoncus ultrices mauris, ac porta felis. Phasellus convallis nec eros quis egestas. Donec ultricies id magna vel lacinia. Nulla ac tempor dolor, sed hendrerit dolor. Nullam hendrerit turpis purus, sit amet pulvinar felis tempus et. Aliquam ut enim imperdiet, dignissim lacus a, euismod ante. Proin id mi ullamcorper, bibendum augue ac, dignissim dui. Integer varius libero maximus odio accumsan, et elementum justo faucibus.<br><br>' +
            'Etiam vitae efficitur est. Praesent in nibh eget arcu porttitor dapibus. In hac habitasse platea dictumst. Praesent sed turpis massa. Nam rhoncus eget velit a ultrices. Nulla convallis, sem in condimentum vehicula, erat nisi viverra nulla, ac ullamcorper arcu tortor vehicula ante. Pellentesque sagittis iaculis sagittis.',
          date: new Date(),
        },
      };
      this.newsForm.patchValue({
        title: this.newsInfo.info.title,
        content: this.newsInfo.info.content,
      });
    }
  }

  get f() {
    return this.newsForm.controls;
  }

  onNewsFormSubmit() {
    //todo
    this.news.set('image', this.newsForm.get('image')?.value);
    this.news.set('title', this.newsForm.get('title')?.value);
    this.news.set('content', this.newsForm.get('content')?.value);
    console.log(this.news.get('title'));
  }

  closeComponent() {
    this.closeNewsDetails.emit();
  }

  getInvalidControl() {
    let controls = this.f;
    for (let name in controls) {
      if (controls[name].invalid)
        console.log(controls[name]);
    }
    return true;
  }

  uploadPhoto($event: any) {
    if ($event.target.files[0]) {
      this.newsForm.patchValue({
        image: $event.target.files[0],
      });
      const reader = new FileReader();
      reader.onload = () => {
        this.imageUrl = reader.result as string;
      }
      reader.readAsDataURL($event.target.files[0]);
    } else {
      this.newsForm.patchValue({
        image: null,
      });
    }
    console.log(this.newsForm.get('image')?.value);
  }
}
