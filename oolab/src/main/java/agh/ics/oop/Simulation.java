package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Simulation implements Runnable {
    private final List<Animal> animals;
    private final List<MoveDirection> moves;
    private final WorldMap map;

    public Simulation(List<Vector2d> animalPositions, List<MoveDirection> moves, WorldMap map) {
        this.moves = moves;
        this.animals = new ArrayList<>();
        this.map = map;
        for (Vector2d position : animalPositions) {
            try {
                Animal animal = new Animal(position);
                if (this.map.place(animal)) {
                    this.animals.add(animal);
                }
            } catch (IncorrectPositionException e) {
                System.out.println("Warning: " + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        if (animals.isEmpty()) return;

        int currentAnimal = 0;
        for (MoveDirection move: moves) {
            this.map.move(this.animals.get(currentAnimal), move);
            currentAnimal = (currentAnimal + 1) % animals.size();
        }
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(this.animals);
    }

    public List<MoveDirection> getMoves() {
        return new LinkedList<>(this.moves);
    }
}