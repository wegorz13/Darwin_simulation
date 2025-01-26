package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Simulation implements Runnable {
    private final GrassField map;

    public Simulation(GrassField map) {
        this.map = map;
    }

    @Override
    public void run() {
        while (true) {
            this.map.dayPasses();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}