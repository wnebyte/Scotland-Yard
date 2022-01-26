import { Component, OnInit, OnChanges, Input, SimpleChange } from '@angular/core';
import {MatchState} from '../match/match-state';
import {PlayerState} from '../match/player-state';
import {MatchManagementService} from '../match-management.service';

@Component({
  selector: 'app-match-state-panel',
  templateUrl: './match-state-panel.component.html',
  styleUrls: ['./match-state-panel.component.css']
})
export class MatchStatePanelComponent implements /*OnInit,*/ OnChanges {

  @Input()
  state: MatchState;
  surfacingTurns: number[] = [];
  isSurfacingTurn: boolean = false;
  nextSurfacingTurn: number = 0;
  mrxPosition: number = -1;
  
  constructor(private mmService: MatchManagementService) { }

/*  ngOnInit(): void {
	  this.getSurfacingTurns();
	  if (this.surfacingTurns.length > 0) {
		this.isSurfacingTurn = this.surfacingTurns.includes(this.state.turn);
		this.nextSurfacingTurn = Math.min(...this.surfacingTurns.filter(e => e > this.state.turn));
	  }
  }*/

  ngOnChanges(changes: {[propKey: string]: SimpleChange}): void {
	  this.getSurfacingTurns();
	  this.isSurfacingTurn = this.surfacingTurns.includes(this.state.turn);
	  this.nextSurfacingTurn = Math.min(...this.surfacingTurns.filter(e => e > this.state.turn));
	  this.mrxPosition = this.getMrXPosition();
  }
  
  getSurfacingTurns(): void {
	this.mmService.getSurfacingTurns(this.state.matchId)
		.subscribe(data => {
			console.log(data);
			this.surfacingTurns = data;
		});
  }
  
  getMrXPosition(): number {
	  let ps: PlayerState[] = this.state.playerStates
		.filter(p => p.role == "MR_X");
	  if (ps.length > 0) {
		  return ps[0].position;
	  }
	  else {
		  return -1;
	  }
  }
}
