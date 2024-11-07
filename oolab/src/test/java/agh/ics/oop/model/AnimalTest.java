package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {
    @Test
    void testToString() {
        Animal a1 = new Animal();
        Animal a2 = new Animal(new Vector2d(3, 2));

        assertEquals("(2,2) Północ", a1.toString());
        assertEquals("(3,2) Północ", a2.toString());
    }

    @Test
    void testToStringWithDifferentOrientation() {
        Animal a1 = new Animal();
        Animal a2 = new Animal(new Vector2d(3, 2));
        Animal a3 = new Animal(new Vector2d(5, 6));

        a1.move(MoveDirection.LEFT);
        a2.move(MoveDirection.RIGHT);
        a3.move(MoveDirection.LEFT);
        a3.move(MoveDirection.LEFT);

        assertEquals("(2,2) Zachód", a1.toString());
        assertEquals("(3,2) Wschód", a2.toString());
        assertEquals("(5,6) Południe", a3.toString());
    }

    @Test
    void isAt() {
        Animal a1 = new Animal();
        Animal a2 = new Animal(new Vector2d(3, 2));

        assertTrue(a1.isAt(new Vector2d(2, 2)));
        assertFalse(a2.isAt(new Vector2d(2, 3)));
        assertFalse(a2.isAt(new Vector2d(3, 3)));
    }

    @Test
    void moveForwad() {
        Animal a1 = new Animal();

        a1.move(MoveDirection.FORWARD);

        assertTrue(a1.isAt(new Vector2d(2, 3)));
    }

    @Test
    void moveBackward() {
        Animal a1 = new Animal();
        Animal a2 = new Animal();

        a1.move(MoveDirection.BACKWARD);

        assertTrue(a1.isAt(new Vector2d(2, 1)));
    }

    @Test
    void moveLeft() {
        Animal a1 = new Animal();

        a1.move(MoveDirection.LEFT);

        assertTrue(a1.isAt(new Vector2d(2, 2)));
        assertEquals(MapDirection.WEST, a1.getOrientation());
    }

    @Test
    void moveRight() {
        Animal a1 = new Animal();

        a1.move(MoveDirection.RIGHT);

        assertTrue(a1.isAt(new Vector2d(2, 2)));
        assertEquals(MapDirection.EAST, a1.getOrientation());
    }

    @Test
    void moveAllInOne() {
        Animal a1 = new Animal();

        a1.move(MoveDirection.FORWARD);
        a1.move(MoveDirection.FORWARD);
        a1.move(MoveDirection.RIGHT);
        a1.move(MoveDirection.BACKWARD);
        a1.move(MoveDirection.LEFT);
        a1.move(MoveDirection.FORWARD);

        assertEquals("(1,4) Północ", a1.toString());
    }

    @Test
    void moveOutsideOfMap() {
        Animal a1 = new Animal(new Vector2d(0, 0));
        Animal a2 = new Animal(new Vector2d(4, 4));

        a1.move(MoveDirection.BACKWARD);
        a1.move(MoveDirection.LEFT);
        a1.move(MoveDirection.FORWARD);
        a2.move(MoveDirection.FORWARD);
        a2.move(MoveDirection.RIGHT);
        a2.move(MoveDirection.FORWARD);

        assertTrue(a1.isAt(new Vector2d(0, 0)));
        assertTrue(a2.isAt(new Vector2d(4, 4)));
        assertEquals(MapDirection.WEST, a1.getOrientation());
        assertEquals(MapDirection.EAST, a2.getOrientation());
    }
}