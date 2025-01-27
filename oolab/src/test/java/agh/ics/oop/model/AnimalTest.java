package agh.ics.oop.model;

import agh.ics.oop.model.records.SimulationConfig;
import agh.ics.oop.model.records.SubjectStatistics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class AnimalTest {
    @Test
    public void moveTest() {
        Animal animal = new Animal(new Vector2d(2, 2), new Genotype(4), 10, false, 0);
        GrassField map = new GrassField(new SimulationConfig(0, 0, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, false));

        animal.move(map, map.getCurrentBounds().rightUpCorner().getX());
        assertEquals(1, animal.getAge());
        assertEquals(9, animal.getEnergy());
        assertEquals(1, animal.getActiveGene());
        assertEquals(0, animal.getGrassConsumed());
    }

    @Test
    public void giveBirthTest() {
        Animal animal = new Animal(new Vector2d(2, 2), new Genotype(4), 10, false, 0);
        Animal baby = new Animal(new Vector2d(2, 2), new Genotype(4), 10, false, 0);
        Animal child = new Animal(new Vector2d(2, 2), new Genotype(4), 10, false, 0);


        animal.giveBirth(4, baby);
        baby.giveBirth(4, child);

        assertEquals(6, animal.getEnergy());
        assertEquals(1, animal.getNumberOfChildren());
        assertEquals(2, animal.countDescendants());
        assertEquals(1, baby.countDescendants());
        assertEquals(0, child.countDescendants());
    }

    @Test
    public void compareTo() {
        Animal animal1 = new Animal(new Vector2d(2, 2), new Genotype(4), 10, false, 0);
        Animal animal2 = new Animal(new Vector2d(2, 2), new Genotype(4), 9, false, 0);
        GrassField map = new GrassField(new SimulationConfig(0, 0, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, false));

        assertEquals(1, animal1.compareTo(animal2));

        animal1.move(map, map.getCurrentBounds().rightUpCorner().getX());
        animal1.move(map, map.getCurrentBounds().rightUpCorner().getX());

        assertEquals(-1, animal1.compareTo(animal2));

        Animal animalA = new Animal(new Vector2d(2, 2), new Genotype(4), 10, false, 0);
        Animal animalB = new Animal(new Vector2d(2, 2), new Genotype(4), 10, false, 0);

        animalA.giveBirth(0, animal1);

        assertEquals(1, animalA.compareTo(animalB));
        assertEquals(-1, animalB.compareTo(animalA));
    }

    @Test
    public void getStatistics() {
        Animal animal = new Animal(new Vector2d(2, 2), new Genotype(4), 10, false, 0);

        SubjectStatistics statistics = animal.getStatistics();

        assertEquals(0, statistics.activeGene());
        assertEquals(0, statistics.subjectAge());
        assertEquals(0, statistics.subjectChildren());
        assertEquals(0, statistics.grassConsumed());
        assertEquals(10, statistics.subjectEnergy());
        assertEquals(0, statistics.descendants());
    }
}