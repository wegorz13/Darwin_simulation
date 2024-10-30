package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {
    @Test
    void emptyMoves() {
        String[] moves = {};
        MoveDirection[] correct = {};

        assertArrayEquals(correct, OptionsParser.parse(moves));
    }

    @Test
    void invalidMove() {
        String[] moves = {"x"};
        MoveDirection[] correct = {};

        assertArrayEquals(correct, OptionsParser.parse(moves));
    }

    @Test
    void moveForward() {
        String[] moves = {"f"};
        MoveDirection[] correct = {MoveDirection.FORWARD};

        assertArrayEquals(correct, OptionsParser.parse(moves));
    }

    @Test
    void moveBackward() {
        String[] moves = {"b"};
        MoveDirection[] correct = {MoveDirection.BACKWARD};

        assertArrayEquals(correct, OptionsParser.parse(moves));
    }

    @Test
    void moveLeft() {
        String[] moves = {"l"};
        MoveDirection[] correct = {MoveDirection.LEFT};

        assertArrayEquals(correct, OptionsParser.parse(moves));
    }

    @Test
    void moveRight() {
        String[] moves = {"r"};
        MoveDirection[] correct = {MoveDirection.RIGHT};

        assertArrayEquals(correct, OptionsParser.parse(moves));
    }

    @Test
    void allDirectionsWithInvalidMoveBetween() {
        String[] moves = {"f", "b", "x", "l", "r"};
        MoveDirection[] correct = {MoveDirection.FORWARD, MoveDirection.BACKWARD, MoveDirection.LEFT, MoveDirection.RIGHT};

        assertArrayEquals(correct, OptionsParser.parse(moves));
    }
}