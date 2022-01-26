package se.kau.cs.sy.transfer.match;

import java.util.UUID;

import se.kau.cs.sy.board.TransportType;

public class MoveDTO {

	private UUID playerId;
	private int destination;
	private TransportType transport;
	private boolean black;
	
	public MoveDTO() {
	}
	
	public MoveDTO(UUID matchId, UUID playerId, int destination, TransportType transport, boolean black) {
		this.playerId = playerId;
		this.destination = destination;
		this.transport = transport;
		this.black = black;
	}

	public UUID getPlayerId() {
		return playerId;
	}

	public int getDestination() {
		return destination;
	}

	public TransportType getTransport() {
		return transport;
	}
	
	public boolean isBlack() {
		return black;
	}

	public void setPlayerId(UUID playerId) {
		this.playerId = playerId;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public void setTransport(TransportType transport) {
		this.transport = transport;
	}

	public void setBlack(boolean black) {
		this.black = black;
	}
	
	
}
