package se.kau.cs.sy.match.event;

import se.kau.cs.sy.match.state.MatchState;

/**
 * This class represent the change of the state of match as a {@link MatchEvent} instance.
 * @author Sebastian Herold
 *
 */
public class StateChangeEvent implements MatchEvent {

	private final MatchState oldState, newState;
	
	public StateChangeEvent(MatchState oldState, MatchState newState) {
		this.oldState = oldState;
		this.newState = newState;
	}

	/**
	 * Returns the state before the state change. Might be {@code null} if there was no previous state.
	 */
	public MatchState getOldState() {
		return this.oldState;
	}
	
	/**
	 * Returns the state after the state change.
	 * @return
	 */
	public MatchState getNewState() {
		return this.newState;
	}
}
