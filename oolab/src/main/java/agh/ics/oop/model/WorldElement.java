package agh.ics.oop.model;

public interface WorldElement {
    Vector2d getPosition();

    String toRegionStyle(String backgroundColor);
}