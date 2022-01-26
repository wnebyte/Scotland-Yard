package se.kau.cs.sy.transfer.board;

import java.util.UUID;

public class SimpleBoardDTO {

	private UUID id;

	private String name;
	
	public SimpleBoardDTO(UUID id, String name) {
		this.id = id;
		this.name = name;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	
}
