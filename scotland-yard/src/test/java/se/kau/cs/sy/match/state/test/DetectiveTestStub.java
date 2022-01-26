package se.kau.cs.sy.match.state.test;

import java.util.UUID;

import se.kau.cs.sy.match.Player;
import se.kau.cs.sy.match.PlayerRole;
import se.kau.cs.sy.match.event.MatchEvent;
import se.kau.cs.sy.match.event.StateChangeEvent;

public class DetectiveTestStub implements Player {

	
	public static DetectiveTestStub holmes = new DetectiveTestStub("Holmes");
	public static DetectiveTestStub wallander = new DetectiveTestStub("Wallander");
	
	private UUID id;
	private String name;
	
	private DetectiveTestStub(String name) {
		this.name = name;
		this.id = UUID.randomUUID();
	}
	
	public UUID getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public PlayerRole getRole() {
		return PlayerRole.DETECTIVE;
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
