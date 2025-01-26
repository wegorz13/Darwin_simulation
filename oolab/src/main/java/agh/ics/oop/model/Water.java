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
    public String toRegionStyle() {
        return "-fx-background-color: #0000ff;"+
                "-fx-background-size: cover; " +
                "-fx-background-position: center;";
    }
}
