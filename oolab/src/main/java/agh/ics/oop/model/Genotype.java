package agh.ics.oop.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genotype {
    private final List<Integer> genes;
    private final Random rand = new Random();

    public Genotype(int size) {
        List<Integer> genes = new ArrayList<>(size);
        for (int index = 0; index < size; index++) {
            genes.add(rand.nextInt(8));
        }
        this.genes = genes;
    }

    public Genotype(Animal parent1, Animal parent2, int lowerBoundOfMutations, int upperBoundOfMutations) {
        // swap parents to assure parent1 is stronger
        if (parent1.getEnergy() < parent2.getEnergy()) {
            Animal swapReference = parent1;
            parent1 = parent2;
            parent1 = swapReference;
        }

        List<Integer> genes = new ArrayList<>(parent1.getGenotype().getSize());
        int numberOfElementsFromParent1 = (int) (parent1.getGenotype().getSize() * Math.ceil((double) parent1.getEnergy() / (parent1.getEnergy() + parent2.getEnergy())));
        int numberOfElementsFromParent2 = parent2.getGenotype().getSize() - numberOfElementsFromParent1;
        if (rand.nextInt(2) == 0) {
            // left side
            for (int index = 0; index < parent1.getGenotype().getSize(); index++) {
                genes.set(index, index < numberOfElementsFromParent1 ? parent1.getGenotype().getGene(index) : parent2.getGenotype().getGene((index)));
            }
        } else {
            // right side
            for (int index = 0; index < parent1.getGenotype().getSize(); index++) {
                genes.set(index, index < numberOfElementsFromParent2 ? parent2.getGenotype().getGene(index) : parent1.getGenotype().getGene((index)));
            }
        }

        // mutations
        int numberOfMutations = rand.nextInt(upperBoundOfMutations - lowerBoundOfMutations + 1) + lowerBoundOfMutations;
        for (int mutation = 0; mutation < numberOfMutations; mutation++) {
            int index = rand.nextInt(genes.size());
            int gene = rand.nextInt(8);
            genes.set(index, gene);
        }

        this.genes = genes;
    }

    public int getSize() {
        return genes.size();
    }

    public int getGene(int index) {
        return genes.get(index);
    }

    public List<Integer> getGenes() {
        return new ArrayList<>(this.genes);
    }
}