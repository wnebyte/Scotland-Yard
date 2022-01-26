import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';

import {WebSocketService} from '../web-socket.service';
import {MatchManagementService} from '../match-management.service';
import {MatchState} from './match-state';
import {PlayerState} from './player-state';
import {Move} from '../available-moves/move';


import * as Stomp from 'stompjs';

@Component({
  selector: 'app-match',
  templateUrl: './match.component.html',
  styleUrls: ['./match.component.css']
})
export class MatchComponent implements OnInit {

  matchId: string;
  playerId: string;
  matchState: MatchState;
  hasMoved: boolean;
  private ws: WebSocket;
  private client: Stomp.Client;
  
  constructor(private wsService: WebSocketService,
			  private mmService: MatchManagementService,
			  private route: ActivatedRoute) { }

  ngOnInit(): void {
	  this.connectToWebSocket();
	  this.matchId = this.route.snapshot.paramMap.get('matchId');
	  this.playerId = this.route.snapshot.paramMap.get('playerId');
	  this.hasMoved = false;
	  this.getMatchState();
  }

  getMatchState() {
	this.mmService.getMatchState(this.matchId, this.playerId)
		.subscribe(state => {
			this.matchState = state;
			});
			
  }

  onMessage(update: MatchUpdate) {
	if (update.executed) {
		if (!update.stateUpdated && update.movingPlayer == this.playerId) {
			this.hasMoved = true;
		}
	  	else if (update.stateUpdated) {
			  this.hasMoved = false;
			  this.getMatchState();
		}
	}
	else { 

	}
  }

  onSelectedMove(move: Move) {
	  //send message
	  console.log(JSON.stringify(move));
	  var url = "/matches/" + this.matchId + "/moves";
	  this.client.send(url, {}, JSON.stringify(move));
  }
  
  connectToWebSocket() {
	  this.ws = this.wsService.getWebSocket();
	  this.client = Stomp.over(this.ws);
	  this.client.connect({}, () => {
		this.client.subscribe("/topic/" + this.matchId + "/moves", data => {
			this.onMessage(JSON.parse(data.body));
		});
	  });
  }
  

  
  disconnectFromWebSocket() {
	  if (this.client) {
		  this.ws.close();
		  this.client.unsubscribe("/topic/" + this.matchId + "/moves");
	  }
  }
}

export interface MatchUpdate {
	movingPlayer: string;
	stateUpdated: boolean;
	executed: boolean;
}
