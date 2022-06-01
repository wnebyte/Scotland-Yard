package se.kau.cs.sy.services.board;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.Session;
import se.kau.cs.sy.board.Link;
import se.kau.cs.sy.board.Node;
import se.kau.cs.sy.board.TransportType;

import static org.neo4j.driver.Values.parameters;

public class NodeDao {

    private static final Map<Integer, Character> map = new HashMap<Integer, Character>() {
        {
            put(0, 'n');
        }

        {
            put(1, 'b');
        }

        {
            put(2, 'c');
        }

        {
            put(3, 'd');
        }

        {
            put(4, 'e');
        }

        {
            put(5, 'f');
        }

        {
            put(6, 'g');
        }

        {
            put(7, 'h');
        }
    };

    public void create(Session session, Node node) {
        session.writeTransaction(tx -> {
            tx.run("CREATE (n:Station {id: $id, x: $x, y: $y})",
                    parameters("id", node.getId(),
                            "x", node.getLocation().x, "y", node.getLocation().y)).consume();
            return 1;
        });
    }

    public void link(Session session, Node node) {
        session.writeTransaction(tx -> {
            for (Link link : node.getLinks()) {
                int n = link.getNodes()[0] == node.getId() ? link.getNodes()[1] : link.getNodes()[0];
                String type = link.getType().toString();
                tx.run("MATCH (a:Station),(b:Station) WHERE a.id = $id AND b.id = $n AND NOT EXISTS " +
                                "{ MATCH (a)-[r:" + type + "]-(b) } CREATE (a)-[r:" + type + "]->(b) RETURN type(r)",
                        parameters("id", node.getId(), "n", n)).consume();
            }
            return 1;
        });
    }

    public List<Integer> shortestPath(Session session, int start, int end) {
        return session.writeTransaction(tx -> {
            Result result = tx.run("MATCH (a:Station{id: $aId}),(b:Station{id: $bId})," +
                            "p = shortestPath((a)-[*]-(b))" +
                            "RETURN p",
                    parameters("aId", start, "bId", end));
            List<Integer> route = new ArrayList<>();
            while (result.hasNext()) {
                Record record = result.next();
                for (Value value : record.values()) {
                    Path path = value.asPath();
                    path.nodes().forEach(n -> route.add(n.get("id").asInt()));
                    break;
                }
            }
            return route;
        });
    }

    public Set<Integer> athop(Session session, int node, List<TransportType> types) {
        return session.writeTransaction(tx -> {
            Set<Integer> athop = new HashSet<>();
            String rel = types.stream().map(Enum::toString).collect(Collectors.joining("|"));
            Result result = tx.run("" +
                            "MATCH (n: Station {id: $id}) " +
                            "CALL apoc.neighbors.athop(n, $rel, $count) " +
                            "YIELD node " +
                            "RETURN node",
                    parameters("id", node, "rel", rel, "count", types.size()));
            while (result.hasNext()) {
                Record record = result.next();
                for (Value value : record.values()) {
                    int n = value.get("id").asInt();
                    athop.add(n);
                }
            }
            return athop;
        });
    }

    public Set<Integer> athopMany(Session session, Set<Integer> nodes, List<TransportType> types) {
        return session.writeTransaction(tx -> {
            Set<Integer> athop = new HashSet<>();
            String rel = types.stream().map(Enum::toString).collect(Collectors.joining("|"));
            Result result = tx.run("" +
                            "MATCH (n: Station) WHERE n.id IN $set " +
                            "CALL apoc.neighbors.athop(n, $rel, $count) " +
                            "YIELD node " +
                            "RETURN node",
                    parameters("set", nodes, "rel", rel, "count", types.size()));
            while (result.hasNext()) {
                Record record = result.next();
                for (Value value : record.values()) {
                    int n = value.get("id").asInt();
                    athop.add(n);
                }
            }
            return athop;
        });
    }

    // This implementation performs worse than NodeDao#athopMany().
    public Set<Integer> getReachableNodes(Session session, Set<Integer> nodes, List<TransportType> types) {
        return session.writeTransaction(tx -> {
            Set<Integer> reachableNodes = new HashSet<>();
            int index = 0;
            List<String> l = types.stream().map(new Function<TransportType, String>() {
                @Override
                public String apply(TransportType transportType) {
                    return "[:" + transportType.toString() + "]";
                }
            }).collect(Collectors.toList());
            StringBuilder builder = new StringBuilder();
            for (String t : l) {
                builder.append("-").append(t).append("-").append("(").append(map.get(++index)).append(")");
            }
            Result result = tx.run(String.format(
                    "MATCH (n:Station)%s WHERE n.id IN %s RETURN %s",
                    builder.toString(), Arrays.toString(nodes.toArray()), map.get(index)));
            while (result.hasNext()) {
                Record record = result.next();
                for (Value value : record.values()) {
                    int n = value.get("id").asInt();
                    reachableNodes.add(n);
                }
            }
            return reachableNodes;
        });
    }

    public void createIndex(Session session) {
        session.writeTransaction(tx -> {
            tx.run("CREATE INDEX station_id IF NOT EXISTS FOR (n:Station) ON (n.id)").consume();
            return 1;
        });
    }

    public Set<Integer> getNeighbours(Session session, Node node, List<TransportType> types) {
        return session.writeTransaction(tx -> {
            Set<Integer> nodes = new HashSet<>();
            String s;
            if (types == null || types.isEmpty()) {
                s = "";
            } else {
                s = "[r:" + types.stream().map(TransportType::toString).collect(Collectors.joining("|")) + "]";
            }
            Result result = tx.run("MATCH (n:Station { id: $id })-" + s + "-(b) RETURN b",
                    parameters("id", node.getId()));
            while (result.hasNext()) {
                Record record = result.next();
                for (Value value : record.values()) {
                    int n = value.get("id").asInt();
                    nodes.add(n);
                }
            }
            return nodes;
        });
    }

    public Set<Integer> getNeighbours(Session session, int node, List<TransportType> types) {
        return session.writeTransaction(tx -> {
            Set<Integer> neighbours = new HashSet<>();
            String s;
            if (types == null || types.isEmpty()) {
                s = "";
            } else {
                s = "[r:" + types.stream().map(TransportType::toString).collect(Collectors.joining("|")) + "]";
            }
            Result result = tx.run("MATCH (n:Station { id: $id })-" + s + "-(b) RETURN b",
                    parameters("id", node));
            while (result.hasNext()) {
                Record record = result.next();
                for (Value value : record.values()) {
                    int n = value.get("id").asInt();
                    neighbours.add(n);
                }
            }
            return neighbours;
        });
    }

    public Set<Integer> getNeighbours(Session session, Set<Integer> nodes, List<TransportType> types) {
        return session.writeTransaction(tx -> {
            Set<Integer> neighbours = new HashSet<>();
            String s;
            if (types == null || types.isEmpty()) {
                s = "";
            } else {
                s = "[r:" + types.stream().map(TransportType::toString).collect(Collectors.joining("|")) + "]";
            }
            Result result = tx.run("MATCH (n:Station)-" + s + "-(b) WHERE n.id IN $set RETURN b",
                    parameters("set", nodes));
            while (result.hasNext()) {
                Record record = result.next();
                for (Value value : record.values()) {
                    int n = value.get("id").asInt();
                    neighbours.add(n);
                }
            }
            return neighbours;
        });
    }
}
