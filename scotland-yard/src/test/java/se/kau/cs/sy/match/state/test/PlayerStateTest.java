package se.kau.cs.sy.match.state.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import se.kau.cs.sy.board.Board;
import se.kau.cs.sy.board.TransportType;
import se.kau.cs.sy.match.Move;
import se.kau.cs.sy.match.state.PlayerState;

public class PlayerStateTest {
	
	private static Board board = Board.create();
	private PlayerState holmesAtOne, poorHolmes, holmesAtPier;
	
	@Before
	public void setup() {
		 holmesAtOne = new PlayerState.Builder(DetectiveTestStub.holmes)
				.taxi(10)
				.bus(8)
				.underground(4)
				.position(1)
				.build();

		 poorHolmes = new PlayerState.Builder(DetectiveTestStub.holmes)
					.taxi(10)
					.bus(8)
					.position(1)
					.build();
		 
		 holmesAtPier = new PlayerState.Builder(DetectiveTestStub.holmes)
					.taxi(10)
					.bus(8)
					.underground(4)
					.position(108)
					.build();
	}

	@Test
	public void testMoveToNewState() {
		
		Move move = new Move(DetectiveTestStub.holmes, 46, TransportType.UNDERGROUND, false);
		PlayerState nextState = new PlayerState.Builder(DetectiveTestStub.holmes)
				.moveFrom(holmesAtOne, move)
				.build();
		
		assertTrue("Next state equals null.", nextState != null);
	}
	
	@Test
	public void testMoveToNewStateWrongPlayer() {
		Move move = new Move(DetectiveTestStub.wallander, 46, TransportType.UNDERGROUND, false);
		PlayerState nextState = new PlayerState.Builder(DetectiveTestStub.wallander)
				.moveFrom(holmesAtOne, move)
				.build();
		
		assertTrue("Switching players between states worked.", nextState == null);
		
	}
	
	@Test
	public void testMoveToNewStateNoTicket() {
		Move move = new Move(DetectiveTestStub.holmes, 46, TransportType.UNDERGROUND, false);
		PlayerState nextState = new PlayerState.Builder(DetectiveTestStub.holmes)
				.moveFrom(poorHolmes, move)
				.build();
		
		assertTrue("Moving without ticket worked.", nextState == null);
		
	}

	@Test
	public void testMoveToNewStateNoBlackTicket() {
		Move move = new Move(DetectiveTestStub.holmes, 115, TransportType.BOAT, false);
		PlayerState nextState = new PlayerState.Builder(DetectiveTestStub.holmes)
				.moveFrom(holmesAtPier, move)
				.build();
		
		assertTrue("Going by boat without black ticket worked.", nextState == null); 
	}
}
