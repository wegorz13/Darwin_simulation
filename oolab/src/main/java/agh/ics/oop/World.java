package agh.ics.oop;

import agh.ics.oop.model.*;
import javafx.application.Application;

import java.util.LinkedList;
import java.util.List;

public class World {
    public static void main(String[] args) {
        Application.launch(SimulationApp.class, args);
//        List<MoveDirection> directions = OptionsParser.parse(args);
//        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
//
//        List<Simulation> simulations = new LinkedList<>();
//        for (int i = 0; i < 2000; i++) {
//            GrassField map = new GrassField(100);
//            map.addListener(new ConsoleMapDisplay());
//            simulations.add(new Simulation(positions, directions, map));
//        }
//        SimulationEngine engine = new SimulationEngine(simulations);
//        engine.runAsyncInThreadPool();
//
//        System.out.println("System zakończył działanie");
    }
}