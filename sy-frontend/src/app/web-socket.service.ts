import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  private hostServer  = window.location.hostname;
  private wsUrl = "ws://" + this.hostServer + ":7001/websocket";
  
  constructor() { }
  
  getWebSocket(): WebSocket {
	return new WebSocket(this.wsUrl);
  }
}
