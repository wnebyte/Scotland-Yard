package se.kau.cs.sy.match.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import se.kau.cs.sy.board.Link;
import se.kau.cs.sy.match.Match;
import se.kau.cs.sy.match.Move;
import se.kau.cs.sy.match.Player;
import se.kau.cs.sy.match.PlayerRole;

/**
 * The central class for representing the state of match.
 * <p>
 * In Scotland Yard, the match state consists of 
 * <p><ul>
 * <li> The states of the players
 * <li> The number of the turn
 * <li> Who's turn it is
 * </ul><p>
 * @author Sebastian Herold
 *
 */
public class MatchState {

	private Match match;
	
	private Map<Player, PlayerState> playerStates;

	private int turnNumber;

	private TurnState turnState;
	
	private Map<Player, Move> scheduledDetectiveMoves;
	
	/**
	 * Creates an initial match state, i.e. it is the first turn and it is M. X's turn.
	 * @param match The connected match
	 * @param playerStates Initial states per player
	 */
	public MatchState(Match match, Map<Player, PlayerState> playerStates) {
		this(match, playerStates, 1, TurnState.MRX_MOVING);
	}
	
	public MatchState(Match match, Map<Player, PlayerState> playerStates, int turnNumber, TurnState turnState) {
		this.match = match;
		this.playerStates = new HashMap<>();
		this.playerStates.putAll(playerStates);
		this.turnNumber = turnNumber;
		this.turnState = turnState;
		this.scheduledDetectiveMoves = new HashMap<>();
	}
	
	/**
	 * Copy constructor. A deep copy is created, i.e. player state object of the copied state are reused.
	 * @param state
	 */
	public MatchState(MatchState state) {
		this.match = state.match;
		this.playerStates = new HashMap<>();
		for (PlayerState ps : state.playerStates.values()) {
			this.playerStates.put(ps.getPlayer(),
					new PlayerState.Builder().copy(ps).build());
		}
		this.playerStates.putAll(state.playerStates);
		this.scheduledDetectiveMoves = new HashMap<>();
		this.scheduledDetectiveMoves.putAll(state.scheduledDetectiveMoves);
		turnNumber = state.turnNumber;
		turnState = state.turnState;
	}	

	/**
	 * Creates a copy of the given match state and blackens the player state for Mr X in case
	 * unless it's a turn in which he has to reveal himself.
	 * @param state The given state.
	 * @return The blackened copy.
	 */
	public static MatchState blacken(MatchState state) {
		MatchState blackenedState = new MatchState(state);
		PlayerState mrxState = blackenedState.getMrXState();
		if (!mrxState.isVisible()) {
			blackenedState.playerStates.remove(mrxState.getPlayer());
		}
		return blackenedState;
	}
	
	/**
	 * Creates a new state resulting from Mr X performing a move in the current state.
	 * @param move The move to be performed.
	 * @return The new state if {@code move} is a legal move. {@code null} otherwise.
	 */
	public MatchState moveMrX(Move move) {
		MatchState resultingState;
		if (turnState != TurnState.MRX_MOVING) return null;	//TODO special case object
		Player player = getMrXState().getPlayer();
		if (player != move.player) return null;
		if (isLegal(move)) {
			//create new state with:
			resultingState = new MatchState(this);
			PlayerState.Builder psBuilder =  new PlayerState.Builder()
				.moveFrom(getMrXState(), move);
			
			psBuilder.visible(IntStream.of(match.getSurfacingTurns()).anyMatch(e -> e == resultingState.getTurnNumber()));
			resultingState.playerStates.put(player, psBuilder.build());
			resultingState.turnState = TurnState.DETECTIVES_MOVING;
			return resultingState;
		}
		else {
			//TODO Special case object
			return null;
		}
	}
	
	/**
	 * Returns the associated match.
	 */
	public Match getMatch() {
		return match;
	}
	
	/**
	 * Schedules the move of a detective.
	 * @param move The move to be performed
	 * @return {@code true} if the specified move is legal and could be scheduled, {@code false} otherwise.
	 */
	public synchronized boolean scheduleDetectiveMove(Move move) {
		if (turnState != TurnState.DETECTIVES_MOVING) return false;
		if (move.player.getRole() != PlayerRole.DETECTIVE) return false;
		if (isLegal(move)) {
			scheduledDetectiveMoves.put(move.player, move);
			return true;
		}
		return false;
		
	}
	
	/**
	 * Performs the moves of the detectives collectively if all detectives' moves have been scheduled.
	 * @return the new state if all detectives are scheduled, {@code null} otherwise.
	 */
	public MatchState moveDetectives() {
		MatchState resultingState = null;
		if (turnState != TurnState.DETECTIVES_MOVING) return null;
		if (allDetectivesPlanned()) {
			resultingState = new MatchState(this);
			for (Move move : scheduledDetectiveMoves.values()) {
				PlayerState ps = new PlayerState.Builder()
						.moveFrom(playerStates.get(move.player), move)
						.build();
				resultingState.playerStates.put(move.player, ps);
			}
			resultingState.scheduledDetectiveMoves.clear();
			resultingState.turnNumber++;
			if (resultingState.caughtMrX()) {
				resultingState.turnState = TurnState.DETECTIVES_WON;
			}
			else if (resultingState.playerIsStuck(resultingState.getMrXState().getPlayer())) {
				resultingState.turnState = TurnState.DETECTIVES_WON;
			}
			else if (resultingState.allDetectivesAreStuck()) {
				resultingState.turnState = TurnState.MRX_WON;
			}
			else if (resultingState.turnNumber > resultingState.match.getMaxTurns()) {
				resultingState.turnState = TurnState.MRX_WON;
			}
			else {
				resultingState.turnState = TurnState.MRX_MOVING;
			}
		}
		return resultingState;
	}
	
	
	private boolean playerIsStuck(Player player) {
		return getAllPossibleMoves(player).size() == 0;
	}
	
	/**
	 * Returns all the possible legal moves of a player in this match state.
	 * @param player the player of interest.
	 * @return A (possibly empty) set of moves.
	 */
	public Set<Move> getAllPossibleMoves(Player player) {

		Set<Move> result = new HashSet<>();
		PlayerState ps = getPlayerState(player);
		Set<Link> links = match.getBoard().getLinks(ps.position);
		for (Link link : links) {
			int linkEnds[] = link.getNodes();
			int neighbour = linkEnds[0] == ps.getPosition() ? linkEnds[1] : linkEnds[0]; 
			Move regMove = new Move(player, neighbour, link.getType(), false);
			Move blkMove = new Move(player, neighbour, link.getType(), true);
			if (isLegal(regMove)) {
				result.add(regMove);
			}
			if (isLegal(blkMove)) {
				result.add(blkMove);
			}
		}
		return result;
	}
	
	private Set<Player> getDetectives() {
		Set<Player> detectives = new HashSet<>(playerStates.keySet());
		detectives.removeIf(p -> p.getRole() != PlayerRole.DETECTIVE);
		return detectives;
	}
	
	private Set<Player> getStuckDetectives() {
		Set<Player> stuck = getDetectives();
		stuck.removeIf(p -> !playerIsStuck(p));
		return stuck;
	}
	
	private boolean allDetectivesAreStuck() {
		for (Player p : playerStates.keySet()) {
			if (p.getRole() == PlayerRole.DETECTIVE) {
				if (!playerIsStuck(p)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns the turn state (who's turn it is) of this state 
	 */
	public TurnState getTurnState() {
		return turnState;
	}

	/**
	 * Return the turn number of this state.
	 * @return
	 */
	public int getTurnNumber() {
		return turnNumber;
	}
	
	/**
	 * Checks if all detectives have been scheduled
	 */
	public boolean allDetectivesPlanned() {
		Set<Player> detectives = getDetectives();
		detectives.removeAll(getStuckDetectives());
		for (Player p : scheduledDetectiveMoves.keySet()) {
			detectives.remove(p);
		}
		return detectives.isEmpty();
	}
	
	private boolean isLegal(Move move) {
		boolean legal;
		PlayerState ps = getPlayerState(move.player); 
		int from = ps.getPosition();
		//check destination exists
		legal = match.getBoard().nodeExists(move.to);
		//check connection on map: nodes connected via type
		legal = legal && match.getBoard().connected(from, move.to, move.type);
		//check ticket availability
		if (move.black) {
			legal = legal && ps.hasBlackTicket();
		}
		else {
			legal = legal && ps.hasRegularTicketFor(move.type);
		}
		//check destination occupied by detective
		legal = legal && !isOccupiedPosition(move.to);
		return legal;
	}

	/**
	 * Return the state of a specific player
	 */
	public PlayerState getPlayerState(Player pl) {
		return playerStates.get(pl);
	}
	
	public Set<PlayerState> getPlayerStates() {
		Set<PlayerState> states = new HashSet<>();
		states.addAll(playerStates.values()); 
		return states;
	}
	
	/**
	 * Returns the state of the player being Mr X
	 * @return
	 */
	public PlayerState getMrXState() {
		for (PlayerState ps : playerStates.values()) {
			if (ps.getPlayer().getRole() == PlayerRole.MR_X) {
				return ps;
			}
		}
		return null;
	}
	
	private synchronized boolean isOccupiedPosition(int pos) {
		return getOccupiedPositions().contains(pos);
	}
	
	private synchronized Set<Integer> getOccupiedPositions() {
		Set<Integer> result = new HashSet<Integer>();
		Set<Player> movedPlayers = new HashSet<>();
		for (Move move : scheduledDetectiveMoves.values()) {
			result.add(move.to);
			movedPlayers.add(move.player);
		}
		for (Player p : playerStates.keySet()) {
			if (p.getRole() == PlayerRole.DETECTIVE && !movedPlayers.contains(p)) {
				result.add(playerStates.get(p).getPosition());
			}
		}
		return result;
	}
	
	private boolean caughtMrX() {
		PlayerState mrxState = getMrXState();
		for (PlayerState ps : playerStates.values()) {
			if (ps.getPlayer().getRole() == PlayerRole.DETECTIVE) {
				if (ps.getPosition() == mrxState.getPosition()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public String toString() {
		String result = "Turn " + getTurnNumber() +
				", " + getTurnState() + "\n" + getMrXState() + "\n";
		for (Player p : getDetectives()) {
			result += getPlayerState(p) + "\n";
		}
		return result;
	}
}
