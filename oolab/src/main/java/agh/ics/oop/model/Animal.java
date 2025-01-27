package agh.ics.oop.model;


import agh.ics.oop.model.records.SubjectStatistics;

import java.util.*;

import static java.lang.Math.min;

public class Animal implements WorldElement, Comparable<Animal> {
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position;
    private final Genotype genotype;
    private int energy;
    private int age = 0;
    private int dayOfDeath;
    private int grassConsumed = 0;
    private final List<Animal> children = new ArrayList<Animal>();
    private final static Random rand = new Random();
    private final int genotypeStartingIndex;
    private final boolean oldNotGold;
    private final String animalPath;

    public Animal(Vector2d position, Genotype genotype, int energy, boolean oldNotGold, int genotypeStartingIndex) {
        this.position = position;
        this.genotype = genotype;
        this.energy = energy;
        this.oldNotGold = oldNotGold;
        this.genotypeStartingIndex = genotypeStartingIndex;
        this.animalPath = "url('animal" + (int)(Math.random() * 4 + 1) + ".png');";;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return this.orientation.toString();
    }

    public String toRegionStyle(String backgroundColor) {
        return "-fx-background-color: " + backgroundColor + "; " +
                "-fx-background-image: " + animalPath + "; " +
                "-fx-background-size: contain; " +
                "-fx-background-repeat: no-repeat;";
    }

    public int getGrassConsumed() {
        return this.grassConsumed;
    }
    public int getDayOfDeath() {
        return this.dayOfDeath;
    }
    public int getActiveGene() {
        return (genotypeStartingIndex + age) % genotype.getSize();
    }

    public void move(MoveValidator validator, int rightEdge) {
        // possible skip of move due to age, probability stops increasing after 80%
        if (!oldNotGold || rand.nextInt(100) >= min(age, 80)) {
            for (int i = 0; i < this.genotype.getGene((genotypeStartingIndex + age) % genotype.getSize()); i++) {
                this.orientation = orientation.next();
            }

            Vector2d newPosition = this.position.add(this.orientation.toUnitVector());
            if (!validator.canMoveTo(newPosition)) {
                this.orientation=orientation.next().next().next().next();
            }
            else if (!validator.aroundTheWorld(newPosition)) {
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

    public int getAge(){
        return this.age;
    }

    public int getNumberOfChildren(){
        return this.children.size();
    }

    public void consume(int energy) {
        this.energy += energy;
        this.grassConsumed++;
    }

    public void giveBirth(int childCost, Animal baby) {
        this.energy -= childCost;
        this.children.add(baby);
    }

    public void unlive(int dayOfSimulation) {
        this.dayOfDeath = dayOfSimulation;
    }

    public int countDescendants() {
        Set<Animal> descendants = new HashSet<>();
        for (Animal child : children) {
            descendants.add(child);
            child.addDescendants(descendants);
        }
        return descendants.size();
    }

    private void addDescendants(Set<Animal> descendants) {
        for (Animal child : children) {
            descendants.add(child);
            child.addDescendants(descendants);
        }
    }

    @Override
    public int compareTo(Animal other) {
        // Compare by energy
        if (this.energy != other.energy) {
            return Integer.compare(this.energy, other.energy);
        }

        // Compare by age
        if (this.age != other.age) {
            return Integer.compare(this.age, other.age);
        }

        // Compare by number of children
        if (this.children.size() != other.children.size()) {
            return Integer.compare(this.children.size(), other.children.size());
        }

        // Decide by randomness
        return Math.random() <= 0.5 ? -1 : 1;
    }

    public SubjectStatistics getStatistics() {
        Genotype genotype = this.genotype;
        int activeGene = getActiveGene();
        int particularAge = getAge();
        int particularChildren = getNumberOfChildren();
        int grassConsumed = getGrassConsumed();
        int particularEnergy = getEnergy();
        int descendants = countDescendants();
        int dayOfDeath = getDayOfDeath();
        return new SubjectStatistics(genotype,activeGene,particularEnergy,grassConsumed,particularChildren,descendants,particularAge,dayOfDeath);
    }
}