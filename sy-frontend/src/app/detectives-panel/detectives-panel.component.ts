import { Component, OnChanges, Input, SimpleChange} from '@angular/core';

import {MatchState} from '../match/match-state';
import {PlayerState} from '../match/player-state';

@Component({
  selector: 'app-detectives-panel',
  templateUrl: './detectives-panel.component.html',
  styleUrls: ['./detectives-panel.component.css']
})
export class DetectivesPanelComponent implements OnChanges {

  @Input()
  state: MatchState;
  
  constructor() { }

	ngOnChanges(changes: {[propKey: string]: SimpleChange}) {
		let ps: PlayerState[] = this.state.playerStates
			.filter(p => p.role=="DETECTIVE");
		this.state.playerStates = ps;
	}

}
