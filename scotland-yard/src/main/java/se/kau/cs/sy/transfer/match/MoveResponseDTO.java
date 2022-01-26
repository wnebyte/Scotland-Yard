package se.kau.cs.sy.transfer.match;

import java.util.UUID;

public class MoveResponseDTO {
	private UUID movingPlayer;
	private boolean executed;
	private boolean stateUpdated;
	
	public MoveResponseDTO(UUID playerId, boolean updated, boolean executed) {
		this.movingPlayer = playerId;
		this.stateUpdated = updated;
		this.executed = executed;
	}

	public UUID getMovingPlayer() {
		return movingPlayer;
	}

	public boolean isStateUpdated() {
		return stateUpdated;
	}
	
	public boolean isExecuted() {
		return executed;
	}
}
