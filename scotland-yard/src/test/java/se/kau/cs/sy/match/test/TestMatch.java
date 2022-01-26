package se.kau.cs.sy.match.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import se.kau.cs.sy.match.ClassicConfiguration;
import se.kau.cs.sy.match.Match;
import se.kau.cs.sy.match.Player;
import se.kau.cs.sy.match.PlayerRole;
import se.kau.cs.sy.match.RandomPlayer;
import se.kau.cs.sy.match.event.ConsoleMatchEventListener;
import se.kau.cs.sy.match.state.MatchState;
import se.kau.cs.sy.match.state.PlayerState;
import se.kau.cs.sy.match.state.TurnState;

public class TestMatch {

	private Match match;

	private Player mrx;

	private Player det[];
	
	@Before
	public void setUp() throws Exception {
		match = new Match(new ClassicConfiguration());
		mrx = new RandomPlayer(PlayerRole.MR_X);
		det = new Player[5];
		for (int i = 0; i < 5; i++) {
			det[i] = new RandomPlayer(PlayerRole.DETECTIVE);
		}
	}
	
	private void registerAll() {
		match.registerMrX(mrx);
		for (int i = 0; i < 5; i++) {
			match.registerDetective(det[i]);
		}
	}

	@Test
	public void testRegistrationIncomplete() {
		match.registerMrX(mrx);
		match.registerDetective(det[0]);
		
		assertTrue("Match started without all players onboard", match.getPhase() == Match.Phase.INIT);
	}

	@Test
	public void testRegistrationComplete() {
		registerAll();
		
		assertTrue("Match registration complete but game has not started", match.getPhase() == Match.Phase.RUNNING);
	}
	
	@Test
	public void testPlayerInitializedConsistently() {
		registerAll();
		MatchState state = match.getCurrentState();
		PlayerState ps = state.getPlayerState(det[1]);
		
		assertTrue("Detective incorrectly initialized.", ps.getNrTaxiTickets() == 10 &&
				ps.getNrBusTickets() == 8 &&
				ps.getNrUndergroundTickets() == 4 &&
				ps.getNrBlackTickets() == 0 &&
				ps.isVisible() &&
				ps.getPosition() > 0);
	}
	
	@Test
	public void testInitialStateCorrect() {
		match.registerListener(new ConsoleMatchEventListener());
		registerAll();
		MatchState state = match.getCurrentState();

		assertTrue("Initial state incorrect",
				state.getTurnNumber() == 1 &&
				state.getTurnState() == TurnState.MRX_MOVING);
	}	
}
