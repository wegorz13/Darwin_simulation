package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WaterReservoir {
    private final Map<Vector2d,List<Water>> waters;
    private final static List<Vector2d> FIRST_STAGE_VECTORS = List.of(new Vector2d(0, 1), new Vector2d(0, -1),new Vector2d(1, 0),new Vector2d(-1, 0));
    private final static List<Vector2d> SECOND_STAGE_VECTORS = List.of(new Vector2d(1, 1), new Vector2d(1, -1),new Vector2d(-1, 1),new Vector2d(-1, -1),new Vector2d(0, 2), new Vector2d(0, -2),new Vector2d(2, 0),new Vector2d(-2, 0));
    private final List<Water> firstStageWaters = new ArrayList<>();
    private final List<Water> secondStageWaters = new ArrayList<>();
    private int phase = 0;
    private int dayOfCycle=0;

    private final Runnable[] tideCycle = new Runnable[]{
            () -> {},
            () -> {},
            this::highTide,
            this::highTide,
            () -> {},
            () -> {},
            this::lowTide,
            this::lowTide};

    public WaterReservoir(Vector2d centerPosition, Map<Vector2d, List<Water>> waters) {
        this.waters = waters;
        for (Vector2d stageVector : WaterReservoir.FIRST_STAGE_VECTORS) {
            firstStageWaters.add(new Water(centerPosition.add(stageVector)));
        }
        for (Vector2d stageVector : WaterReservoir.SECOND_STAGE_VECTORS) {
            secondStageWaters.add(new Water(centerPosition.add(stageVector)));
        }
    }

    private void highTide(){
        if (phase == 0) {
            place(firstStageWaters);
            phase = 1;
        }
        else {
            place(secondStageWaters);
        }
    }

    private void place(List<Water> stageWaters) {
        for (Water water : stageWaters) {
            if (waters.containsKey(water.getPosition())) {
                waters.get(water.getPosition()).add(water);
            }
            else {
                List<Water> waterList = new ArrayList<>();
                waterList.add(water);
                waters.put(water.getPosition(), waterList);
            }
        }
    }

    private void remove(List<Water> stageWaters){
        for (Water water : stageWaters) {
            waters.get(water.getPosition()).remove(water);
            if (waters.get(water.getPosition()).isEmpty()) {
                waters.remove(water.getPosition());
            }
        }
    }

    private void lowTide(){
        if (phase == 0) {
            remove(firstStageWaters);
        }
        else {
            remove(secondStageWaters);
            phase = 0;
        }
    }

    public void updateSize() {
        this.tideCycle[dayOfCycle].run();
        dayOfCycle = (dayOfCycle + 1) % 8;
    }

}
