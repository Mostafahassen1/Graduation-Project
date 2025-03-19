import { NgClass } from '@angular/common';
import {Component, ElementRef, ViewChild} from '@angular/core';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [NgClass, RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {
  isProfileDropdownOpen: boolean = false;
  isNotificationsDropdownOpen: boolean = false;
  @ViewChild('overlay') dropdownOverlay!: ElementRef<HTMLDivElement>;

  toggleProfileDropdown () : void {
    this.isProfileDropdownOpen = !this.isProfileDropdownOpen;
    this.isNotificationsDropdownOpen = false;
    this.dropdownOverlay.nativeElement.classList.remove('hidden');
  }

  toggleNotificationsDropdown () : void {
    this.isNotificationsDropdownOpen = !this.isNotificationsDropdownOpen;
    this.isProfileDropdownOpen = false;
    this.dropdownOverlay.nativeElement.classList.remove('hidden');
  }

  closeDropdown() {
    this.isProfileDropdownOpen = false;
    this.isNotificationsDropdownOpen = false;
    this.dropdownOverlay.nativeElement.classList.add('hidden');
  }
}
