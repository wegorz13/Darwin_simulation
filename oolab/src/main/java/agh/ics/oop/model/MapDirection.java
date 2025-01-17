package agh.ics.oop.model;

public enum MapDirection {
    NORTH("^", new Vector2d(0, 1)),
    NORTH_EAST("/", new Vector2d(1,1)),
    NORTH_WEST("\\", new Vector2d(-1,1)),
    SOUTH("v", new Vector2d(0, -1)),
    SOUTH_EAST("/", new Vector2d(1,-1)),
    SOUTH_WEST("\\", new Vector2d(-1,-1)),
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
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
            case EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> NORTH_WEST;
            case NORTH_WEST -> WEST;
            case WEST -> SOUTH_WEST;
            case SOUTH_WEST -> SOUTH;
            case SOUTH -> SOUTH_EAST;
            case SOUTH_EAST -> EAST;
            case EAST -> NORTH_EAST;
            case NORTH_EAST -> NORTH;

        };
    }

    public Vector2d toUnitVector() {
        return this.vectorRepresentation;
    }
}
