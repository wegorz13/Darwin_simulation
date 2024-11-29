package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.sqrt;

public class GrassField extends AbstractWorldMap {
    private final Map<Vector2d, Grass> grasses;

    public GrassField(int numberOfGrassFields) {
        this.grasses = new HashMap<>();

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator((int)sqrt(numberOfGrassFields * 10), (int)sqrt(numberOfGrassFields * 10), numberOfGrassFields);
        for(Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement objectAtAnimal = super.objectAt(position);
        return objectAtAnimal != null ? objectAtAnimal : grasses.getOrDefault(position, null);
    }

    @Override
    public List<WorldElement> getElements() {
        List<WorldElement> elements = super.getElements();
        elements.addAll(grasses.values());
        return elements;
    }

    // Założyłem że mapa może się rozszerzać i kurczyć, kurczenie jest problematyczne - każdy ruch może być potencjalnie kurczący
    @Override
    public void move(Animal animal, MoveDirection direction) {
        super.move(animal, direction);
    }

    @Override
    public Boundary getCurrentBounds()  {
        List<WorldElement> elements = getElements();
        Vector2d lowerCorner = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Vector2d upperCorner = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (WorldElement element : elements) {
            lowerCorner = lowerCorner.lowerLeft(element.getPosition());
            upperCorner = upperCorner.upperRight(element.getPosition());
        }
        return new Boundary(lowerCorner, upperCorner);
    }
}