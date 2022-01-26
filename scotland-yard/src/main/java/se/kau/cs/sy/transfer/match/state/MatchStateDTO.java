package se.kau.cs.sy.transfer.match.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import se.kau.cs.sy.match.state.MatchState;
import se.kau.cs.sy.match.state.PlayerState;
import se.kau.cs.sy.match.state.TurnState;

public class MatchStateDTO {
	
	private UUID matchId;
	private Set<PlayerStateDTO> playerStates;
	private int turn;
	private TurnState turnState;
	
	
	public static class Builder {
		private UUID matchId = null;
		private Map<UUID, PlayerStateDTO> playerStates;
		private int turn = 0;
		private TurnState turnState = TurnState.UNDEFINED;

		public Builder() {
			playerStates = new HashMap<>();
		}
		
		public Builder(MatchState state) {
			this();
			this.matchId = state.getMatch().getId();
			this.turn = state.getTurnNumber();
			this.turnState = state.getTurnState();
			for (PlayerState ps : state.getPlayerStates()) {
				PlayerStateDTO psDto = new PlayerStateDTO.Builder(ps).build();
				playerStates.put(psDto.getId(), psDto);
			}
		}
		
		public MatchStateDTO build() {
			MatchStateDTO result = new MatchStateDTO();
			result.matchId = this.matchId;
			result.playerStates.addAll(this.playerStates.values());
			result.turn = this.turn;
			result.turnState = this.turnState;
			return result;
		}
		
		public Builder matchId(UUID matchId) {
			this.matchId = matchId;
			return this;
		}
		
		public Builder turn(int turn) {
			this.turn = turn;
			return this;
		}
		
		public Builder turnState(TurnState ts) {
			this.turnState = ts;
			return this;
		}
		
		public Builder player(PlayerStateDTO ps) {
			if (!this.playerStates.containsKey(ps.getId())) {
				this.playerStates.put(ps.getId(), ps);
			}
			return this;
		}
		
	}
	
	private MatchStateDTO() {
		playerStates = new HashSet<>();
	}

	public UUID getMatchId() {
		return matchId;
	}

	public Set<PlayerStateDTO> getPlayerStates() {
		Set<PlayerStateDTO> result = new HashSet<>();
		result.addAll(playerStates);
		return result;
	}

	public int getTurn() {
		return turn;
	}

	public TurnState getTurnState() {
		return turnState;
	}
	
	
}
