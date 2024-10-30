package agh.ics.oop;
import agh.ics.oop.model.MoveDirection;

import java.util.Arrays;

public class OptionsParser {
    public static MoveDirection[] parse(String[] moves) {
        MoveDirection[] directions = new MoveDirection[moves.length];
        int j = 0;
        for (String move : moves) {
            switch (move) {
                case "f" -> directions[j++] = MoveDirection.FORWARD;
                case "b" -> directions[j++] = MoveDirection.BACKWARD;
                case "r" -> directions[j++] = MoveDirection.RIGHT;
                case "l" -> directions[j++] = MoveDirection.LEFT;
            }
        }
        return Arrays.copyOfRange(directions, 0, j);
    }
}