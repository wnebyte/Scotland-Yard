package se.kau.cs.sy.services.match.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MatchManagementIntegration {

	private final String BOARD_DETAILS_SERVICE_URL;
	
	@Autowired
	public MatchManagementIntegration(
			@Value("${app.board-service.host}") String boardServiceHost,
			@Value("${app.board-service.port}") int boardServicePort){
		
		BOARD_DETAILS_SERVICE_URL = "http://" + boardServiceHost + ":" + boardServicePort + "/board/details";
	}
	
	public String getBoardDetailsServiceUrl() {
		return BOARD_DETAILS_SERVICE_URL;
	}
}
