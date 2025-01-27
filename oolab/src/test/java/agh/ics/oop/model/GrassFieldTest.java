package agh.ics.oop.model;

import static org.junit.jupiter.api.Assertions.*;

import agh.ics.oop.model.records.SimulationConfig;
import agh.ics.oop.model.records.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GrassFieldTest {
    private GrassField grassField;
    private SimulationConfig config;

    @BeforeEach
    void setUp() {
        config = new SimulationConfig(
                10,
                10,
                5,
                15,
                15,
                2,
                10,
                0,
                1,
                15,
                80,
                20,
                0,
                false,
                false
        );

        grassField = new GrassField(config);
    }

    @Test
    void testInitialization() {
        assertEquals(config.basePopulation(), grassField.getStatistics().animalsNumber());
        assertEquals(config.baseGrassNumber(), grassField.getStatistics().grassNumber());
    }

    @Test
    void testAnimalMovement() {
        Animal animal = new Animal(new Vector2d(config.mapWidth()/2, config.mapHeight()/2), new Genotype(1),80,false,0);

        Vector2d originalPosition = animal.getPosition();

        grassField.place(animal);
        grassField.move(animal);
        Vector2d newPosition = animal.getPosition();

        assertNotEquals(originalPosition, newPosition);
        assertTrue(grassField.isOccupied(newPosition));
    }

    @Test
    void testEatingGrass() {
        Grass grass = grassField.getElements().stream()
                .filter(element -> element instanceof Grass)
                .map(element -> (Grass) element)
                .findFirst()
                .orElseThrow();

        Animal animal = new Animal(grass.getPosition(), new Genotype(config.genotypeLength()), config.baseEnergy(),  config.oldNotGold(), 0);
        grassField.place(animal);

        grassField.eatingStage();

        assertFalse(grassField.grasses.containsKey(animal.getPosition()));
        assertTrue(grassField.isOccupied(animal.getPosition()));
        assertTrue(animal.getEnergy() > config.baseEnergy());
    }

    @Test
    void testBreedingAnimals() {
        Animal parent1 = new Animal(new Vector2d(0, 0), new Genotype(config.genotypeLength()), config.readyToParent(),  config.oldNotGold(), 0);
        Animal parent2 = new Animal(new Vector2d(0, 0), new Genotype(config.genotypeLength()), config.readyToParent() ,  config.oldNotGold(), 0);
        grassField.place(parent1);
        grassField.place(parent2);

        List<Animal> animalsAtPosition = grassField.getElements().stream()
                .filter(element -> element instanceof Animal)
                .map(element -> (Animal) element)
                .toList();

        int numberOfAnimals = animalsAtPosition.size();

        grassField.lovingStage();

        List<Animal> animalsAtPositionAfter = grassField.getElements().stream()
                .filter(element -> element instanceof Animal)
                .map(element -> (Animal) element)
                .toList();

        assertTrue(animalsAtPositionAfter.size()>numberOfAnimals);
    }

    @Test
    void testClearingDeadAnimals() {
        Animal animal = new Animal(new Vector2d(0, 0), new Genotype(config.genotypeLength()), 0,  config.oldNotGold(), 0);
        grassField.place(animal);

        grassField.clearingStage();

        assertEquals(grassField.getStatistics().animalsNumber(), config.basePopulation());
    }

    @Test
    void testGrassRegrowth() {
        int initialGrassCount = grassField.grasses.size();

        grassField.pollinationStage();

        assertTrue(grassField.getStatistics().grassNumber() > initialGrassCount);
    }

    @Test
    void testGetStatistics() {
        // Call the method under test
        Statistics statistics = grassField.getStatistics();

        // Assertions
        assertNotNull(statistics);
        assertEquals(10, statistics.animalsNumber());
        assertEquals(10, statistics.grassNumber());
        assertEquals(0, statistics.averageNumberOfChildren());


        assertEquals(80.0, statistics.averageEnergy());
        assertEquals(0, statistics.averageLifetime());
    }
}
