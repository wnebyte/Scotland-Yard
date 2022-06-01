package se.kau.cs.sy.services.board;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import se.kau.cs.sy.board.Node;
import se.kau.cs.sy.board.TransportType;

@Repository
public class BoardRepository {

    private final Driver driver;

    private final NodeDao nodeDao;

    private final ExecutorService executor;

    @Autowired
    public BoardRepository(Driver driver) {
        this.driver = driver;
        this.nodeDao = new NodeDao();
        this.executor = Executors.newCachedThreadPool();
    }

    /**
     * Creates a new database index on the Node labels id attribute.
     */
    public void createNodeIndex() {
        executor.submit(() -> {
            Session session = driver.session();
            nodeDao.createIndex(session);
            session.close();
        });
    }

    /**
     * Saves the specified <code>nodes</code> to the underlying database.
     * @param nodes a set of nodes.
     */
    /*
    It can take a very long time to insert a large amount of nodes into the database using
    this implementation. 50k nodes can take multiple hours.
     */
    public void createNodes(List<Node> nodes) {
        executor.submit(() -> {
           Session session = driver.session();
           nodes.forEach((n) -> nodeDao.create(session, n));
           nodes.forEach((n) -> nodeDao.link(session, n));
           session.close();
        });
    }

    /**
     * Retrieves and returns the shortest path between the two specified nodes.
     * @param x a node from which the path should begin.
     * @param y a node from which the path should end.
     * @return the path.
     */
    public Future<List<Integer>> shortestPath(int x, int y) {
        return executor.submit(() -> {
            Session session = driver.session();
            List<Integer> path = nodeDao.shortestPath(session, x, y);
            session.close();
            return path;
        });
    }

    public Future<Set<Integer>> athop(int node, List<TransportType> types) {
        return executor.submit(() -> {
           Session session = driver.session();
           Set<Integer> result = nodeDao.athop(session, node, types);
           session.close();
           return result;
        });
    }

    public Future<Set<Integer>> athopMany(Set<Integer> nodes, List<TransportType> types) {
        return executor.submit(() -> {
           Session session = driver.session();
           Set<Integer> result = nodeDao.athopMany(session, nodes, types);
           session.close();
           return result;
        });
    }

    public void close() {
        driver.close();
    }
}
