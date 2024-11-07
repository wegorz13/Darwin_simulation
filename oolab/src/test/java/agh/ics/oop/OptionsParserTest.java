package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {
    @Test
    void emptyMoves() {
        String[] moves = {};
        List<MoveDirection> correct = new LinkedList<>();

        assertEquals(correct, OptionsParser.parse(moves));
    }

    @Test
    void invalidMove() {
        String[] moves = {"x"};
        List<MoveDirection> correct = new LinkedList<>();

        assertEquals(correct, OptionsParser.parse(moves));
    }

    @Test
    void moveForward() {
        String[] moves = {"f"};
        List<MoveDirection> correct = List.of(MoveDirection.FORWARD);

        assertEquals(correct, OptionsParser.parse(moves));
    }

    @Test
    void moveBackward() {
        String[] moves = {"b"};
        List<MoveDirection> correct = List.of(MoveDirection.BACKWARD);

        assertEquals(correct, OptionsParser.parse(moves));
    }

    @Test
    void moveLeft() {
        String[] moves = {"l"};
        List<MoveDirection> correct = List.of(MoveDirection.LEFT);

        assertEquals(correct, OptionsParser.parse(moves));
    }

    @Test
    void moveRight() {
        String[] moves = {"r"};
        List<MoveDirection> correct = List.of(MoveDirection.RIGHT);

        assertEquals(correct, OptionsParser.parse(moves));
    }

    @Test
    void allDirectionsWithInvalidMoveBetween() {
        String[] moves = {"f", "b", "x", "l", "r"};
        List<MoveDirection> correct = List.of(MoveDirection.FORWARD, MoveDirection.BACKWARD, MoveDirection.LEFT, MoveDirection.RIGHT);

        assertEquals(correct, OptionsParser.parse(moves));
    }
}