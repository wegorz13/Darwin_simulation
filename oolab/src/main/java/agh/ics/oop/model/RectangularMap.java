package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.HashMap;
import java.util.Map;

public class RectangularMap implements WorldMap {
    private final Map<Vector2d, Animal> animals = new HashMap<>();
    private final int width;
    private final int height;
    private final Vector2d leftDownCorner;
    private final Vector2d rightUpCorner;
    private final MapVisualizer visualizer = new MapVisualizer(this);

    public RectangularMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.leftDownCorner = new Vector2d(0, 0);
        this.rightUpCorner = new Vector2d(this.width - 1, this.height - 1);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return (position.follows(this.leftDownCorner) && position.precedes(this.rightUpCorner) && objectAt(position) == null);
    }

    @Override
    public boolean place(Animal animal) {
        if (canMoveTo(animal.getPosition())) {
            animals.put(animal.getPosition(), animal);
            return true;
        }
        return false;
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        // check if animal is present on the map
        if (objectAt(animal.getPosition()) != animal) return;

        animals.remove(animal.getPosition());
        animal.move(direction, this);
        animals.put(animal.getPosition(), animal);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public Animal objectAt(Vector2d position) {
        return animals.getOrDefault(position, null);
    }

    @Override
    public String toString() {
        return visualizer.draw(leftDownCorner, rightUpCorner);
    }
}