import {Component, EventEmitter, Input, Output} from '@angular/core';
import { NavigationComponent } from './navigation/navigation.component';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [NavigationComponent, NgClass],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css',
})
export class SidebarComponent {
  @Output() sidebarState = new EventEmitter<boolean>();
  @Input() isSidebarMinimized = false;

  toggleSidebarMinimization() {
    this.isSidebarMinimized = !this.isSidebarMinimized;
    this.sidebarState.emit(this.isSidebarMinimized);
  }
}
