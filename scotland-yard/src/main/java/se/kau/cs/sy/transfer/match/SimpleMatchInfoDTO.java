package se.kau.cs.sy.transfer.match;

import java.util.UUID;

public class SimpleMatchInfoDTO {
	
	private UUID matchId;
	private UUID boardId;
	private String matchName;
	private String boardName;
	private int detectivesJoined;
	private int maxDetectives;
	
	public SimpleMatchInfoDTO(UUID matchId, String matchName, UUID boardId, String boardName, int detectivesJoined, int maxDetectives) {
		this.matchId = matchId;
		this.matchName = matchName;
		this.boardId = boardId;
		this.boardName = boardName;
		this.detectivesJoined = detectivesJoined;
		this.maxDetectives = maxDetectives;
	}

	public UUID getMatchId() {
		return matchId;
	}

	public UUID getBoardId() {
		return boardId;
	}

	public String getMatchName() {
		return matchName;
	}

	public String getBoardName() {
		return boardName;
	}

	public int getDetectivesJoined() {
		return detectivesJoined;
	}

	public int getMaxDetectives() {
		return maxDetectives;
	}
}
