package agh.ics.oop.model.records;

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