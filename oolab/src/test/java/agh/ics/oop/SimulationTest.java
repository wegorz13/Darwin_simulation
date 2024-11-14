package agh.ics.oop;

import agh.ics.oop.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {
    @Test
    void inputInvalidMove() {
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        String[] moves = {"x"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        WorldMap map = new RectangularMap(5, 5);
        Simulation simulation = new Simulation(positions, directions, map);

        simulation.run();
        List<Animal> finalPositions = simulation.getAnimals();

        assertTrue(finalPositions.get(0).isAt(new Vector2d(2, 2)));
        assertEquals(MapDirection.NORTH, finalPositions.get(0).getOrientation());
    }

    @Test
    void inputForward() {
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        String[] moves = {"f"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        WorldMap map = new RectangularMap(5, 5);
        Simulation simulation = new Simulation(positions, directions, map);

        simulation.run();
        List<Animal> finalPositions = simulation.getAnimals();

        assertTrue(finalPositions.get(0).isAt(new Vector2d(2, 3)));
        assertEquals(MapDirection.NORTH, finalPositions.get(0).getOrientation());
    }

    @Test
    void inputBackward() {
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        String[] moves = {"b"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        WorldMap map = new RectangularMap(5, 5);
        Simulation simulation = new Simulation(positions, directions, map);

        simulation.run();
        List<Animal> finalPositions = simulation.getAnimals();

        assertTrue(finalPositions.get(0).isAt(new Vector2d(2, 1)));
        assertEquals(MapDirection.NORTH, finalPositions.get(0).getOrientation());
    }
    @Test
    void inputLeft() {
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        String[] moves = {"l"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        WorldMap map = new RectangularMap(5, 5);
        Simulation simulation = new Simulation(positions, directions, map);

        simulation.run();
        List<Animal> finalPositions = simulation.getAnimals();

        assertTrue(finalPositions.get(0).isAt(new Vector2d(2, 2)));
        assertEquals(MapDirection.WEST, finalPositions.get(0).getOrientation());
    }

    @Test
    void inputRight() {
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        String[] moves = {"r"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        WorldMap map = new RectangularMap(5, 5);
        Simulation simulation = new Simulation(positions, directions, map);

        simulation.run();
        List<Animal> finalPositions = simulation.getAnimals();

        assertTrue(finalPositions.get(0).isAt(new Vector2d(2, 2)));
        assertEquals(MapDirection.EAST, finalPositions.get(0).getOrientation());
    }

    @Test
    void orientation() {
        List<Vector2d> positions = List.of(new Vector2d(2, 2));
        String[] moves = {"l", "l", "l", "r"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        WorldMap map = new RectangularMap(5, 5);
        Simulation simulation = new Simulation(positions, directions, map);

        simulation.run();
        List<Animal> finalPositions = simulation.getAnimals();

        assertTrue(finalPositions.get(0).isAt(new Vector2d(2, 2)));
        assertEquals(MapDirection.SOUTH, finalPositions.get(0).getOrientation());
    }

    @Test
    void runOnlyValidMoves() {
        List<Vector2d> positions = List.of(new Vector2d(0, 0));
        String[] moves = {"f", "f", "f", "r", "f", "f", "l", "b", "b"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        WorldMap map = new RectangularMap(5, 5);
        Simulation simulation = new Simulation(positions, directions, map);

        simulation.run();
        List<Animal> finalPositions = simulation.getAnimals();

        assertTrue(finalPositions.get(0).isAt(new Vector2d(2, 1)));
        assertEquals(MapDirection.NORTH, finalPositions.get(0).getOrientation());
    }

    @Test
    void runWithInvalidMovesOnTwoDifferentMaps() {
        List<Vector2d> positions = List.of(new Vector2d(0, 0));
        String[] moves = {"b", "b", "b", "r", "b", "f", "f", "r", "f", "b", "b", "b", "b", "b", "b", "l", "f", "f", "f", "f"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        WorldMap map1 = new RectangularMap(5, 5);
        WorldMap map2 = new RectangularMap(4, 7);
        Simulation simulation1 = new Simulation(positions, directions, map1);
        Simulation simulation2 = new Simulation(positions, directions, map2);

        simulation1.run();
        simulation2.run();
        List<Animal> finalPositions1 = simulation1.getAnimals();
        List<Animal> finalPositions2 = simulation2.getAnimals();

        assertTrue(finalPositions1.get(0).isAt(new Vector2d(4, 4)));
        assertEquals(MapDirection.EAST, finalPositions1.get(0).getOrientation());
        assertTrue(finalPositions2.get(0).isAt(new Vector2d(3, 6)));
        assertEquals(MapDirection.EAST, finalPositions2.get(0).getOrientation());
    }

    @Test
    void runWithTwoAnimalsOnTwoDifferentMaps() {
        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
        String[] moves = {"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
        List<MoveDirection> directions = OptionsParser.parse(moves);
        WorldMap map1 = new RectangularMap(5, 5);
        WorldMap map2 = new RectangularMap(4, 7);
        Simulation simulation1 = new Simulation(positions, directions, map1);
        Simulation simulation2 = new Simulation(positions, directions, map2);

        simulation1.run();
        simulation2.run();
        List<Animal> finalPositions1 = simulation1.getAnimals();
        List<Animal> finalPositions2 = simulation2.getAnimals();

        assertTrue(finalPositions1.get(0).isAt(new Vector2d(2, 0)));
        assertEquals(MapDirection.SOUTH, finalPositions1.get(0).getOrientation());
        assertTrue(finalPositions1.get(1).isAt(new Vector2d(3, 4)));
        assertEquals(MapDirection.NORTH, finalPositions1.get(1).getOrientation());
        assertTrue(finalPositions2.get(0).isAt(new Vector2d(2, 0)));
        assertEquals(MapDirection.SOUTH, finalPositions2.get(0).getOrientation());
        assertTrue(finalPositions2.get(1).isAt(new Vector2d(3, 6)));
        assertEquals(MapDirection.NORTH, finalPositions2.get(1).getOrientation());
    }
}