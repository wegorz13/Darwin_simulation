package agh.ics.oop.model.util;

import agh.ics.oop.model.Genotype;

public record SubjectStatistics (Genotype particularGenotype,
                                 int activeGene,
                                 int subjectEnergy,
                                 int grassConsumed,
                                 int subjectChildren,
                                 int descendants,
                                 int subjectAge,
                                 int dayOfDeath) {
}