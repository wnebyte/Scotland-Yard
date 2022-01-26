package se.kau.cs.sy.match.state.test;

import java.util.UUID;

import se.kau.cs.sy.match.Player;
import se.kau.cs.sy.match.PlayerRole;
import se.kau.cs.sy.match.event.MatchEvent;
import se.kau.cs.sy.match.event.StateChangeEvent;

public class MrXTestStub implements Player {

	public static MrXTestStub instance = new MrXTestStub();
	private UUID id;
	
	@Override
	public String getName() {
		return "Mr. X";
	}

	public UUID getId() {
		return id;
	}
	
	@Override
	public PlayerRole getRole() {
		return PlayerRole.MR_X;
	}

	@Override
	public void onStateChange(StateChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEvent(MatchEvent event) {
		// TODO Auto-generated method stub
		
	}

}
