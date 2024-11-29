package agh.ics.oop;
import agh.ics.oop.model.MoveDirection;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class OptionsParser {
    public static List<MoveDirection> parse(String[] moves) {
        List<MoveDirection> directions = new LinkedList<>();
        for (String move : moves) {
            switch (move) {
                case "f" -> directions.add(MoveDirection.FORWARD);
                case "b" -> directions.add(MoveDirection.BACKWARD);
                case "r" -> directions.add(MoveDirection.RIGHT);
                case "l" -> directions.add(MoveDirection.LEFT);
                default -> throw new IllegalArgumentException(move + " is not legal move specification");
            }
        }
        return directions;
    }
}