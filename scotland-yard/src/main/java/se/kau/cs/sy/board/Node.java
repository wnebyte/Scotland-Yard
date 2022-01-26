package se.kau.cs.sy.board;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Node implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private Location location;

	private Set<Link> links;

	//Only for json deserialization
	public Node() {
		links = new HashSet<>();
	}
	
	public Node(int id) {
		this();
		this.id = id;
	}
	
	public void setLocation(Location l) {
		this.location = l;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void addLink(Link link) {
		links.add(link);
	}
	
	public Set<Link> getLinks() {
		return new HashSet<>(links);
	}
	
	public int getId() {
		return id;
	}
	
	//Only for json deserialization
	public void setId(int id) {
		this.id = id;
	}

	//Only for json deserialization
	public void setLinks(Set<Link> links) {
		this.links = links;
	}

}
