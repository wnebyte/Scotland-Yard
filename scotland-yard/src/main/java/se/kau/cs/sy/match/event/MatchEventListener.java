package se.kau.cs.sy.match.event;

import se.kau.cs.sy.match.Match;

/**
 * Interface for objects that need to react to events in a match.
 * It is recommended not to implement this interface directly but to derive
 * new implementation from {@link AbstractMatchEventListener}.
 * @author sebahero
 *
 */
public interface MatchEventListener {

	/**
	 * Called in case of match state changes
	 */
	public void onStateChange(StateChangeEvent event);

	/**
	 * Method invoked by {@link Match#fire} to notify listeners
	 * Implementations should be non-blocking.
	 * @param event
	 */
	public void onEvent(MatchEvent event);
}
