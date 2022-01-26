export interface MatchConfiguration {
	boardId: String;
	startingPositions: number[];
	surfacingTurns: number[];
	nrOfDetectives: number;
	nrOfTurns: number;
	taxiPerDetective: number;
	busPerDetective: number;
	undergroundPerDetective: number;
	blackPerDetective: number;
	taxiForMrX: number;
	busForMrX: number;
	undergroundForMrX: number;
	blackForMrX: number;
}
