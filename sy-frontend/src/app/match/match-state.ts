import {PlayerState} from './player-state'; 

export interface MatchState {
	matchId : string;
	playerStates : PlayerState[];
	turn : number;
	turnState : string;
}
