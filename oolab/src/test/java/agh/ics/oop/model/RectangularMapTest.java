package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RectangularMapTest {
    @Test
    void place() {
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal(new Vector2d(2, 2));
        Animal animal2 = new Animal(new Vector2d(2, 2));
        Animal animal3 = new Animal(new Vector2d(5, 3));

        assertTrue(map.place(animal1));
        assertFalse(map.place(animal2));
        assertFalse(map.place(animal3));
    }

    @Test
    void isOccupied() {
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal(new Vector2d(2, 2));

        map.place(animal1);

        assertTrue(map.isOccupied(new Vector2d(2, 2)));
        assertFalse(map.isOccupied(new Vector2d(3, 2)));
        assertFalse(map.isOccupied(new Vector2d(2, 3)));
    }

    @Test
    void objectAt() {
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal(new Vector2d(2, 2));

        map.place(animal1);

        assertEquals(animal1, map.objectAt(new Vector2d(2, 2)));
        assertNull(map.objectAt(new Vector2d(3, 2)));
    }

    @Test
    void canMoveTo() {
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal(new Vector2d(2, 2));

        map.place(animal1);

        assertTrue(map.canMoveTo(new Vector2d(0, 0)));
        assertFalse(map.canMoveTo(new Vector2d(2, 2)));
        assertFalse(map.canMoveTo(new Vector2d(5, 5)));
        assertFalse(map.canMoveTo(new Vector2d(-1, -1)));
        assertFalse(map.canMoveTo(new Vector2d(5, 2)));
        assertFalse(map.canMoveTo(new Vector2d(2, 5)));
        assertFalse(map.canMoveTo(new Vector2d(-1, 2)));
        assertFalse(map.canMoveTo(new Vector2d(2, -1)));
    }

    @Test
    void move() {
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal(new Vector2d(2, 2));
        Animal animal2 = new Animal(new Vector2d(2, 2));

        map.place(animal1);
        map.move(animal1, MoveDirection.FORWARD);
        map.move(animal2, MoveDirection.FORWARD);

        assertEquals(animal2.getPosition(), new Vector2d(2, 2));
        assertEquals(animal1.getPosition(), new Vector2d(2, 3));
        assertEquals(animal1, map.objectAt(new Vector2d(2, 3)));
        assertTrue(map.isOccupied(new Vector2d(2, 3)));
    }

    @Test
    void testToString() {
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal(new Vector2d(2, 2));
        Animal animal2 = new Animal(new Vector2d(3, 2));
        String correct = " y\\x  0 1 2 3 4\n" +
                         "  5: -----------\n" +
                         "  4: | | | | | |\n" +
                         "  3: | | | |^| |\n" +
                         "  2: | | |^| | |\n" +
                         "  1: | | | | | |\n" +
                         "  0: | | | | | |\n" +
                         " -1: -----------\n";

        map.place(animal1);
        map.place(animal2);
        map.move(animal2, MoveDirection.FORWARD);

        assertEquals(correct, map.toString());
    }
}