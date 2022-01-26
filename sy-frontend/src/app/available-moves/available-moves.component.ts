import { Component, Input, OnChanges, SimpleChange, Output, EventEmitter } from '@angular/core';

import { MatchState } from '../match/match-state';
import {MatchManagementService} from '../match-management.service';
import {Move} from './move';

@Component({
  selector: 'app-available-moves',
  templateUrl: './available-moves.component.html',
  styleUrls: ['./available-moves.component.css']
})
export class AvailableMovesComponent implements OnChanges {

	@Input()
	state: MatchState;
	@Input()
	matchId: string;
	@Input()
	playerId: string;
	@Output()
	selectedMove = new EventEmitter<Move>();
	
	moves: Move[];
	
	constructor(private mmService: MatchManagementService) { }

	ngOnChanges(changes: {[propKey: string]: SimpleChange}) {
		this.getAvailableMoves();
	}

	onMoveSelected(move: Move) {
		this.selectedMove.emit(move);
	}
  
	getAvailableMoves() {
		this.mmService.getAvailableMoves(this.matchId, this.playerId)
			.subscribe(moves => this.moves = moves);
	}
}
