package agh.ics.oop.model.util;

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
}
