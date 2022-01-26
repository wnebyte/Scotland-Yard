package se.kau.cs.sy.transfer.match.state;

import java.util.UUID;

import se.kau.cs.sy.match.PlayerRole;
import se.kau.cs.sy.match.state.PlayerState;

public class PlayerStateDTO {

	private UUID id;
	private String name;
	private PlayerRole role;
	private int taxi;
	private int bus;
	private int underground;
	private int black;
	private int position;
	private boolean visible;
	
	public static class Builder {
		private UUID id = null;
		private String name = "No name";
		private PlayerRole role = PlayerRole.DETECTIVE;
		private int taxi = 0;
		private int bus = 0;
		private int underground = 0;
		private int black = 0;
		private int position = 0;
		private boolean visible = true;

		public Builder() {
		}
		
		public Builder(PlayerState state) {
			this.id = state.getPlayer().getId();
			this.name = state.getPlayer().getName();
			this.role = state.getPlayer().getRole();
			this.taxi = state.getNrTaxiTickets();
			this.bus = state.getNrBusTickets();
			this.underground = state.getNrUndergroundTickets();
			this.black = state.getNrBlackTickets();
			this.position = state.getPosition();
			this.visible = state.isVisible();
		}
		
		public PlayerStateDTO build() {
			PlayerStateDTO result = new PlayerStateDTO();
			result.id = this.id;
			result.name = this.name;
			result.role = this.role;
			result.taxi = this.taxi;
			result.bus = this.bus;
			result.underground = this.underground;
			result.black = this.black;
			result.position = this.position;
			result.visible = this.visible;
			return result;
		}
		
		public Builder id(UUID id) {
			this.id = id;
			return this;
		}
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder role(PlayerRole role) {
			this.role = role;
			return this;
		}
		
		public Builder taxi(int taxi) {
			this.taxi = taxi;
			return this;
		}
		
		public Builder bus(int bus) {
			this.bus = bus;
			return this;
		}
		
		public Builder underground(int underground) {
			this.underground = underground;
			return this;
		}
		
		public Builder black(int black) {
			this.black = black;
			return this;
		}
		
		public Builder position(int position) {
			this.position = position;
			return this;
		}
		
		public Builder visible(boolean visible) {
			this.visible = visible;
			return this;
		}
		
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public PlayerRole getRole() {
		return role;
	}

	public int getTaxi() {
		return taxi;
	}

	public int getBus() {
		return bus;
	}

	public int getUnderground() {
		return underground;
	}

	public int getBlack() {
		return black;
	}

	public int getPosition() {
		return position;
	}

	public boolean isVisible() {
		return visible;
	}
}
