package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.GrassGenerator;
import javafx.application.Application;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class World {
    public static void main(String[] args) {
//        Genotype gen = new Genotype(4);
//        System.out.println(gen.getGenes());
//        Application.launch(SimulationApp.class, args);
        GrassField map = new GrassField(2, 5, 5, 5, 4, 5, 0, 1, 10, 5, 8);
        System.out.println(map);
        map.dayPasses();
        System.out.println(map);
//        System.out.println("System zakończył działanie");

//        GrassGenerator rand = new GrassGenerator(3, 3);
//        int grassPerDay = 100;
//        for (int numberOfGrass = 0; numberOfGrass < grassPerDay; numberOfGrass++) {
//            try {
//                Grass grass = rand.iterator().next();
//                System.out.println(grass.getPosition());
//            } catch (NoSuchElementException e) {
//                break;
//            }
//        }
    }
}