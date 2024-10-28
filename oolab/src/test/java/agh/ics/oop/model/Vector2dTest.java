package agh.ics.oop.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {
    @Test
    void testEquals() {
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(1, 1);
        Vector2d v3 = new Vector2d(1, 2);

        assertTrue(v1.equals(v1));
        assertTrue(v1.equals(v2));
        assertFalse(v1.equals(v3));
    }

    @Test
    void testToString() {
        Vector2d v = new Vector2d(1, -2);

        assertEquals("(1,-2)", v.toString());
    }

    @Test
    void procedes() {
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(1, 2);
        Vector2d v3 = new Vector2d(0, 2);

        assertTrue(v1.precedes(v2));
        assertFalse(v1.precedes(v3));
    }

    @Test
    void follows() {
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(1, 0);
        Vector2d v3 = new Vector2d(0, 2);

        assertTrue(v1.follows(v2));
        assertFalse(v1.follows(v3));
    }

    @Test
    void upperRight() {
        Vector2d v1 = new Vector2d(-10, 1);
        Vector2d v2 = new Vector2d(-1, 10);

        assertEquals(new Vector2d(-1, 10), v1.upperRight(v2));
    }

    @Test
    void lowerLeft() {
        Vector2d v1 = new Vector2d(-10, 1);
        Vector2d v2 = new Vector2d(-1, 10);

        assertEquals(new Vector2d(-10, 1), v1.lowerLeft(v2));
    }

    @Test
    void add() {
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(1, -1);

        assertEquals(new Vector2d(2, 0), v1.add(v2));
    }

    @Test
    void subtract() {
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(1, -1);

        assertEquals(new Vector2d(0, 2), v1.subtract(v2));
    }

    @Test
    void opposite() {
        Vector2d v = new Vector2d(-1, 1);

        assertEquals(new Vector2d(1, -1), v.opposite());
    }
}