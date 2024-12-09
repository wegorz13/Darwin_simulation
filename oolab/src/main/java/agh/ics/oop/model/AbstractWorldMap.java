package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Boundary;

import java.util.*;

abstract class AbstractWorldMap implements WorldMap {
    // Probability of collision: https://en.wikipedia.org/wiki/Universally_unique_identifier#Collisions
    protected final UUID id = UUID.randomUUID();
    protected Vector2d leftDownCorner = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
    protected Vector2d rightUpCorner = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    protected final List<MapChangeListener> listeners = new ArrayList<>();

    @Override
    public boolean canMoveTo(Vector2d position) {
        return (position.follows(this.leftDownCorner) && position.precedes(this.rightUpCorner) && !(objectAt(position) instanceof Animal));
    }

    @Override
    public boolean place(Animal animal) throws IncorrectPositionException {
        if (canMoveTo(animal.getPosition())) {
            animals.put(animal.getPosition(), animal);
            mapChanged("Animal placed at %s".formatted(animal.getPosition()));
            return true;
        }
        throw new IncorrectPositionException(animal.getPosition());
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        // check if animal is present on the map
        if (objectAt(animal.getPosition()) != animal) return;

        Vector2d fromPosition = animal.getPosition();
        animals.remove(animal.getPosition());
        animal.move(direction, this);
        animals.put(animal.getPosition(), animal);
        mapChanged("Animal moved from %s to %s".formatted(fromPosition, animal.getPosition()));
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animals.getOrDefault(position, null);
    }

    @Override
    public String toString() {
        Boundary drawCorners = getCurrentBounds();
        return visualizer.draw(drawCorners.leftDownCorner(), drawCorners.rightUpCorner());
    }

    @Override
    public List<WorldElement> getElements() {
        return new ArrayList<>(animals.values());
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(leftDownCorner, rightUpCorner);
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    public void addListener(MapChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MapChangeListener listener) {
        listeners.remove(listener);
    }

    protected void mapChanged(String message) {
        for (MapChangeListener listener : listeners)
            listener.mapChanged(this, message);
    }
}