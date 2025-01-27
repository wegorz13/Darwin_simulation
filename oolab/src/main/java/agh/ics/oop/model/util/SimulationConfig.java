package agh.ics.oop.model.util;

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
                               int grassCalory,
                               int baseEnergy,
                               int readyToParent,
                               int numberOfReservoirs,
                               boolean oldNotGold) {

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
        map.put("grassCalory", grassCalory);
        map.put("baseEnergy", baseEnergy);
        map.put("readyToParent", readyToParent);
        map.put("numberOfReservoirs", numberOfReservoirs);
        map.put("oldNotGold", oldNotGold ? 1 : 0);
        return map;
    }
}
