package se.kau.cs.sy.match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import se.kau.cs.sy.board.Board;
import se.kau.cs.sy.board.TransportType;
import se.kau.cs.sy.match.event.MatchEvent;
import se.kau.cs.sy.match.event.MatchEventListener;
import se.kau.cs.sy.match.event.StateChangeEvent;
import se.kau.cs.sy.match.state.MatchState;
import se.kau.cs.sy.match.state.PlayerState;
import se.kau.cs.sy.match.state.TurnState;
import se.kau.cs.sy.util.GeneralUtils;

/**
 * Instances of this class are the entry points to matches. 
 * A match goes through three different phases.
 * 
 * First, during initialization, player can register for the match.
 * As soon as one player has registered as Mr X and the number of 
 * registered detectives matches the specified number from the used
 * configuration, the match enters the "Running" phase.
 * Eventually, when any party has won, it enters the "Finished" phase.
 * 
 * While the game is running, the current state of the match is reflected
 * in a corresponding MatchState object. The state changes if (a) Mr. X completed
 * a turn or (b) all the detectives moved. In both cases, all registered players
 * and other interested {@link MatchEventListener} objects are notified.
 *  
 * @author Sebastian Herold
 *
 */
public class Match {

	private UUID id;

	private List<MatchState> states = new ArrayList<>();

	private List<TransportType> mrxHistory = new ArrayList<>();

	private Set<Player> detectives = new HashSet<Player>();

	private Player mrx = null;
	
	private MatchConfiguration config;

	private Phase phase;
	
	private Set<MatchEventListener> eventListeners = new HashSet<>();
	
	public static enum Phase {
		INIT,
		RUNNING,
		FINISHED
	}
	
	/**
	 * Creates an instance in init phase.
	 * @param config The match configuration to be used
	 */
	public Match(MatchConfiguration config) {
		this.id = UUID.randomUUID();
		this.config = config;
		this.phase = Phase.INIT;
	}
	
	public UUID getId() {
		return id;
	}
	
	public Player getPlayer(UUID id) {
		if (mrx.getId().compareTo(id) == 0) return mrx;
		for (Player p : detectives) {
			if (p.getId().compareTo(id) == 0) return p;
		}
		return null;
	}
	
	/**
	 * Retrieves the board to be used for this match
	 */
	public Board getBoard() {
		return config.getBoard();
	}

	/**
	 * Return the current match state
	 * @return The current match state if match is running or has finished. {@code null} during init.
	 */
	public synchronized MatchState getCurrentState() {
		if (phase == Phase.INIT) return null;
		return states.get(states.size() - 1);
	}
	
	public List<TransportType> getMrXMoveHistory() {
		List<TransportType> result = new ArrayList<>();
		result.addAll(mrxHistory);
		return result;
	}
	
	/**
	 * Checks whether all players have been registered
	 * @return {@code true} if all players are registered, {@false} otherwise.
	 */
	public synchronized boolean allPlayersRegistered() {
		return mrx != null && detectives.size() == config.getNumberOfDetectives();
	}
	
	/**
	 * Checks if a given player is registered with the match
	 * @param p The player of interest.
	 * @return {@code true} if {@code p} is registered, {@code false} otherwise.
	 */
	public synchronized boolean isRegisteredPlayer(Player p) {
		return mrx == p || detectives.contains(p);
	}
	
	/**
	 * Returns the phase the match is currently in
	 * 
	 */
	public synchronized Phase getPhase() {
		return phase;
	}
	
	private synchronized boolean updatePhase() {
		boolean updated = false;
		if (phase == Phase.INIT) {
			if (allPlayersRegistered() && mrx != null) {
				startMatch();
				phase = Phase.RUNNING;
				updated = true;
			}
		}
		else if (phase == Phase.RUNNING) {
			TurnState turnState = getCurrentState().getTurnState();
			if (turnState == TurnState.DETECTIVES_WON || turnState == TurnState.MRX_WON) {
				phase = Phase.FINISHED;
				updated = true;
			}
		}
		return updated;
	}
	
	private synchronized void startMatch() {
		MatchState initialState;
		Map<Player, PlayerState> playerStates = new HashMap<>();
	
		int[] shuffledStartPos = GeneralUtils.randomizeArray(config.getStartingPositions());
		PlayerState ps = placeMrX(shuffledStartPos[0]);
		playerStates.put(mrx, ps);
		int i = 1;
		for (Player p : detectives) {
			ps = placeDetective(p, shuffledStartPos[i++]);
			playerStates.put(p, ps);
		}
		initialState = new MatchState(this, playerStates);
		states.add(initialState);
		fire(new StateChangeEvent(null, initialState));
	}
	
	private PlayerState placeMrX(int pos) {
		PlayerState ps = new PlayerState.Builder(mrx)
			.taxi(config.getTaxiTicketsForMrX())
			.bus(config.getBusTicketsForMrX())
			.underground(config.getUndergroundTicketsForMrX())
			.black(config.getBlackTicketsForMrX())
			.visible(false)
			.position(pos)
			.build();
		return ps;
	}
	
	private PlayerState placeDetective(Player p, int pos) {
		PlayerState ps = new PlayerState.Builder(p)
			.taxi(config.getTaxiTicketsPerDetective())
			.bus(config.getBusTicketsPerDetective())
			.underground(config.getUndergroundTicketsPerDetective())
			.black(config.getBlackTicketsPerDetective())
			.visible(true)
			.position(pos)
			.build();
		return ps;
	}
	
	
	/**
	 * Registers player as detective.
	 * @param p The player to be registered. {@code p.getRole()} must indicate p is a detective. 
	 * @return {@code true} if the player could be registered, {@code false} if registration failed.
	 * Possible reasons for failed registrations are that the player has already been registered, 
	 * the maximal number of detective has already been registered, or the player is not a detective.
	 */
	public synchronized boolean registerDetective(Player p) {
		boolean registered = false;
		if (p.getRole() == PlayerRole.DETECTIVE && !detectives.contains(p) &&
				mrx != p && detectives.size() < config.getNumberOfDetectives()) {
			detectives.add(p);
			eventListeners.remove(p);
			eventListeners.add(p);
			updatePhase();
			registered = true;
		}
		return registered;
	}
	
	/**
	 * Register a player as Mr. X
	 * @param p The player to be registered. {@code p.getRole()} must indicate p is Mr. X. 
	 * @return {@code true} if the player could be registered, {@code false} if registration failed.
	 * Possible reasons for failed registrations are that the player has already been registered, 
	 * the maximal number of detective has already been registered, or the player is not Mr. X.
	 */
	public synchronized boolean registerMrX(Player p) {
		boolean registered = false;
		if (p.getRole() == PlayerRole.MR_X && mrx == null && !detectives.contains(p)) {
			mrx = p;
			eventListeners.remove(p);
			eventListeners.add(p);
			registered = true;
			updatePhase();
		}
		return registered;
	}
	
	/**
	 * Registers a listener to the match.
	 * @param l The listener to be added
	 * @return {@code true} if registration was successful, {@code} false otherwise.
	 */
	public synchronized boolean registerListener(MatchEventListener l) {
		boolean registered = false;
		if (!eventListeners.contains(l)) {
			eventListeners.add(l);
			registered = true;
		}
		return registered;
	}
	
	/**
	 * Performs a move in the current match state. In case, the move is a legal move to be performed
	 * by Mr. X, the match changes state. In case, the move is a legal move performed by a detective,
	 * and all other detectives have moved already, the match changes state, too. If there are detectives left
	 * to be moved, this move is scheduled and the match state remains unaltered.
	 * 
	 * If the match state changes, all listeners are notified.
	 * @param move the move to be performed.
	 * @return {@code true} if the move is legal, i.e. it could be performed. {@code false} otherwise.
	 */
	public synchronized boolean performMove(Move move) {
		MatchState state = getCurrentState();
		MatchState nextState = null;
		boolean legal = false;
		if (move.player.getRole() == PlayerRole.DETECTIVE) {
			legal = state.scheduleDetectiveMove(move);
			if (state.allDetectivesPlanned()) {
				nextState = state.moveDetectives();
			}
			else {
				return legal;
			}
		}
		else if (move.player.getRole() == PlayerRole.MR_X) {
			nextState = state.moveMrX(move);
			if (nextState != null) {
				mrxHistory.add(move.black ? TransportType.UNKNOWN : move.type);
			}
		}
		if (nextState != null) {
			legal = true;
			this.states.add(nextState);
			updatePhase();
			fire(new StateChangeEvent(state, nextState));
		}
		return legal;
	}
	
	/**
	 * Returns the turn numbers at which Mr. X surfaces.
	 */
	public int[] getSurfacingTurns() {
		return config.getSurfacingTurns();
	}
	
	/**
	 * Returns the maximal number of turns.
	 */
	public int getMaxTurns() {
		return config.getNumberOfTurns();
	}

	public synchronized int getRegisteredDetectives( ) {
		return detectives.size();
	}
	
	public int getNumberOfDetectives() {
		return config.getNumberOfDetectives();
	}
	
	private void fire(MatchEvent event) {
		for (MatchEventListener listener : eventListeners) {
			listener.onEvent(event);
		}
	}
}
