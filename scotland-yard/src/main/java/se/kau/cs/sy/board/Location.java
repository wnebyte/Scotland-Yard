package se.kau.cs.sy.board;

public class Location {

	public int x, y;

	//Only for json deserialization
	public Location() {
		this(0, 0);
	}
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	//Only for json deserialization
	public int getX() {
		return x;
	}

	//Only for json deserialization
	public int getY() {
		return y;
	}
	
	//Only for json deserialization
	public void setX(int x) {
		this.x = x;
	}

	//Only for json deserialization
	public void setY(int y) {
		this.y = y;
	}
	
}
