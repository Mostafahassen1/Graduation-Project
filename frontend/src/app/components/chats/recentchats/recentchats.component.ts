import {Component, Input} from '@angular/core';
import {ChatcardComponent} from './chatcard/chatcard.component';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import {Chat} from '../../../models/chats/chat';

@Component({
  selector: 'app-recentchats',
  standalone: true,
  imports: [
    ChatcardComponent,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './recentchats.component.html',
  styleUrl: './recentchats.component.css'
})
export class RecentChatsComponent {
  @Input() chats!: Chat[];
}
