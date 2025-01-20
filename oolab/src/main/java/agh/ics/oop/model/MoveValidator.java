package agh.ics.oop.model;

import agh.ics.oop.model.Vector2d;

public interface MoveValidator {

    /**
     * Indicate if any object can move to the given position.
     *
     * @param position
     *            The position checked for the movement possibility.
     * @return True if the object can move to that position.
     */
    boolean canMoveHorizontal(Vector2d position);

    boolean canMoveVertical(Vector2d position);

}