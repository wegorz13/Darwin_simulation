package agh.ics.oop.model.util;

import agh.ics.oop.model.Genotype;

public record SubjectStatistics (Genotype particularGenotype,
                                 int activeGene,
                                 int particularEnergy,
                                 int grassConsumed,
                                 int particularChildren,
                                 int descendants,
                                 int particularAge,
                                 int dayOfDeath) {
}