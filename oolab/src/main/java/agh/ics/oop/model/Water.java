package agh.ics.oop.model;

public class Water implements WorldElement {
    @Override
    public String toString() {
        return "~";
    }

    @Override
    public Vector2d getPosition() {
        return null;
    }

    @Override
    public String toRegionStyle(String backgroundColor) {
        return "-fx-background-color: " + backgroundColor +";"+
                "-fx-background-size: cover; " +
                "-fx-background-position: center;";
    }
}
