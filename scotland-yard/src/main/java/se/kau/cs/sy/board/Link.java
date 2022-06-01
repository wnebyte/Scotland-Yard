package se.kau.cs.sy.board;

import java.io.Serializable;

public class Link implements Serializable {

	private static final long serialVersionUID = 1L;

	private int[] nodes = new int[2];

	private TransportType type;
	
	//Only for json deserialization
	public Link() {
	}
	
	public Link(int nodea, int nodeb, TransportType t) {
		nodes[0] = nodea;
		nodes[1] = nodeb;
		type = t;
	}

	public int[] getNodes() {
		return nodes;
	}

	public TransportType getType() {
		return type;
	}

	//Only for json deserialization
	public void setNodes(int[] nodes) {
		this.nodes = nodes;
	}

	//Only for json deserialization
	public void setType(TransportType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return String.format(
				"(%d, %d, %s)", nodes[0], nodes[1], type
		);
	}
	
}
