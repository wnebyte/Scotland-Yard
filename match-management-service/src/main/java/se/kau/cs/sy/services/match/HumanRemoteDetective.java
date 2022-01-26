package se.kau.cs.sy.services.match;

import java.util.UUID;
import se.kau.cs.sy.match.Player;
import se.kau.cs.sy.match.PlayerRole;
import se.kau.cs.sy.match.event.AbstractMatchEventListener;
import se.kau.cs.sy.match.event.MatchEvent;
import se.kau.cs.sy.match.event.StateChangeEvent;

public class HumanRemoteDetective extends AbstractMatchEventListener implements Player {

	private UUID id;

	private String name;
	
	public HumanRemoteDetective(String name) {
		this.name = name;
		this.id = UUID.randomUUID();
	}
	
	@Override
	public void onStateChange(StateChangeEvent event) {
	}

	public UUID getId() {
		return this.id;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public PlayerRole getRole() {
		return PlayerRole.DETECTIVE;
	}

}
