package agh.ics.oop.model;

public class Animal implements  WorldElement {
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position;

    public Animal() {
        this.position = new Vector2d(2, 2);
    }

    public Animal(Vector2d position) {
        this.position = position;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return this.orientation.toString();
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveDirection direction, MoveValidator validator) {
        Vector2d newPosition = null;
        switch (direction) {
            case LEFT -> this.orientation = orientation.previous();
            case RIGHT -> this.orientation = orientation.next();
            case FORWARD -> newPosition = position.add(orientation.toUnitVector());
            case BACKWARD -> newPosition = position.subtract(orientation.toUnitVector());
        }

        if (newPosition != null && validator.canMoveTo(newPosition))
            this.position = newPosition;
    }
}
