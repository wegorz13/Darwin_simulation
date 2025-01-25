package agh.ics.oop.model.util;

import agh.ics.oop.model.Genotype;

public record Statistics (int dayOfSimulation,int animalsNumber, int grassNumber, int freePositionsNumber, double averageEnergy, double averageLifetime, double averageNumberOfChildren,Genotype mostPopularGenotype,SubjectStatistics subjectStatistics){}
