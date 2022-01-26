package se.kau.cs.sy.match;

import se.kau.cs.sy.board.TransportType;

/**
 * Represents a move in a match of Scotland Yard.
 * The accessible fields stand for player to move, destination of the move, type of transportation,
 * and whether or not a black ticket is to be used.
 * @author Sebastian Herold
 *
 */
public class Move {
	
	public final Player player;

	public final int to;

	public final TransportType type;

	public final boolean black;

	public Move(Player p, int to, TransportType type, boolean black) {
		this.player = p;
		this.to = to;
		this.type = type;
		this.black = black;
	}
	
	public String toString() {
		return player.getName() + " moves to " + to + " via "
				+ type + (black ? " (secretly)" : "");
	}
}
