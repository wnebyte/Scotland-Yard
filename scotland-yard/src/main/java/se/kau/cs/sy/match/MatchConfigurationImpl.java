package se.kau.cs.sy.match;

import se.kau.cs.sy.board.Board;

public class MatchConfigurationImpl implements MatchConfiguration {

	private Board board = null;
	private int[] startingPositions = {};
	private int[] surfacingTurns = {};
	private int numberOfDetectives = 0;
	private int numberOfTurns = 0;
	private int taxiPerDetective = 0;
	private int busPerDetective = 0;
	private int undergroundPerDetective = 0;
	private int blackPerDetective = 0;
	private int taxiForMrX = 0;
	private int busForMrX = 0;
	private int undergroundForMrX = 0;
	private int blackForMrX = 0;
	
	public static class Builder {
		
		private Board board;
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
		
		public Builder() {
		}
		
		public MatchConfigurationImpl build() {
			MatchConfigurationImpl config = new MatchConfigurationImpl();
			config.board = this.board;
			config.startingPositions = this.startingPositions;
			config.surfacingTurns = this.surfacingTurns;
			config.numberOfDetectives = this.nrOfDetectives;
			config.numberOfTurns = this.nrOfTurns;
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
		
		public Builder board(Board board) {
			this.board = board;
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

	@Override
	public int[] getStartingPositions() {
		return startingPositions;
	}

	@Override
	public int getTaxiTicketsPerDetective() {
		return taxiPerDetective;
	}

	@Override
	public int getBusTicketsPerDetective() {
		return busPerDetective;
	}

	@Override
	public int getUndergroundTicketsPerDetective() {
		return undergroundPerDetective;
	}

	@Override
	public int getBlackTicketsPerDetective() {
		return blackPerDetective;
	}

	@Override
	public int getTaxiTicketsForMrX() {
		return taxiForMrX;
	}

	@Override
	public int getBusTicketsForMrX() {
		return busForMrX;
	}

	@Override
	public int getUndergroundTicketsForMrX() {
		return undergroundForMrX;
	}

	@Override
	public int getBlackTicketsForMrX() {
		return blackForMrX;
	}

	@Override
	public int getNumberOfTurns() {
		return numberOfTurns;
	}

	@Override
	public int getNumberOfDetectives() {
		return numberOfDetectives;
	}

	@Override
	public int[] getSurfacingTurns() {
		return surfacingTurns;
	}

	@Override
	public Board getBoard() {
		return board;
	}

}
