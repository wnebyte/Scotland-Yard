package se.kau.cs.sy.services.board;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import se.kau.cs.sy.board.*;
import se.kau.cs.sy.match.ClassicConfiguration;
import se.kau.cs.sy.transfer.board.SimpleBoardDTO;
import se.kau.cs.sy.transfer.match.MatchConfigurationDTO;
import static se.kau.cs.sy.services.util.GeneralUtils.readAllBytes;

@RestController
public class BoardController {

	private final Board board = Board.create();

	private final BoardRepository repository;

	public BoardController(BoardRepository repository) {
		this.repository = repository;
	}
	
	@GetMapping("/boards") 
	public Set<SimpleBoardDTO> getBoards() {
		Set<SimpleBoardDTO> result = new HashSet<SimpleBoardDTO>();
		result.add(new SimpleBoardDTO(board.getId(), board.getName()));
		return result;
	}
	
	@GetMapping("/board/details")
	public Board getBoardDetails() {
		return board;
	}
	
	@GetMapping("/links")
	public Set<Link> getLinks(
			@RequestParam(value = "node", defaultValue = "1") int nodeNumber,
			@RequestParam(value = "type", defaultValue= "") TransportType type) {
		
		if (type == null) return board.getLinks(nodeNumber);
		else return board.getLinks(nodeNumber, type);
	}
	
	@GetMapping("/neighbours")
	public Set<Integer> getNeighbours(
			@RequestParam(value = "node", defaultValue = "1") int nodeNumber,
			@RequestParam(value = "type", defaultValue = "") TransportType type) {
		
		if (type == null) return board.getNeighbourNodes(nodeNumber);
		else return board.getNeighbourNodes(nodeNumber, type);
	}

	// O(V+E).
	@GetMapping("/shortestPath")
	public List<Integer> getShortestPath(
			@RequestParam(value = "x") int x,
			@RequestParam(value = "y") int y
	) {
		if (!exists(x, y)) return null;
		return board.shortestPath(x, y);
	}

	// db impl
	@GetMapping("/shortestPathNeo4j")
	public CompletableFuture<List<Integer>> getShortestPathNeo4j(
			@RequestParam(value = "x") int x,
			@RequestParam(value = "y") int y
	) {
		if (!existsNeo4j(x, y)) return null;
		return repository.shortestPathCompletable(x, y);
	}

	@GetMapping("/athop")
	public Set<Integer> athop(
			@RequestParam(value = "nodes") Set<Integer> nodes,
			@RequestParam(value = "types") List<TransportType> types
	) {
		if (nodes == null || types == null) return null;
		nodes.removeIf(n -> !exists(n));
		return board.athop(nodes, types);
	}

	// db impl
	@GetMapping("/athopNeo4j")
	public Set<Integer> athopNeo4j(
			@RequestParam(value = "nodes") Set<Integer> nodes,
			@RequestParam(value = "types") List<TransportType> types
	) throws Exception {
		if (nodes == null || types == null) return null;
		nodes.removeIf(n -> !existsNeo4j(n));
		return repository.athopMany(nodes, types).get();
	}
	
	@GetMapping("/location/{nodeNumber}")
	public Location getLocation(@PathVariable int nodeNumber) {
		return board.getLocation(nodeNumber);
	}
	
	@GetMapping(
		value="/map",
		produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getBoardImage() throws IOException {
		InputStream is = getClass().getResourceAsStream("/static/map.jpg");
		return readAllBytes(is);
	}
	
	@GetMapping("/config")
	public MatchConfigurationDTO getDefaultConfiguration() {
		return new MatchConfigurationDTO.Builder(new ClassicConfiguration()).build();
	}

	/*
    ###########################
    #     UTILITY METHODS     #
    ###########################
    */

	private boolean exists(int... nodes) {
		if (nodes == null) return false;
		for (int n : nodes) {
			if (!(board.nodeExists(n) && n % 200 != 0)) {
				return false;
			}
		}
		return true;
	}

	private boolean existsNeo4j(int... nodes) {
		if (nodes == null) return false;
		for (int n : nodes) {
			if (!(n > 0 && n < 56000 && n % 200 != 0)) {
				return false;
			}
		}
		return true;
	}
}
