package se.kau.cs.sy.match;

import java.util.UUID;

import se.kau.cs.sy.match.event.MatchEventListener;

/**
 * This is the interface that players (whether they are detectives or Mr. X) have to implement.
 * Note that different instances are interpreted as different players regardless of their names
 * or other attributes. Or in short: {@code equals()} has not been redefined and checks for 
 * object equality.
 * @author Sebastian Herold
 *
 */
public interface Player extends MatchEventListener {

	/**
	 * Return a unique identifier for this player object.
	 * @return
	 */
	public UUID getId();
	
	/**
	 * Returns the name of a player
	 */
	public String getName();
	
	/**
	 * Returns the role of a player
	 */
	public PlayerRole getRole();
}
