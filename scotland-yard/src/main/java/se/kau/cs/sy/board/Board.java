package se.kau.cs.sy.board;

import java.io.Serializable;
import java.util.*;

import se.kau.cs.sy.util.FileHandler;

public class Board implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Board londonBoard;

	private final UUID id;

	private String name = "";

	private List<Node> nodes = new ArrayList<>();
	
	static {
		 londonBoard = loadStubMap();
		 londonBoard.setName("London");
	}
	
	private Board(String name) {
		id = UUID.randomUUID();
		this.name = name;
	}

	//Only for json deserialization
	public Board() {
		this("");
	}

	public static Board create() {
		return londonBoard;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Node> getNodes() {
		return new ArrayList<>(nodes);
	}
	
	public Set<Link> getLinks(int node) {
		return getLinks(node, TransportType.UNKNOWN);
	}

	/**
	 * Returns the Links that can be traversed using the specified <code>TransportType</code> for the
	 * <code>Node</code> with the specified index.
	 * @param node the index of the node.
	 * @param type the TransportType.
	 * @return the result.
	 */
	public Set<Link> getLinks(int node, TransportType type) {
		Set<Link> links = nodes.get(node).getLinks();
		if (type != TransportType.UNKNOWN) {
			links.removeIf(l -> l.getType() != type);
		}
		return links; 
	}

	/**
	 * Returns the <code>Location</code> of the <code>Node</code> with the specified index.
	 * @param node the index of the node.
	 * @return the result.
	 */
	public Location getLocation(int node) {
		return nodes.get(node).getLocation();
	}
	
	public Set<Integer> getNeighbourNodes(int node) {
		return getNeighbourNodes(node, TransportType.UNKNOWN);
	}

	/**
	 * Returns the index of each <code>Node</code> that neighbours the Node with the specified index
	 * whose <code>Link</code> can be traversed using the specified <code>TransportType</code>.
	 * @param node the index of the node.
	 * @param type the <code>TransportType</code>.
	 * @return the result.
	 */
	public Set<Integer> getNeighbourNodes(int node, TransportType type) {
		Set<Integer> result = new HashSet<>();
		Set<Link> links = this.getLinks(node, type);
		for (Link l : links) {
			result.add(l.getNodes()[0]);
			result.add(l.getNodes()[1]);
		}
		result.remove(node);
		return result;
	}
	
	public boolean connected(int nodeA, int nodeB, TransportType type) {
		return getNeighbourNodes(nodeA, type).contains(nodeB);
	}
	
	public boolean connected(int nodeA, int nodeB) {
		return getNeighbourNodes(nodeA, TransportType.UNKNOWN).contains(nodeB);
	}
	
	public int lastNodeIndex() {
		return nodes.get(nodes.size() - 1).getId();
	}
	
	public boolean nodeExists(int number) {
		return number > 0 && number <= lastNodeIndex();
	}

	public List<Integer> shortestPath(int nodeA, int nodeB) {
		Queue<Integer> queue = new LinkedList<>();
		Map<Integer, Boolean> visited = new HashMap<>();
		Map<Integer, Integer> preceding = new HashMap<>();
		boolean solved = false;
		visited.put(nodeA, true);
		queue.add(nodeA);

		while (!queue.isEmpty()) {
			int currentNode = queue.poll();
			for (int node : getNeighbourNodes(currentNode)) {
				if (!visited.containsKey(node)) {
					visited.put(node, true);
					queue.add(node);
					preceding.put(node, currentNode);
					if (node == nodeB) {
						queue.clear();
						solved = true;
						break;
					}
				}
			}
		}
		if (solved) {
			int node = nodeB;
			List<Integer> path = new ArrayList<>();
			while (node != nodeA) {
				path.add(node);
				node = preceding.get(node);
			}
			Collections.reverse(path);
			return path;
		} else {
			return null;
		}
	}

	public Set<Integer> athop(Set<Integer> nodes, Collection<TransportType> types) {
		if (nodes == null || types.isEmpty()) return nodes;
		Queue<TransportType> queue = new LinkedList<>(types);
		TransportType type = queue.poll();
		Set<Integer> neighbours = new HashSet<>();
		for (int node : nodes) {
			Set<Integer> c = getNeighbourNodes(node, type);
			neighbours.addAll(c);
		}
		return athop(neighbours, queue);
	}
	
	private static Board loadMap() {
      	Board map = new Board();
        try {
  			FileHandler mapHandler = new FileHandler("scotmap-large.txt"); // se/kau/cs/sy/scotmap.txt
			// RandomAccessFile map=new RandomAccessFile(f,"r");
			String buffer=mapHandler.readLine();
			StringTokenizer token;
			token=new StringTokenizer(buffer);
			int nrNodes=Integer.parseInt(token.nextToken());
			Location[] locs = readMapPositions();
			for (int i = 0; i < nrNodes; i++ ) {
				Node newNode = new Node(i);
				newNode.setLocation(locs[i]);
				map.nodes.add(newNode);
			}

			buffer=mapHandler.readLine();
			while(buffer!=null && buffer.trim().length()>0) {
				token=new StringTokenizer(buffer);
				int node1=Integer.parseInt(token.nextToken());
				int node2=Integer.parseInt(token.nextToken());
				String strType=token.nextToken();
				
				TransportType type = TransportType.UNKNOWN;
				if(strType.equals("T")) type=TransportType.TAXI;
				if(strType.equals("B")) type=TransportType.BUS;
				if(strType.equals("U")) type=TransportType.UNDERGROUND;
				if(strType.equals("X")) type=TransportType.BOAT;
				Link newLink = new Link(node1, node2, type);
				map.nodes.get(node1).addLink(newLink);
				map.nodes.get(node2).addLink(newLink);
				buffer=mapHandler.readLine();
			}
			mapHandler.close();
	    }
        catch(Exception e) {
			e.printStackTrace();
	    }
	return map;
	}

	private static Board loadStubMap() {
		return new Board();
	}

	private static Location[] readMapPositions() {
    	Location[] result = null;
        try {
    		FileHandler map = new FileHandler("scotmapg-large.txt"); // se/kau/cs/sy/scotmapg.txt
        		
			String buffer = map.readLine();
			StringTokenizer token;
			token = new StringTokenizer(buffer);
			int numPos = Integer.parseInt(token.nextToken());
			result = new Location[numPos];
			for(int i = 0; i < numPos; i++)
			{
				buffer = map.readLine();
				token = new StringTokenizer(buffer);
				
				int pos = Integer.parseInt(token.nextToken());
				int posX = Integer.parseInt(token.nextToken());
				int posY = Integer.parseInt(token.nextToken());
				
				result[pos] = new Location(posX, posY);
			}
	    }
        catch(Exception e) {
			System.exit(1);
	    }
        return result;
    }

	//Only for json deserialization
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
	
}
