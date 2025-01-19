package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Boundary;

import java.util.*;

abstract class AbstractWorldMap implements WorldMap {
    // Probability of collision: https://en.wikipedia.org/wiki/Universally_unique_identifier#Collisions
    protected final UUID id = UUID.randomUUID();
    protected Vector2d leftDownCorner = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
    protected Vector2d rightUpCorner = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
    //sortujemy animale na tej samej pozycji rosnąco po energii
    protected final Map<Vector2d, LinkedList<Animal>> animals = new HashMap<>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    protected final List<MapChangeListener> listeners = new ArrayList<>();

    @Override
    public Vector2d canMoveTo(Vector2d position,MapDirection orientation) {
        Vector2d newPosition = position.add(orientation.toUnitVector());

        //zwraca poprawna pozycje, jak jest taka sama to zwierze musi zrobic 180
        if (newPosition.getY()> rightUpCorner.getY() || newPosition.getY()< leftDownCorner.getY()){
            return position;
        }
        else if (newPosition.getX() < leftDownCorner.getX()){
            return new Vector2d(rightUpCorner.getX(), newPosition.getY());
        }
        else if (newPosition.getX() > rightUpCorner.getX()){
            return new Vector2d(leftDownCorner.getX(), newPosition.getY());
        }
        return newPosition;
    }

    @Override
    public void place(Animal animal)  {
        Vector2d position = animal.getPosition();
        this.animals.get(position).add(animal);
        mapChanged("Animal placed at %s".formatted(position));
        }



    @Override
    public void move(Animal animal, MoveDirection direction) {
        // check if animal is present on the map
        if (objectAt(animal.getPosition()) != animal) return;

        Vector2d fromPosition = animal.getPosition();
        animals.remove(animal.getPosition());
        animal.move( this);
        this.animals.get(animal.getPosition()).add(animal);
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