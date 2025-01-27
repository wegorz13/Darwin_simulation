package agh.ics.oop.model.records;

import agh.ics.oop.model.Genotype;

public record Statistics (int animalsNumber,
                          int grassNumber,
                          int freePositionsNumber,
                          double averageEnergy,
                          double averageLifetime,
                          double averageNumberOfChildren,
                          Genotype mostPopularGenotype){}
