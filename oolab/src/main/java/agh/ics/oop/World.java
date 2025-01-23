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
        GrassField map = new GrassField(7, 3, 1,5, 10, 10, 4, 4, 0, 1, 4, 5, 6,true);
        System.out.println(map);
        for (int i=0;i<20;i++){
            map.dayPasses();
            System.out.println(map);
            System.out.println(map.aliveAnimals.size());
            if (map.aliveAnimals.size()==0) {
                System.out.println("animals survived " + i + " days");
                break;
            }
        }

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