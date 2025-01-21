package agh.ics.oop.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.min;

public class Animal implements  WorldElement {
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position;
    private final Genotype genotype;
    private int energy;
    private int age = 0;
    private int dayOfDeath;
    private int grassConsumed = 0;
    private final List<Animal> babies = new ArrayList<Animal>();
    private final Random rand = new Random();
    private final int rightEdge;

    public Animal(Vector2d position, Genotype genotype, int energy, int rightEdge) {
        this.position = position;
        this.genotype = genotype;
        this.energy = energy;
        this.rightEdge = rightEdge;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return this.orientation.toString();
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveValidator validator) {
        // possible skip of move due to age, probability stops increasing after 80%
        if (rand.nextInt(100) >= min(age, 80)) {
            for (int i = 0; i < this.genotype.getGene(age % genotype.getSize()); i++) {
                this.orientation = orientation.next();
            }

            Vector2d newPosition = this.position.add(this.orientation.toUnitVector());
            if (!validator.canMoveHorizontal(newPosition)) {
                this.orientation=orientation.next().next().next().next();
            }
            else if (!validator.canMoveVertical(newPosition)) {
                this.position = new Vector2d(Math.abs(position.getX() - rightEdge), position.getY());
            }
            else this.position = newPosition;
        }
        this.energy -= 1;
        this.age++;
    }

    public Genotype getGenotype(){
        return this.genotype;
    }

    public int getEnergy(){
        return this.energy;
    }

    public void consume(int calory) {
        this.energy += calory;
        this.grassConsumed++;
    }

    public void giveBirth(int childCost, Animal baby) {
        this.energy -= childCost;
        this.babies.add(baby);
    }

    public void unlive(int dayOfSimulation) {
        this.dayOfDeath = dayOfSimulation;
    }

    public int countDescendants() {
        int descendantsCount = this.babies.size();
        for (Animal baby : babies) {
            descendantsCount += baby.countDescendants();
        }
        return descendantsCount;
    }
}