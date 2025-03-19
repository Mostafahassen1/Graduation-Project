import { Component } from '@angular/core';
import {FriendCardComponent} from './friend-card/friend-card.component';
import { FriendshipService } from '../../services/friendship.service';
import { FriendshipResponse } from '../../models/friendship/friendship-response.dto';
import {RouterLink} from '@angular/router';
import {FriendRequestsComponent} from './friend-requests/friend-requests.component';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-friends',
  standalone: true,
  imports: [
    FriendCardComponent,
    RouterLink,
    FriendRequestsComponent,
    NgIf
  ],
  templateUrl: './friends.component.html',
  styleUrl: './friends.component.css'
})
export class FriendsComponent {
  friends:FriendshipResponse[]=[];
  RequestsShown! : boolean;
  requestsExists! : boolean;

  constructor(private friendshipService:FriendshipService)
  {

  }

  ngOnInit():void{
    this.getFriends(4);
    this.requestsExists = true; // will be true if there are friend requests.
  }

  getFriends(userId:number):void{
    this.friendshipService.getAllFriends(userId).subscribe((data:FriendshipResponse[])=>{
      this.friends=data;
      console.log(data);
    })
  }

  showRequests() : void {
    this.RequestsShown = true;
  }

  hideRequests() : void {
    this.RequestsShown = false;
  }
}
