package se.kau.cs.sy.match;

import se.kau.cs.sy.board.Board;

/**
 * This match configuration roughly resembles the classic board game configuration.
 * Mr. X stock of tickets is set to 1000 each for taxi, bus, and underground,
 * simulating the practically infinite amounts from the board game.
 * 
 * @author Sebastian Herold
 *
 */
public class ClassicConfiguration implements MatchConfiguration {

	private static Board board = Board.create();

	private static final int[] surfacing = new int[] {3, 8, 13, 18};

	private static final int[] starting = new int[] {13, 26, 29, 34, 50, 53, 91, 94, 103, 112, 117, 132, 138, 141, 155, 174, 197, 198};

	@Override
	public Board getBoard() {
		return board;
	}

	@Override
	public int[] getStartingPositions() {
		return starting;
	}

	@Override
	public int getTaxiTicketsPerDetective() {
		return 10;
	}

	@Override
	public int getBusTicketsPerDetective() {
		return 8;
	}

	@Override
	public int getUndergroundTicketsPerDetective() {
		return 4;
	}

	@Override
	public int getBlackTicketsPerDetective() {
		return 0;
	}

	@Override
	public int getTaxiTicketsForMrX() {
		return 1000;
	}

	@Override
	public int getBusTicketsForMrX() {
		return 1000;
	}

	@Override
	public int getUndergroundTicketsForMrX() {
		return 1000;
	}

	@Override
	public int getBlackTicketsForMrX() {
		return 5;
	}

	@Override
	public int getNumberOfTurns() {
		return 24;
	}

	@Override
	public int getNumberOfDetectives() {
		return 5;
	}

	@Override
	public int[] getSurfacingTurns() {
		return surfacing;
	}

}
