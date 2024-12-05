package agh.ics.oop;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final List<Simulation> simulations;
    private final List<Thread> simulationThreads = new LinkedList<>();
    private final ExecutorService simulationThreadPool = Executors.newFixedThreadPool(4);

    public SimulationEngine(List<Simulation> simulations) {
        this.simulations = simulations;
    }

    public void runSync() {
        for (Simulation simulation : simulations)
            simulation.run();
    }

    public void runAsync() {
        for (Simulation simulation : simulations) {
            Thread simulationThread = new Thread(simulation);
            simulationThreads.add(simulationThread);
            simulationThread.start();
        }
        awaitSimulationsEnd();
    }

    public void awaitSimulationsEnd() {
        try {
            for (Thread thread : simulationThreads) {
                thread.join();
            }
            simulationThreadPool.shutdown();
            if (!simulationThreadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("Timeout. Force shutting down the thread pool.");
                simulationThreadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void runAsyncInThreadPool() {
        for (Simulation simulation : simulations) {
            simulationThreadPool.submit(simulation);
        }
        awaitSimulationsEnd();
    }
}
