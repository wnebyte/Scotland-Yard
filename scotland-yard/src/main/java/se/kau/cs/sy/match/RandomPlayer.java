package se.kau.cs.sy.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import se.kau.cs.sy.match.event.AbstractMatchEventListener;
import se.kau.cs.sy.match.event.StateChangeEvent;
import se.kau.cs.sy.match.state.MatchState;
import se.kau.cs.sy.match.state.TurnState;

/**
 * A proof-of-concept {@link Player} implementation that randomly moves a player to
 * any of the positions legally reachable from its current position. 
 * @author Sebastian Herold
 *
 */
public class RandomPlayer extends AbstractMatchEventListener implements Player {

	private UUID id;

	private String name;

	private PlayerRole role;
	
	private static int detectiveSuffix = 1;
	
	public RandomPlayer(PlayerRole role) {
		this.id = UUID.randomUUID();
		this.role = role;
		if (role == PlayerRole.DETECTIVE) {
			name = "Random Detective " + detectiveSuffix++;
		}
		else {
			name = "Random Mr. X";
		}
	}
	
	public UUID getId() {
		return id;
	}
	
	@Override
	public void onStateChange(StateChangeEvent event) {
		MatchState state = event.getNewState();
		if (!state.getMatch().isRegisteredPlayer(this)) return;
		boolean moved = false;
		if (myTurn(state)) {
			List<Move> moves = new ArrayList<>(state.getAllPossibleMoves(this));
			if (!moves.isEmpty()) {
				Collections.shuffle(moves);
				while (!moved && !moves.isEmpty()) {
					moved = state.getMatch().performMove(moves.get(0));
					moves.remove(0);
				}
			}
		}
	}

	private boolean myTurn(MatchState state) { 
		return state.getTurnState() == TurnState.DETECTIVES_MOVING && role == PlayerRole.DETECTIVE ||
				state.getTurnState() == TurnState.MRX_MOVING && role == PlayerRole.MR_X;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public PlayerRole getRole() {
		return role;
	}
}
