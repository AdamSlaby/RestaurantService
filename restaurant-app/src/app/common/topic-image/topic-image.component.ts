import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-topic-image',
  templateUrl: './topic-image.component.html',
  styleUrls: ['./topic-image.component.scss']
})
export class TopicImageComponent implements OnInit {
  @Input() urlImage!: string;
  @Input() minHeight!: number;
  @Input() content!: string;

  constructor() { }

  ngOnInit(): void {
  }

}
