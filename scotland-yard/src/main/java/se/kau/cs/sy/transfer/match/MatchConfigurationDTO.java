package se.kau.cs.sy.transfer.match;

import java.util.UUID;

import se.kau.cs.sy.match.MatchConfiguration;

public class MatchConfigurationDTO {

	private UUID boardId;
	private int[] startingPositions;
	private int[] surfacingTurns;
	private int nrOfDetectives;
	private int nrOfTurns;
	private int taxiPerDetective;
	private int busPerDetective;
	private int undergroundPerDetective;
	private int blackPerDetective;
	private int taxiForMrX;
	private int busForMrX;
	private int undergroundForMrX;
	private int blackForMrX;

	private MatchConfigurationDTO() {
			}
	
	public UUID getBoardId() {
		return boardId;
	}

	public int[] getStartingPositions() {
		return startingPositions;
	}

	public int[] getSurfacingTurns() {
		return surfacingTurns;
	}

	public int getNrOfDetectives() {
		return nrOfDetectives;
	}

	public int getNrOfTurns() {
		return nrOfTurns;
	}

	public int getTaxiPerDetective() {
		return taxiPerDetective;
	}

	public int getBusPerDetective() {
		return busPerDetective;
	}

	public int getUndergroundPerDetective() {
		return undergroundPerDetective;
	}

	public int getBlackPerDetective() {
		return blackPerDetective;
	}

	public int getTaxiForMrX() {
		return taxiForMrX;
	}

	public int getBusForMrX() {
		return busForMrX;
	}

	public int getUndergroundForMrX() {
		return undergroundForMrX;
	}

	public int getBlackForMrX() {
		return blackForMrX;
	}

	public MatchConfigurationDTO(MatchConfiguration config) {
	}
	
	public static class Builder {
		private UUID boardId = null;
		private int[] startingPositions = {};
		private int[] surfacingTurns = {};
		private int nrOfDetectives = 0;
		private int nrOfTurns = 0;
		private int taxiPerDetective = 0;
		private int busPerDetective = 0;
		private int undergroundPerDetective = 0;
		private int blackPerDetective = 0;
		private int taxiForMrX = 0;
		private int busForMrX = 0;
		private int undergroundForMrX = 0;
		private int blackForMrX = 0;

		public Builder(MatchConfiguration mc) {
			this.boardId = mc.getBoard().getId();
			this.startingPositions = mc.getStartingPositions();
			this.surfacingTurns = mc.getSurfacingTurns();
			this.nrOfDetectives = mc.getNumberOfDetectives();
			this.nrOfTurns = mc.getNumberOfTurns();
			this.taxiPerDetective = mc.getTaxiTicketsPerDetective();
			this.busPerDetective = mc.getBusTicketsPerDetective();
			this.undergroundPerDetective = mc.getUndergroundTicketsPerDetective();
			this.blackPerDetective = mc.getBlackTicketsPerDetective();
			this.taxiForMrX = mc.getTaxiTicketsForMrX();
			this.busForMrX = mc.getBusTicketsForMrX();
			this.undergroundForMrX = mc.getUndergroundTicketsForMrX();
			this.blackForMrX = mc.getBlackTicketsForMrX();
		}
		
		public MatchConfigurationDTO build() {
			MatchConfigurationDTO config = new MatchConfigurationDTO();
			config.boardId = this.boardId;
			config.startingPositions = this.startingPositions;
			config.surfacingTurns = this.surfacingTurns;
			config.nrOfDetectives = this.nrOfDetectives;
			config.nrOfTurns = this.nrOfTurns;
			config.taxiPerDetective = this.taxiPerDetective;
			config.busPerDetective = this.busPerDetective;
			config.undergroundPerDetective = this.undergroundPerDetective;
			config.blackPerDetective = this.blackPerDetective;
			config.taxiForMrX = this.taxiForMrX;
			config.busForMrX = this.busForMrX;
			config.undergroundForMrX = this.undergroundForMrX;
			config.blackForMrX = this.blackForMrX;
			
			return config;
		}
		
		public Builder board(UUID id) {
			this.boardId = id;
			return this;
		}
		
		public Builder startPositions(int[] pos) {
			this.startingPositions = pos;
			return this;
		}
		
		public Builder surfacingTurns(int[] turns) {
			this.surfacingTurns = turns;
			return this;
		}
		
		public Builder turns(int turns) {
			this.nrOfTurns = turns;
			return this;
		}
		
		public Builder detectives(int detectives) {
			this.nrOfDetectives = detectives;
			return this;
		}
		
		public Builder ticketsForDetectives(int taxi, int bus, int underground, int black) {
			this.taxiPerDetective = taxi;
			this.busPerDetective = bus;
			this.undergroundPerDetective = underground;
			this.blackPerDetective = black;
			return this;
		}
		
		public Builder ticketsForMrX(int taxi, int bus, int underground, int black) {
			this.taxiForMrX = taxi;
			this.busForMrX = bus;
			this.undergroundForMrX = underground;
			this.blackForMrX = black;
			return this;
		}
		
	}
}
