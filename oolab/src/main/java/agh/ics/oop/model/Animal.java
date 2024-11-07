package agh.ics.oop.model;

public class Animal {
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position;

    private static final Vector2d MAPLEFTDOWNCORNER = new Vector2d(0, 0);
    private static final Vector2d MAPRIGHTUPCORNER = new Vector2d(4, 4);

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
        return position + " " + orientation;
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveDirection direction) {
        Vector2d newPosition = null;
        switch (direction) {
            case LEFT:
                this.orientation = orientation.previous();
                break;
            case RIGHT:
                this.orientation = orientation.next();
                break;
            case FORWARD:
                newPosition = position.add(orientation.toUnitVector());
                if (newPosition.follows(MAPLEFTDOWNCORNER) && newPosition.precedes(MAPRIGHTUPCORNER))
                    this.position = newPosition;
                break;
            case BACKWARD:
                newPosition = position.subtract(orientation.toUnitVector());
                if (newPosition.follows(MAPLEFTDOWNCORNER) && newPosition.precedes(MAPRIGHTUPCORNER))
                    this.position = newPosition;
                break;
        }
    }
}
