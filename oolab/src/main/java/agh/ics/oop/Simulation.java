package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.records.SimulationConfig;
import agh.ics.oop.model.records.Statistics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Simulation extends Thread {
    private final SimulationConfig config;
    private final GrassField map;
    private boolean running = true;
    private final Object lock = new Object();
    private final String statsFilePath = "stats" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".csv";

    public Simulation(SimulationConfig config) {
        this.config = config;
        this.map = new GrassField(config);

        if (config.saveStats()){
            try {
                if (!Files.exists(Paths.get(statsFilePath))) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(statsFilePath, true))) {
                        writer.write("Animals,Grass,Free Positions,Avg Energy,Avg Lifetime,Avg Children,Most Popular Genotype, Day of Simulation");
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                System.err.println("Error initializing statistics file: " + e.getMessage());
            }
        }
    }

    public void saveStatistics(Statistics statistics){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(statsFilePath, true))) {
            String line = String.format("%d,%d,%d,%.2f,%.2f,%.2f,%s,%d",
                    statistics.animalsNumber(),
                    statistics.grassNumber(),
                    statistics.freePositionsNumber(),
                    statistics.averageEnergy(),
                    statistics.averageLifetime(),
                    statistics.averageNumberOfChildren(),
                    statistics.mostPopularGenotype(),
                    map.getDayOfSimulation());


            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving statistics: " + e.getMessage());
        }
    }

    public SimulationConfig getConfig() {
        return config;
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

                if (config.saveStats()){
                    saveStatistics(this.map.getStatistics());
                }

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}