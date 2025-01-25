package agh.ics.oop.model;

public class Grass implements WorldElement {
    private final Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
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
    public String toRegionStyle() {
        return "-fx-background-color: #219b40; " +
                "-fx-background-image: url('CosmoOUTLINED.png'); " +
                "-fx-background-size: contain; " +      // Set the total size of the spritesheet
                "-fx-background-repeat: no-repeat;";
    }
}
