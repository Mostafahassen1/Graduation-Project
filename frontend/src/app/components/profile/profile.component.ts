import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {NgIf} from '@angular/common';
import {ProfileEditComponent} from './profile-edit/profile-edit.component';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    NgIf,
    RouterLink,
    ProfileEditComponent
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  userName!: string | null;
  isOwnProfile: boolean = false;
  areFriends : boolean = false;
  editProfile: boolean = false;

  constructor(protected router: Router) {}

  ngOnInit() {
    if (this.router.url === '/profile')
      this.isOwnProfile = true;
  }

  addFriend() {
    this.areFriends = true;
  }

  removeFriend() {
    this.areFriends = false;
  }

  enableProfileEdit() {
    this.editProfile = true;
  }
}
