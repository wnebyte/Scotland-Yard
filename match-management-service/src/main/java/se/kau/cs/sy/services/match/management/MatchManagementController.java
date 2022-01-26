package se.kau.cs.sy.services.match.management;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.kau.cs.sy.board.Board;
import se.kau.cs.sy.board.TransportType;
import se.kau.cs.sy.match.Match;
import se.kau.cs.sy.match.MatchConfigurationImpl;
import se.kau.cs.sy.match.Move;
import se.kau.cs.sy.match.PlayerRole;
import se.kau.cs.sy.match.RandomPlayer;
import se.kau.cs.sy.match.state.MatchState;
import se.kau.cs.sy.services.match.HumanRemoteDetective;
import se.kau.cs.sy.transfer.match.MatchConfigurationDTO;
import se.kau.cs.sy.transfer.match.MoveDTO;
import se.kau.cs.sy.transfer.match.MoveResponseDTO;
import se.kau.cs.sy.transfer.match.SimpleMatchInfoDTO;
import se.kau.cs.sy.transfer.match.state.MatchStateDTO;

@RestController
public class MatchManagementController {

	private RestTemplate restTemplate;

	private ObjectMapper mapper;

	private MatchManagementIntegration integration;
	
	private MatchStorage storage;
	
	@Autowired
	public MatchManagementController(MatchManagementIntegration integration, RestTemplate restTemplate, ObjectMapper mapper) {
		this.integration = integration;
		this.mapper = mapper;
		this.restTemplate = restTemplate;
		this.storage = MatchStorage.getInstance();
	}

	@PostMapping("/matches")
	public UUID createMatch(@RequestBody MatchConfigurationDTO conf) {
		MatchConfigurationImpl.Builder builder = new MatchConfigurationImpl.Builder();
		Board board = restTemplate.getForObject(integration.getBoardDetailsServiceUrl(), Board.class);
		if (board != null) {
			builder.board(board)
				.detectives(conf.getNrOfDetectives())
				.startPositions(conf.getStartingPositions())
				.surfacingTurns(conf.getSurfacingTurns())
				.turns(conf.getNrOfTurns())
				.ticketsForDetectives(conf.getTaxiPerDetective(),
						conf.getBusPerDetective(),
						conf.getUndergroundPerDetective(),
						conf.getBlackPerDetective())
				.ticketsForMrX(conf.getTaxiForMrX(),
						conf.getBusForMrX(),
						conf.getUndergroundForMrX(),
						conf.getBlackForMrX());
			Match newMatch = new Match(builder.build());
			//TODO: add Mr. X properly
			newMatch.registerMrX(new RandomPlayer(PlayerRole.MR_X));
			storage.add(newMatch, "New match");
			return newMatch.getId();
		}
		else
			return null;
	}

	@PostMapping("/matches/{id}")
	public String setMatchName(
			@PathVariable UUID id,
			@RequestBody String name) {

		boolean modified = storage.setName(id, name);
		return modified ? name : "";
	}
	
	@PostMapping("/matches/{id}/join")
	public UUID joinMatch(
			@PathVariable UUID id,
			@RequestBody String playerName) {
		Match match = storage.getMatch(id);
		if (match != null) {
			HumanRemoteDetective player = new HumanRemoteDetective(playerName);
			boolean registered = match.registerDetective(player);
			if (registered) {
				return player.getId();
			}
		}
		return null;
	}
	
	@GetMapping("/matches")
	public List<SimpleMatchInfoDTO> getMatches() {
		List<SimpleMatchInfoDTO> result = new ArrayList<>();
		for (Match match : storage.getAllMatches()) {
			SimpleMatchInfoDTO matchEntry =
					new SimpleMatchInfoDTO(match.getId(),
							storage.getName(match.getId()),
							//matchNames.get(match.getId()),
							match.getBoard().getId(),
							match.getBoard().getName(),
							match.getRegisteredDetectives(),
							match.getNumberOfDetectives());
			result.add(matchEntry);
		}
		return result;
	}
	
	@GetMapping("/matches/{matchId}/state")
	public MatchStateDTO getMatchState(
			@PathVariable UUID matchId, 
			@RequestParam(value="playerId", defaultValue="0") UUID playerId) {
		Match match = storage.getMatch(matchId);
		if (match != null) {
			MatchState state = match.getCurrentState();
			if (state != null) {
				if (playerId.compareTo(
						state.getMrXState().getPlayer().getId()) != 0) {
					state = MatchState.blacken(state);
				}
				return new MatchStateDTO.Builder(state).build();
			}
		}
		return null;
	}

	
	@MessageMapping("/matches/{matchId}/moves")
	@SendTo("/topic/{matchId}/moves")
	public MoveResponseDTO move(@DestinationVariable UUID matchId,  MoveDTO move) {

		Match match = storage.getMatch(matchId);
		if (match != null) {
			MatchState preState = match.getCurrentState();
			boolean legal = match.performMove(
					new Move(match.getPlayer(move.getPlayerId()),
							move.getDestination(),
							move.getTransport(),
							move.isBlack()));
			MatchState postState = match.getCurrentState();
			if (legal) {
				return new MoveResponseDTO(move.getPlayerId(),
						preState != postState, legal);
			}
		}
		return new MoveResponseDTO(move.getPlayerId(), false, false);
	}
	
	@GetMapping("/matches/{matchId}/possiblemoves")
	Set<MoveDTO> getAllPossibleMoves(
			@PathVariable UUID matchId,
			@RequestParam UUID playerId) {
		Set<MoveDTO> result = new HashSet<>();
		Match match = storage.getMatch(matchId);
		if (match != null) {
			Set<Move> moves = match.getCurrentState()
				.getAllPossibleMoves(match.getPlayer(playerId));
			for (Move move : moves) {
				result.add(new MoveDTO(
						matchId,
						playerId,
						move.to,
						move.type,
						move.black));
			}
		}
		return result;
	}
	
	@GetMapping("/matches/{matchId}/mrxhistory")
	List<String> getMrXMoveHistory(@PathVariable UUID matchId) {
		List<String> result = new ArrayList<>();
		Match match = storage.getMatch(matchId);
		for (TransportType t : match.getMrXMoveHistory()) {
			result.add(t.toString());
		}
		return result;
	}
	
	@GetMapping("/matches/{matchId}/surfacing")
	int[] getSurfacingTurns(@PathVariable UUID matchId) {
		Match match = storage.getMatch(matchId);
		if (match != null) {
			return match.getSurfacingTurns();
		}
		else {
			return new int[]{};
		}
	}
	
	@GetMapping("/check")
	String getSurfacingTurns() {
		return "It seems the match-management-service is running.";
	}
}
