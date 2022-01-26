import { Component, OnChanges, SimpleChange, Input } from '@angular/core';

import {MatGridList} from '@angular/material/grid-list';

import {MatchState} from '../match/match-state';
import {MatchManagementService} from '../match-management.service';

@Component({
  selector: 'app-move-history-panel',
  templateUrl: './move-history-panel.component.html',
  styleUrls: ['./move-history-panel.component.css']
})
export class MoveHistoryPanelComponent implements OnChanges {

  @Input()
  state: MatchState;
  tickets: string[];

  constructor(private mmService: MatchManagementService) { }

  ngOnChanges(changes: {[propKey: string]: SimpleChange}): void {
	  this.getMoveHistory(this.state.matchId);
  }
  
  getMoveHistory(matchId: string) {
	  this.mmService.getMoveHistory(matchId)
		.subscribe(data => this.tickets = data);
  }
}
