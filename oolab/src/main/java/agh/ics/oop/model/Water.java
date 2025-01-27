package agh.ics.oop.model;

public class Water implements WorldElement {
    private final Vector2d position;

    public Water(Vector2d position){
        this.position = position;
    }
    @Override
    public String toString() {
        return "~";
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public String toRegionStyle(String backgroundColor) {
        return "-fx-background-color: " + backgroundColor +";"+
                "-fx-background-size: cover; " +
                "-fx-background-position: center;";
    }
}
