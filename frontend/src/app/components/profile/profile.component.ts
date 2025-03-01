import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  userId!: string | null;
  isOwnProfile: boolean = false;
  areFriends : boolean = false;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.userId = params.get('userName'); // Get the 'id' parameter from the URL
      this.isOwnProfile = !this.userId; // If no ID, it's the signed-in user's profile
    });
  }

  addFriend() {
    this.areFriends = true;
  }

  removeFriend() {
    this.areFriends = false;
  }
}
