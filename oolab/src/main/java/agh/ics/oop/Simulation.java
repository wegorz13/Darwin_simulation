package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Simulation {
    private final List<Animal> animals;
    private final List<MoveDirection> moves;

    public Simulation(List<Vector2d> animalPositions, List<MoveDirection> moves) {
        this.moves = moves;
        this.animals = new ArrayList<>();
        for (Vector2d position : animalPositions) {
            this.animals.add(new Animal(position));
        }
    }

    public void run() {
        if (animals.isEmpty()) return;

        int currentAnimal = 0;
        for (MoveDirection move: moves) {
            animals.get(currentAnimal).move(move);
            System.out.printf("Zwierzę %d : %s%n", currentAnimal, animals.get(currentAnimal));
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