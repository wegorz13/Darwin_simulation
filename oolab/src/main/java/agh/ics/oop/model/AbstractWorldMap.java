package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Boundary;

import java.util.*;

abstract class AbstractWorldMap implements WorldMap {
    protected final UUID id = UUID.randomUUID();
    protected Vector2d leftDownCorner = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
    protected Vector2d rightUpCorner = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
    protected final Map<Vector2d, ArrayList<Animal>> animals = new HashMap<Vector2d, ArrayList<Animal>>();
    protected final List<WaterReservoir> reservoirs = new ArrayList<>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    protected final List<MapChangeListener> listeners = new ArrayList<>();


    @Override
    public boolean canMoveTo(Vector2d position) {
        for (WaterReservoir reservoir : reservoirs){
            if (position.follows(reservoir.getLeftDownCorner()) & position.precedes(reservoir.getRightUpCorner())){
                return false;
            }
        }

       return (position.getX() >= leftDownCorner.getX() && position.getX()<= rightUpCorner.getX());
    }

    @Override
    public boolean aroundTheWorld(Vector2d position) {
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
    public void move(Animal animal) {
        Vector2d fromPosition = animal.getPosition();

        ArrayList<Animal> animalsAtFrom = this.animals.get(fromPosition);
        if (animalsAtFrom != null) {
            animalsAtFrom.remove(animal);

            //nie wiem czemu sie psuje jak nie usuwam, moja propozycja to tworzyć nową hashmapę codziennie na podstawie aliveAnimals,
            // i tak trzeba przesunąć każde zwierzę, równie dobrze można je wstawić na nowo
            if (animalsAtFrom.isEmpty()) {
                this.animals.remove(fromPosition);
            }
        }

        animal.move(this);

        this.place(animal);

        mapChanged("Animal moved from %s to %s".formatted(fromPosition, animal.getPosition()));
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    // To do zmiany ?
    @Override
    public WorldElement objectAt(Vector2d position) {
        for (WaterReservoir reservoir : reservoirs){
            if (position.follows(reservoir.getLeftDownCorner()) & position.precedes(reservoir.getRightUpCorner())){
                return new Water();
            }
        }
        List<Animal> list = animals.get(position);
        if (list == null || list.isEmpty()) return null;
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