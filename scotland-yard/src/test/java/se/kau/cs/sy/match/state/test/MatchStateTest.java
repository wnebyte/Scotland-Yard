package se.kau.cs.sy.match.state.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import se.kau.cs.sy.board.Board;
import se.kau.cs.sy.board.TransportType;
import se.kau.cs.sy.match.ClassicConfiguration;
import se.kau.cs.sy.match.Match;
import se.kau.cs.sy.match.Move;
import se.kau.cs.sy.match.Player;
import se.kau.cs.sy.match.state.MatchState;
import se.kau.cs.sy.match.state.PlayerState;
import se.kau.cs.sy.match.state.TurnState;

public class MatchStateTest {

	private static Board board = Board.create();
	private PlayerState holmes67,
		holmes11,
		holmes11stuck,
		wallander14,
		wallander14stuck,
		wallander86,
		mrx127, mrx4, mrx13;
	private MatchState defaultState, mrxBlockedState, mrxCaughtState, detectivesStuckState, finalMoveState;
	
	
	@Before
	public void setUp() throws Exception {
		holmes67 = new PlayerState.Builder(DetectiveTestStub.holmes)
				.position(67)
				.taxi(10)
				.bus(8)
				.underground(4)
				.build();
		wallander86 = new PlayerState.Builder(DetectiveTestStub.wallander)
				.position(86)
				.taxi(10)
				.bus(8)
				.underground(4)
				.build();
		mrx127 = new PlayerState.Builder(MrXTestStub.instance)
				.position(127)
				.taxi(100)
				.bus(100)
				.underground(100)
				.black(4)
				.visible(false)
				.build();
		holmes11 = new PlayerState.Builder(DetectiveTestStub.holmes)
				.position(11)
				.taxi(10)
				.bus(8)
				.underground(4)
				.build();
		holmes11stuck = new PlayerState.Builder(DetectiveTestStub.holmes)
				.position(11)
				.bus(8)
				.taxi(1)
				.underground(4)
				.build();
		wallander14 = new PlayerState.Builder(DetectiveTestStub.wallander)
				.position(14)
				.taxi(10)
				.bus(8)
				.underground(4)
				.build();
		wallander14stuck = new PlayerState.Builder(DetectiveTestStub.wallander)
				.position(14)
				.taxi(1)
				.underground(4)
				.build();
		mrx4 = new PlayerState.Builder(MrXTestStub.instance)
				.position(4)
				.taxi(100)
				.bus(100)
				.underground(100)
				.black(4)
				.visible(false)
				.build();
		mrx13 = new PlayerState.Builder(MrXTestStub.instance)
				.position(13)
				.taxi(100)
				.bus(100)
				.underground(100)
				.black(4)
				.visible(false)
				.build();
		
		
		Match match = new Match(new ClassicConfiguration());
		Map<Player, PlayerState> playerStates = new HashMap<>();
		playerStates.put(holmes67.getPlayer(), holmes67);
		playerStates.put(wallander86.getPlayer(), wallander86);
		playerStates.put(mrx127.getPlayer(), mrx127);
		defaultState = new MatchState(match, playerStates);
		
		playerStates = new HashMap<>();
		playerStates.put(holmes11.getPlayer(), holmes11);
		playerStates.put(wallander14.getPlayer(), wallander14);
		playerStates.put(mrx4.getPlayer(), mrx4);
		mrxBlockedState = new MatchState(match, playerStates, 1, TurnState.DETECTIVES_MOVING);
		
		playerStates = new HashMap<>();
		playerStates.put(holmes11.getPlayer(), holmes11);
		playerStates.put(wallander14.getPlayer(), wallander14);
		playerStates.put(mrx13.getPlayer(), mrx13);
		mrxCaughtState = new MatchState(match, playerStates, 1, TurnState.DETECTIVES_MOVING);
		
		playerStates = new HashMap<>();
		playerStates.put(holmes11stuck.getPlayer(), holmes11stuck);
		playerStates.put(wallander14stuck.getPlayer(), wallander14stuck);
		playerStates.put(mrx13.getPlayer(), mrx13);
		detectivesStuckState = new MatchState(match, playerStates, 1, TurnState.DETECTIVES_MOVING);
		
		playerStates = new HashMap<>();
		playerStates.put(holmes11.getPlayer(), holmes11);
		playerStates.put(wallander14.getPlayer(), wallander14);
		playerStates.put(mrx4.getPlayer(), mrx4);
		finalMoveState = new MatchState(match, playerStates, match.getMaxTurns(), TurnState.DETECTIVES_MOVING);
	}

	@Test
	public void testMoveMrXNotNull() {
		Move move = new Move(MrXTestStub.instance, 133, TransportType.BUS, false);
		MatchState state = defaultState.moveMrX(move);
		
		assertTrue("Moving Mr. X not legal", state != null);
	}
	
	@Test
	public void testMoveMrXCorrect() {
		Move move = new Move(MrXTestStub.instance, 133, TransportType.BUS, false);
		MatchState state = defaultState.moveMrX(move);
		
		PlayerState mrxstate = state.getPlayerState(MrXTestStub.instance);
		boolean correct =
				state.getTurnNumber() == 1 &&
				state.getTurnState() == TurnState.DETECTIVES_MOVING &&
				mrxstate.isVisible() == false &&
				mrxstate.getPosition() == 133 &&
				mrxstate.getNrBusTickets() == 99;
		
		assertTrue("Incorrect state after moving Mr. X", correct);
	}
	
	@Test
	public void testMoveMrXCorrectBlackTicket() {
		Move move = new Move(MrXTestStub.instance, 133, TransportType.BUS, true);
		MatchState state = defaultState.moveMrX(move);
		
		PlayerState mrxstate = state.getPlayerState(MrXTestStub.instance);
		boolean correct =
				state.getTurnNumber() == 1 &&
				state.getTurnState() == TurnState.DETECTIVES_MOVING &&
				mrxstate.isVisible() == false &&
				mrxstate.getPosition() == 133 &&
				mrxstate.getNrBusTickets() == 100 &&
				mrxstate.getNrBlackTickets() == 3;
		
		assertTrue("Incorrect state after moving Mr. X", correct);
	}
	
	@Test
	public void testMoveMrXWrongTransport() {
		Move move = new Move(MrXTestStub.instance, 133, TransportType.UNDERGROUND, false);
		MatchState state = defaultState.moveMrX(move);
		
		assertTrue("Moving Mr. X by underground without existing link worked.", state == null);
	}	
	
	@Test
	public void testMoveMrXWrongDestination() {
		Move move = new Move(MrXTestStub.instance, 5, TransportType.BUS, false);
		MatchState state = defaultState.moveMrX(move);
		
		assertTrue("Moving Mr. X to unlinked destination worked.", state == null);
	}

	@Test
	public void testLegalDetectiveMoveScheduled() {
		Move move1 = new Move(MrXTestStub.instance, 133, TransportType.BUS, false);
		Move move2 = new Move(DetectiveTestStub.holmes, 102, TransportType.BUS, false);
		MatchState state = defaultState.moveMrX(move1);
		boolean correct = state.scheduleDetectiveMove(move2);
		
		assertTrue("Detective moves could not be scheduled", correct);
	}
	
	@Test
	public void testLegalDetectiveMoveExecuted() {
		Move move1 = new Move(MrXTestStub.instance, 133, TransportType.BUS, false);
		Move move2 = new Move(DetectiveTestStub.holmes, 102, TransportType.BUS, false);
		Move move3 = new Move(DetectiveTestStub.wallander, 116, TransportType.BUS, false);
		MatchState state = defaultState.moveMrX(move1);
		state.scheduleDetectiveMove(move2);
		state.scheduleDetectiveMove(move3);
		MatchState finalState = state.moveDetectives();
		
		PlayerState mrxstate = finalState.getPlayerState(MrXTestStub.instance);
		PlayerState holmesState = finalState.getPlayerState(DetectiveTestStub.holmes);
		PlayerState wallanderState = finalState.getPlayerState(DetectiveTestStub.wallander);
		
		boolean correct =
				finalState.getTurnNumber() == 2 &&
				finalState.getTurnState() == TurnState.MRX_MOVING &&
				mrxstate.isVisible() == false &&
				mrxstate.getPosition() == 133 &&
				mrxstate.getNrBusTickets() == 99 &&
				mrxstate.getNrBlackTickets() == 4 &&
				holmesState.getPosition() == 102 &&
				holmesState.getNrBusTickets() == 7 &&
				wallanderState.getPosition() == 116 &&
				wallanderState.getNrBusTickets() == 7;
	
		assertTrue("Incorrect state after moving the detectives.", correct);
	}	
	
	@Test
	public void testIllegalDetectiveMoveNotScheduled() {
		Move move1 = new Move(MrXTestStub.instance, 133, TransportType.BUS, false);
		Move move2 = new Move(DetectiveTestStub.holmes, 1, TransportType.BUS, false);
		MatchState state = defaultState.moveMrX(move1);
		boolean correct = !state.scheduleDetectiveMove(move2);
		
		assertTrue("Illegal detective move was scheduled.", correct);
	}
	
	@Test
	public void testMoveDetectivesIncompleteSchedule() {
		Move move1 = new Move(MrXTestStub.instance, 133, TransportType.BUS, false);
		Move move2 = new Move(DetectiveTestStub.holmes, 102, TransportType.BUS, false);
		MatchState state = defaultState.moveMrX(move1);
		state.scheduleDetectiveMove(move2);
		MatchState finalState = state.moveDetectives();
		
		assertTrue("Detectives could be moved despite incomplete schedule.", finalState == null);
	}
	
	@Test
	public void testMrXStuck() {
		Move move1 = new Move(DetectiveTestStub.holmes, 3, TransportType.TAXI, false);
		Move move2 = new Move(DetectiveTestStub.wallander, 13, TransportType.TAXI, false);
		mrxBlockedState.scheduleDetectiveMove(move1);
		mrxBlockedState.scheduleDetectiveMove(move2);
		MatchState finalState = mrxBlockedState.moveDetectives();
		
		assertTrue("Mr. X not stuck after detectives moved.", finalState.getTurnState()== TurnState.DETECTIVES_WON);
	}
	
	@Test
	public void testMrXCaught() {
		Move move1 = new Move(DetectiveTestStub.holmes, 3, TransportType.TAXI, false);
		Move move2 = new Move(DetectiveTestStub.wallander, 13, TransportType.TAXI, false);
		mrxCaughtState.scheduleDetectiveMove(move1);
		mrxCaughtState.scheduleDetectiveMove(move2);
		MatchState finalState = mrxCaughtState.moveDetectives();
		
		assertTrue("Mr. X not caught after detectives moved.", finalState.getTurnState()== TurnState.DETECTIVES_WON);
	}
	
	@Test
	public void testDetectivesStuck() {
		Move move1 = new Move(DetectiveTestStub.holmes, 10, TransportType.TAXI, false);
		Move move2 = new Move(DetectiveTestStub.wallander, 15, TransportType.TAXI, false);
		detectivesStuckState.scheduleDetectiveMove(move1);
		detectivesStuckState.scheduleDetectiveMove(move2);
		MatchState finalState = detectivesStuckState.moveDetectives();

		assertTrue("Detectives not stuck.", finalState.getTurnState() == TurnState.MRX_WON);
	}
	
	@Test
	public void testFinalMoveResult() {
		Move move1 = new Move(DetectiveTestStub.holmes, 10, TransportType.TAXI, false);
		Move move2 = new Move(DetectiveTestStub.wallander, 15, TransportType.TAXI, false);
		finalMoveState.scheduleDetectiveMove(move1);
		finalMoveState.scheduleDetectiveMove(move2);
		MatchState finalState = finalMoveState.moveDetectives();
		
		assertTrue("Mr X has not won after final move.", finalState.getTurnState() == TurnState.MRX_WON);
		
	}
}
