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
    protected final Map<Vector2d, ArrayList<Animal>> animals = new HashMap<Vector2d, ArrayList<Animal>>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    protected final List<MapChangeListener> listeners = new ArrayList<>();

    @Override
    public boolean canMoveHorizontal(Vector2d position) {
       return (position.getX() >= leftDownCorner.getX() && position.getX()<= rightUpCorner.getX());
    }

    @Override
    public boolean canMoveVertical(Vector2d position) {
        return (position.getY() >= leftDownCorner.getY() && position.getY()<= rightUpCorner.getY());
    }

    @Override
    public void place(Animal animal)  {
        Vector2d position = animal.getPosition();
        if (!animals.containsKey(position)) animals.put(position, new ArrayList<>());
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

    // To do zmiany ?
    @Override
    public WorldElement objectAt(Vector2d position) {
        List<Animal> list = animals.get(position);
        if (list == null) return null;
        return list.getFirst();
    }

    @Override
    public String toString() {
        Boundary drawCorners = getCurrentBounds();
        return visualizer.draw(drawCorners.leftDownCorner(), drawCorners.rightUpCorner());
    }

    @Override
    public List<WorldElement> getElements() {
        List<WorldElement> elements = new LinkedList<>();
        for (List<Animal> animalsList : animals.values()) {
            elements.addAll(animalsList);
        }
        return elements;
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