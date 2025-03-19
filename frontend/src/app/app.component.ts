import { Component, OnInit } from '@angular/core';
import { WebSocketService } from './services/websocket.service';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EntryComponent } from './components/entry/entry.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, CommonModule, EntryComponent, LoginComponent, SignupComponent, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title: string = '';
  message = '';

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit() {
    // Subscribe to WebSocket messages and update 'message' when new data arrives
    this.webSocketService.message$.subscribe(msg => {
      this.message = msg;
    });
  }

  connectWebSocket() {
    this.webSocketService.connect();
  }

  sendMessage() {
    this.webSocketService.sendMessage(this.message);

  }

  disconnectWebSocket() {
    this.webSocketService.disconnect();
  }
}
