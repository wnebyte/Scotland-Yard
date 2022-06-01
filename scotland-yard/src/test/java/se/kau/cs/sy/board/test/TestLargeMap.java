package se.kau.cs.sy.board.test;

import org.junit.Test;
import se.kau.cs.sy.board.Board;
import static org.junit.Assert.*;

public class TestLargeMap {

    @Test
    public void testMapLoaded() {
        Board map = Board.create();
        assertNotNull("Map was not loaded", map);
    }

}
