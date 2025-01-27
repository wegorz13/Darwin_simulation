package agh.ics.oop.model;

public class Grass implements WorldElement {
    private final Vector2d position;
    private final String flowerPath;

    public Grass(Vector2d position) {
        this.position = position;
        this.flowerPath = "url('flower" + (int)(Math.random() * 12 + 1) + ".png');";
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public String toRegionStyle(String backgroundColor) {
        return "-fx-background-color: " + backgroundColor + ";"+
                "-fx-background-image: " + flowerPath +
                "-fx-background-size: contain; " +      // Set the total size of the spritesheet
                "-fx-background-repeat: no-repeat;";
    }
}
