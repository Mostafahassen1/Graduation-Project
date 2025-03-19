import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FriendCardComponent} from '../friend-card/friend-card.component';
import {FriendshipResponse} from '../../../models/friendship/friendship-response.dto';
import {NgClass} from '@angular/common';
import {Router} from '@angular/router';

@Component({
  selector: 'app-friend-requests',
  standalone: true,
  imports: [
    FriendCardComponent,
    NgClass
  ],
  templateUrl: './friend-requests.component.html',
  styleUrl: './friend-requests.component.css'
})
export class FriendRequestsComponent {
  requests! : boolean;

  @Output() requestsState = new EventEmitter<boolean>();

  testFriend: FriendshipResponse = {
    friendshipId: 1,
    friendFirstName: 'Ahmed',
    friendLastName: 'Mohamed',
    friendProfilePicture: 'https://placehold.co/65',
    friendUserName: 'ahMo214'
  };

  constructor(private router : Router) {
  }

  hideRequests() {
    this.router.navigateByUrl('/friends');
    this.requests = false;
    this.requestsState.emit(this.requests);
  }
}
