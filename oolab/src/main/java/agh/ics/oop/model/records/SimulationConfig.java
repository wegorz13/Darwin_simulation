package agh.ics.oop.model.records;

import java.util.HashMap;
import java.util.Map;

public record SimulationConfig(int basePopulation,
                               int baseGrassNumber,
                               int grassPerDay,
                               int mapWidth,
                               int mapHeight,
                               int genotypeLength,
                               int childCost,
                               int minMutations,
                               int maxMutations,
                               int grassCalorificValue,
                               int baseEnergy,
                               int readyToParent,
                               int numberOfReservoirs,
                               boolean oldNotGold,
                                boolean saveStats) {

    public Map<String, Integer> changeToJsonFormat() {
        Map<String, Integer> map = new HashMap<>();
        map.put("basePopulation", basePopulation);
        map.put("baseGrassNumber", baseGrassNumber);
        map.put("grassPerDay", grassPerDay);
        map.put("mapWidth", mapWidth);
        map.put("mapHeight", mapHeight);
        map.put("genotypeLength", genotypeLength);
        map.put("childCost", childCost);
        map.put("minMutations", minMutations);
        map.put("maxMutations", maxMutations);
        map.put("grassCalorificValue", grassCalorificValue);
        map.put("baseEnergy", baseEnergy);
        map.put("readyToParent", readyToParent);
        map.put("numberOfReservoirs", numberOfReservoirs);
        map.put("oldNotGold", oldNotGold ? 1 : 0);
        map.put("saveStats", saveStats ? 1 : 0);
        return map;
    }
}
