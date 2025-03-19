import {Component, Input} from '@angular/core';
import {NgClass, NgIf} from '@angular/common';

@Component({
  selector: 'app-chatmessage',
  standalone: true,
  imports: [
    NgClass,
    NgIf
  ],
  templateUrl: './chatmessage.component.html',
  styleUrl: './chatmessage.component.css'
})
export class ChatmessageComponent {
  @Input() outgoingMessage: boolean = false;
  @Input() message: string = '';
  @Input() senderName: string = '';
  @Input() senderProfileImage: string = '';
  @Input() isPrevSame!: boolean;

  constructor() {
    console.log(this.isPrevSame)
  }
}
