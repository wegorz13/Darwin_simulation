package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.SimulationConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Simulation extends Thread {
    private final SimulationConfig config;
    private final GrassField map;
    private boolean running = true;
    Object lock = new Object();

    public Simulation(SimulationConfig config) {
        this.config = config;
        this.map = new GrassField(config);
    }

    public GrassField getMap() {
        return map;
    }

    public void onClickResume() {
        running = true;
        synchronized (lock) {
            lock.notify();
        }
    }

    public void onClickPause() {
        running = false;
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()) {
                synchronized (lock) {
                    while (!running) {
                        lock.wait();
                    }
                }
                this.map.dayPasses();

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}