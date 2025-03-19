import {Component, EventEmitter, Output} from '@angular/core';
import {RecentChatsComponent} from './recentchats/recentchats.component';
import {ChatboxComponent} from './chatbox/chatbox.component';
import {RouterOutlet} from '@angular/router';
import {Chat} from '../../models/chats/chat';
import data from '../../../../public/chats.json';

@Component({
  selector: 'app-chats',
  standalone: true,
  imports: [
    RecentChatsComponent,
    ChatboxComponent,
    RouterOutlet
  ],
  templateUrl: './chats.component.html',
  styleUrl: './chats.component.css'
})
export class ChatsComponent {
  @Output() sidebarMinimizationState = new EventEmitter<boolean>();
  isSidebarMinimized: boolean = false;

  chats: Chat[] = data;

  constructor() {}

  ngOnInit() {
    this.isSidebarMinimized = true;
    this.sidebarMinimizationState.emit(this.isSidebarMinimized);
  }

}
