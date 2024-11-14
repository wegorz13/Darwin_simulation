package agh.ics.oop.model;

public enum MapDirection {
    NORTH("^", new Vector2d(0, 1)),
    SOUTH("v", new Vector2d(0, -1)),
    WEST("<", new Vector2d(-1, 0)),
    EAST(">", new Vector2d(1, 0));

    private final String stringRepresentation;
    private final Vector2d vectorRepresentation;

    MapDirection(String stringRepresentation, Vector2d vectorRepresentation) {
        this.stringRepresentation = stringRepresentation;
        this.vectorRepresentation = vectorRepresentation;
    }

    @Override
    public String toString() {
        return this.stringRepresentation;
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> WEST;
            case EAST -> NORTH;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
        };
    }

    public Vector2d toUnitVector() {
        return this.vectorRepresentation;
    }
}
