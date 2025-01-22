//package agh.ics.oop.model;
//
//import org.junit.jupiter.api.Test;
//
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class AnimalTest {
//    @Test
//    void testToString() {
//        Animal a1 = new Animal();
//        Animal a2 = new Animal(new Vector2d(3, 2));
//        MoveValidator validator = new RectangularMap(5, 5);
//
//        a2.move(MoveDirection.LEFT, validator);
//
//        assertEquals("^", a1.toString());
//        assertEquals("<", a2.toString());
//    }
//
//    @Test
//    void testToStringWithDifferentOrientation() {
//        Animal a1 = new Animal();
//        Animal a2 = new Animal(new Vector2d(3, 2));
//        Animal a3 = new Animal(new Vector2d(5, 6));
//        MoveValidator validator = new RectangularMap(5, 5);
//
//        a1.move(MoveDirection.LEFT, validator);
//        a2.move(MoveDirection.RIGHT, validator);
//        a3.move(MoveDirection.LEFT, validator);
//        a3.move(MoveDirection.LEFT, validator);
//
//        assertEquals("<", a1.toString());
//        assertEquals(">", a2.toString());
//        assertEquals("v", a3.toString());
//    }
//
//    @Test
//    void isAt() {
//        Animal a1 = new Animal();
//        Animal a2 = new Animal(new Vector2d(3, 2));
//
//        assertTrue(a1.isAt(new Vector2d(2, 2)));
//        assertFalse(a2.isAt(new Vector2d(2, 3)));
//        assertFalse(a2.isAt(new Vector2d(3, 3)));
//    }
//
//    @Test
//    void moveForwad() {
//        Animal a1 = new Animal();
//        MoveValidator validator = new RectangularMap(5, 5);
//
//        a1.move(MoveDirection.FORWARD, validator);
//
//        assertTrue(a1.isAt(new Vector2d(2, 3)));
//    }
//
//    @Test
//    void moveBackward() {
//        Animal a1 = new Animal();
//        MoveValidator validator = new RectangularMap(5, 5);
//
//        a1.move(MoveDirection.BACKWARD, validator);
//
//        assertTrue(a1.isAt(new Vector2d(2, 1)));
//    }
//
//    @Test
//    void moveLeft() {
//        Animal a1 = new Animal();
//        MoveValidator validator = new RectangularMap(5, 5);
//
//        a1.move(MoveDirection.LEFT, validator);
//
//        assertTrue(a1.isAt(new Vector2d(2, 2)));
//        assertEquals(MapDirection.WEST, a1.getOrientation());
//    }
//
//    @Test
//    void moveRight() {
//        Animal a1 = new Animal();
//        MoveValidator validator = new RectangularMap(5, 5);
//
//        a1.move(MoveDirection.RIGHT, validator);
//
//        assertTrue(a1.isAt(new Vector2d(2, 2)));
//        assertEquals(MapDirection.EAST, a1.getOrientation());
//    }
//
//    @Test
//    void moveAllInOne() {
//        Animal a1 = new Animal();
//        MoveValidator validator = new RectangularMap(5, 5);
//
//        a1.move(MoveDirection.FORWARD, validator);
//        a1.move(MoveDirection.FORWARD, validator);
//        a1.move(MoveDirection.RIGHT, validator);
//        a1.move(MoveDirection.BACKWARD, validator);
//        a1.move(MoveDirection.LEFT, validator);
//        a1.move(MoveDirection.FORWARD, validator);
//
//        assertTrue(a1.isAt(new Vector2d(1, 4)));
//        assertEquals(MapDirection.NORTH, a1.getOrientation());
//    }
//
//    @Test
//    void moveOutsideOfMap() {
//        Animal a1 = new Animal(new Vector2d(0, 0));
//        Animal a2 = new Animal(new Vector2d(4, 4));
//        MoveValidator validator = new RectangularMap(5, 5);
//
//        a1.move(MoveDirection.BACKWARD, validator);
//        a1.move(MoveDirection.LEFT, validator);
//        a1.move(MoveDirection.FORWARD, validator);
//        a2.move(MoveDirection.FORWARD, validator);
//        a2.move(MoveDirection.RIGHT, validator);
//        a2.move(MoveDirection.FORWARD, validator);
//
//        assertTrue(a1.isAt(new Vector2d(0, 0)));
//        assertTrue(a2.isAt(new Vector2d(4, 4)));
//        assertEquals(MapDirection.WEST, a1.getOrientation());
//        assertEquals(MapDirection.EAST, a2.getOrientation());
//    }
//}