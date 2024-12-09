package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener{
    private int updateCount = 0;

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        synchronized (System.out) {
            System.out.println("Map Id: " + worldMap.getId());
            System.out.println(message);
            System.out.println(worldMap);
            System.out.println(++updateCount);
        }
    }
}
