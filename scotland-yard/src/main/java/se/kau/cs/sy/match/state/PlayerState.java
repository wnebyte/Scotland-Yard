package se.kau.cs.sy.match.state;

import se.kau.cs.sy.board.TransportType;
import se.kau.cs.sy.match.Move;
import se.kau.cs.sy.match.Player;

/**
 * This class represents the state of a player as part of a match state.
 * It consists of the position of the player, whether he/she is visible on the board 
 * (relevant for Mr. X) and the amount of tickets.
 * @author Sebastian Herold
 *
 */
public class PlayerState {
	
	protected Player player;

	protected int nrTaxiTickets;

	protected int nrBusTickets;

	protected int nrUndergroundTickets;

	protected int nrBlackTickets;
	
	protected int position;

	protected boolean visible;
	
	public static class Builder {
		protected Player player = null;
		protected int nrTaxiTickets = 0;
		protected int nrBusTickets = 0;
		protected int nrUndergroundTickets = 0;
		protected int nrBlackTickets = 0;
		
		protected int position = -1;
		protected boolean visible = true;
		
		/**
		 * A builder for player states. See Builder pattern.
		 */
		public Builder() {
			
		}
		
		public Builder(Player player) {
			this.player = player;
		}
		
		/**
		 * Updates the builder according to a move specified by the data and the specified player state
		 * Note: only ticket availability affects whether a valid state can be constructed from
		 * this data but not whether there is a link on the map, whether the destination is occupied, etc.
		 * Those checks are done in MatchState.
		 * 
		 * @param ps 
		 * @param pos
		 * @param type
		 * @param black
		 * @return
		 */
		public Builder moveFrom(PlayerState ps, Move move) {
			if (ps.getPlayer() != move.player) {
				this.player = null;
				return this;
			}
			this.player = ps.player;
			this.nrTaxiTickets = ps.getNrTaxiTickets();
			this.nrBusTickets = ps.getNrBusTickets();
			this.nrUndergroundTickets = ps.getNrUndergroundTickets();
			this.nrBlackTickets = ps.getNrBlackTickets();
			this.position = move.to;
			this.visible = ps.isVisible();
			if (move.black) {
				this.nrBlackTickets--;
			}
			else {
				switch(move.type) {
				case TAXI:
					nrTaxiTickets--;
					break;
				case BOAT:
					nrBlackTickets--;
					break;
				case BUS:
					nrBusTickets--;
					break;
				case UNDERGROUND:
					nrUndergroundTickets--;
					break;
				default:
					break;
				}
			}
			return this;
		}
		
		/**
		 * A builder initialized with the values of another state.
		 * @param ps the state to be copied.
		 * @return
		 */
		public Builder copy(PlayerState ps) {
			this.player = ps.getPlayer();
			this.position = ps.getPosition();
			this.visible = ps.isVisible();
			this.nrTaxiTickets = ps.getNrTaxiTickets();
			this.nrBusTickets = ps.getNrBusTickets();
			this.nrUndergroundTickets = ps.getNrUndergroundTickets();
			this.nrBlackTickets = ps.getNrBlackTickets();
			return this;
		}
		
		public Builder position(int pos) {
			this.position = pos;
			return this;
		}
		
		public Builder taxi(int tickets) {
			this.nrTaxiTickets = tickets;
			return this;
		}
		
		public Builder bus(int tickets) {
			this.nrBusTickets = tickets;
			return this;
		}
		
		public Builder underground(int tickets) {
			this.nrUndergroundTickets = tickets;
			return this;
		}

		public Builder black(int tickets) {
			this.nrBlackTickets = tickets;
			return this;
		}
		
		public Builder visible(boolean v) {
			this.visible = v;
			return this;
		}
		
		public PlayerState build() {
			if (this.nrTaxiTickets < 0 ||
					this.nrBusTickets < 0 ||
					this.nrUndergroundTickets < 0 ||
					this.nrBlackTickets < 0 ||
					this.player == null ||
					this.position == -1) {
				return null;
			}
			return new PlayerState(this.player,
					this.position, this.nrTaxiTickets,
					this.nrBusTickets, this.nrUndergroundTickets,
					this.nrBlackTickets, this.visible);
		}
		
	}
	
	private PlayerState(Player player, int position, int taxi, int bus, int underground, int black, boolean visible) {
		this.player = player;
		this.position = position;
		this.visible = visible;
		nrTaxiTickets = taxi;
		nrBusTickets = bus;
		nrUndergroundTickets = underground;
		nrBlackTickets = black;
	}
	
	public int getNrTaxiTickets() {
		return nrTaxiTickets;
	}

	public int getNrBusTickets() {
		return nrBusTickets;
	}

	public int getNrUndergroundTickets() {
		return nrUndergroundTickets;
	}

	public int getNrBlackTickets() {
		return nrBlackTickets;
	}

	public int getPosition() {
		return position;
	}
	
	public Player getPlayer() {
		return player;
	}

	/**
	 * Checks whether a player (in this state) has regular tickets left
	 * for a given type of transportation.
	 * @param type the type of transportation
	 * @return {@code true} if tickets are available, {@code false} otherwise.
	 */
	public boolean hasRegularTicketFor(TransportType type) {
		boolean available;
		switch(type) {
			case TAXI:
				available = getNrTaxiTickets() > 0;
				break;
			case BUS:
				available = getNrBusTickets() > 0;
				break;
			case UNDERGROUND: 
				available = getNrUndergroundTickets() > 0;
				break;
			case BOAT: 
				available = getNrBlackTickets() > 0;
			default:
				available = false;
		}
		return available;
	}
	
	/**
	 * Checks whether a player in this state has black tickets left
	 * @return {@code true} if tickets are available, {@code false} otherwise.
	 */
	public boolean hasBlackTicket() {
		return getNrBlackTickets() > 0;
	}
	
	/**
	 * Checks whether represented player is visible.
	 * @return {@code true} if player is visible, {@code false} otherwise.
	 */
	public boolean isVisible() {
		return visible;
	}
	
	public String toString() {
		return getPlayer().getName() + "; Position: " + getPosition() +
				", Tickets (Taxi, Bus, U-Ground, Black): (" +
				getNrTaxiTickets() + ", " + getNrBusTickets() + ", " +
				getNrUndergroundTickets() + ", " + getNrBlackTickets() + "); Visible: " +
				isVisible();
	}
}
