package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {
    @Test
    void inputInvalidMove() {
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        String[] moves = {"x"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        Simulation s = new Simulation(positions, directions);

        s.run();
        List<Animal> finalPositions = s.getAnimals();

        assertTrue(finalPositions.get(0).isAt(new Vector2d(2, 2)));
        assertEquals(MapDirection.NORTH, finalPositions.get(0).getOrientation());
    }

    @Test
    void inputForward() {
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        String[] moves = {"f"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        Simulation s = new Simulation(positions, directions);

        s.run();
        List<Animal> finalPositions = s.getAnimals();

        assertTrue(finalPositions.get(0).isAt(new Vector2d(2, 3)));
        assertEquals(MapDirection.NORTH, finalPositions.get(0).getOrientation());
    }

    @Test
    void inputBackward() {
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        String[] moves = {"b"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        Simulation s = new Simulation(positions, directions);

        s.run();
        List<Animal> finalPositions = s.getAnimals();

        assertTrue(finalPositions.get(0).isAt(new Vector2d(2, 1)));
        assertEquals(MapDirection.NORTH, finalPositions.get(0).getOrientation());
    }
    @Test
    void inputLeft() {
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        String[] moves = {"l"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        Simulation s = new Simulation(positions, directions);

        s.run();
        List<Animal> finalPositions = s.getAnimals();

        assertTrue(finalPositions.get(0).isAt(new Vector2d(2, 2)));
        assertEquals(MapDirection.WEST, finalPositions.get(0).getOrientation());
    }

    @Test
    void inputRight() {
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        String[] moves = {"r"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        Simulation s = new Simulation(positions, directions);

        s.run();
        List<Animal> finalPositions = s.getAnimals();

        assertTrue(finalPositions.get(0).isAt(new Vector2d(2, 2)));
        assertEquals(MapDirection.EAST, finalPositions.get(0).getOrientation());
    }

    @Test
    void orientation() {
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        String[] moves = {"l", "l", "l", "r"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        Simulation s = new Simulation(positions, directions);

        s.run();
        List<Animal> finalPositions = s.getAnimals();

        assertTrue(finalPositions.get(0).isAt(new Vector2d(2, 2)));
        assertEquals(MapDirection.SOUTH, finalPositions.get(0).getOrientation());
    }

    @Test
    void runOnlyValidMoves() {
        List<Vector2d> positions = List.of(new Vector2d(0, 0));
        String[] moves = {"f", "f", "f", "r", "f", "f", "l", "b", "b"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        Simulation s = new Simulation(positions, directions);

        s.run();
        List<Animal> finalPositions = s.getAnimals();

        assertTrue(finalPositions.get(0).isAt(new Vector2d(2, 1)));
        assertEquals(MapDirection.NORTH, finalPositions.get(0).getOrientation());
    }

    @Test
    void runWithInvalidMoves() {
        List<Vector2d> positions = List.of(new Vector2d(0, 0));
        String[] moves = {"b", "b", "b", "r", "b", "f", "f", "r", "f", "b", "b", "b", "b", "b", "b", "l", "f", "f", "f", "f"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        Simulation s = new Simulation(positions, directions);

        s.run();
        List<Animal> finalPositions = s.getAnimals();

        assertEquals("(4,4) Wschód", finalPositions.get(0).toString());
        assertTrue(finalPositions.get(0).isAt(new Vector2d(4, 4)));
        assertEquals(MapDirection.EAST, finalPositions.get(0).getOrientation());
    }

    @Test
    void runWithTwoAnimals() {
        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
        String[] moves = {"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        Simulation s = new Simulation(positions, directions);

        s.run();
        List<Animal> finalPositions = s.getAnimals();

        assertTrue(finalPositions.get(0).isAt(new Vector2d(3, 0)));
        assertEquals(MapDirection.SOUTH, finalPositions.get(0).getOrientation());
        assertTrue(finalPositions.get(1).isAt(new Vector2d(2, 4)));
        assertEquals(MapDirection.NORTH, finalPositions.get(1).getOrientation());
    }
}