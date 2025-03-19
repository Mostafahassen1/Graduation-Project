import {Component, OnInit, OnDestroy, ViewChild, ElementRef} from '@angular/core';
import { HeaderComponent } from './header/header.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import {NgClass} from '@angular/common';
import {HomeComponent} from '../home/home.component';
import {ActivatedRoute, RouterOutlet, NavigationEnd, Router} from '@angular/router';
import {filter, Subject, takeUntil} from 'rxjs';
import {ChatsComponent} from '../chats/chats.component';
import {ProfileComponent} from '../profile/profile.component';

@Component({
  selector: 'app-board',
  standalone: true,
  imports: [HeaderComponent, SidebarComponent, NgClass, HomeComponent, RouterOutlet],
  templateUrl: './board.component.html',
  styleUrl: './board.component.css',
})
export class BoardComponent {
  isSidebarMinimized!: boolean;
  noPadding : boolean = false;
  private chats : ChatsComponent | null = null;

  constructor(private router: Router) {}

  onActivated(Page : any) : void {
    if (Page instanceof ChatsComponent) {
      this.chats = Page;
      this.chats.sidebarMinimizationState.subscribe((message: string) => {
        this.noPadding = true;
        this.isSidebarMinimized = true;
      })
    } else if (Page instanceof ProfileComponent) {
      this.noPadding = true;
      this.isSidebarMinimized = true;
    } else {
      this.noPadding = false;
      this.isSidebarMinimized = false;
    }
  }

  getSidebarState(sidebarState: boolean) {
    this.isSidebarMinimized = sidebarState;
  }
}
