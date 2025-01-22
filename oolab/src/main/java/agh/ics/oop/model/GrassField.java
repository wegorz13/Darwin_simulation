package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.GrassGenerator;
import agh.ics.oop.model.util.RandomPositionGenerator;

import javax.lang.model.element.AnnotationMirror;
import java.util.*;


public class GrassField extends AbstractWorldMap {
    private final Map<Vector2d, Grass> grasses;
    public final List<Animal> aliveAnimals = new ArrayList<Animal>();
    private final List<Animal> deadAnimals = new ArrayList<Animal>();
    private final GrassGenerator grassGenerator;
    private final int childCost;
    private final int minMutations;
    private final int maxMutations;
    private final int grassCalory;
    private final int readyToParent;
    private final int grassPerDay;
    private int lordsDay = 0;
    private final boolean oldNotGold;


    public GrassField(int numberOfAnimals, int startingGrass,int numberOfReservoirs ,int grassPerDay,int width, int height, int genotypeLength, int childCost, int minMutations, int maxMutations, int grassCalory, int baseEnergy, int readyToParent, boolean oldNotGold) {
        this.childCost = childCost;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.grassCalory = grassCalory;
        this.readyToParent = readyToParent;
        this.grassPerDay = grassPerDay;
        this.oldNotGold = oldNotGold;

        this.grasses = new HashMap<Vector2d, Grass>();
        this.grassGenerator = new GrassGenerator(width, height);

        Random rand = new Random();

        super.leftDownCorner = new Vector2d(0, 0);
        super.rightUpCorner = new Vector2d(width - 1, height - 1);

        // place animals
        for (int i = 0; i < numberOfAnimals; i++) {
            Vector2d position = new Vector2d(rand.nextInt(width), rand.nextInt(height));
            Genotype genotype = new Genotype(genotypeLength);

            Animal newAnimal = new Animal(position, genotype, baseEnergy, width - 1,oldNotGold);
            this.place(newAnimal);
            this.aliveAnimals.add(newAnimal);
        }

        // spawn grass
        for (int numberOfGrass = 0; numberOfGrass < startingGrass; numberOfGrass++) {
            try {
                Grass grass = grassGenerator.iterator().next();
                this.grasses.put(grass.getPosition(), grass);
            } catch (NoSuchElementException e) {
                break;
            }
        }

        //create reservoirs
        for (int i = 0; i < numberOfReservoirs; i++) {
            this.reservoirs.add(new WaterReservoir(width,height,numberOfReservoirs));
        }
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement objectAtAnimal = super.objectAt(position);
        return objectAtAnimal != null ? objectAtAnimal : grasses.getOrDefault(position, null);
    }

    @Override
    public List<WorldElement> getElements() {
        List<WorldElement> elements = super.getElements();
        elements.addAll(grasses.values());
        return elements;
    }

    private Animal createAnimal(Animal parent1, Animal parent2) {
//        return new Animal(parent1.getPosition(), new Genotype(parent1, parent2, minMutations, maxMutations),childCost * 2, rightUpCorner.getX(),this.oldNotGold);
        return new Animal(parent1.getPosition(), new Genotype(4),childCost * 2, rightUpCorner.getX(),this.oldNotGold);
    }

    private void movingStage() {
        for (Animal animal : aliveAnimals) {
            this.move(animal);
        }
    }

    private void eatingStage(){
        List<Grass> eatenGrass = new ArrayList<>();
        for (Grass grass : grasses.values()){
            List<Animal> animalsAtPosition = animals.get(grass.getPosition());
            if (animalsAtPosition != null){
                eatenGrass.add(grass);
            }
        }

        for (Grass grass : eatenGrass) {
            Animal strongestAnimal = animals.get(grass.getPosition()).getLast();
            strongestAnimal.consume(grassCalory);
            grassGenerator.addEatenGrassBack(grass);
            grasses.remove(grass.getPosition());
        }
    }

    private void clearingStage() {

        int recentlyDead = 0;
        for (List<Animal> animalsAtPosition: animals.values()){
            for (Animal animal : animalsAtPosition){
                if (animal.getEnergy() == 0) {
                    animal.unlive(lordsDay);
                    recentlyDead++;
                    deadAnimals.add(animal);
                }
            }
        }

        for (int i=0;i<recentlyDead;i++){
            Animal animal = deadAnimals.get(deadAnimals.size()-1-i);
            List<Animal> animalsAtPosition = animals.get(animal.getPosition());
            animalsAtPosition.remove(animal);
            if (animalsAtPosition.isEmpty()) {
                this.animals.remove(animal.getPosition());
            }
        }
    }

    private void lovingStage(){
        for (List<Animal> animalsAtPosition: animals.values()){
            int numberOfAnimals = animalsAtPosition.size();
            if (numberOfAnimals >= 2) {
                Animal mommyAnimal = animalsAtPosition.get(numberOfAnimals-1);
                Animal daddyAnimal = animalsAtPosition.get(numberOfAnimals-2);

                if (mommyAnimal.getEnergy() >= readyToParent && daddyAnimal.getEnergy() >= readyToParent){
                    Animal babyAnimal = createAnimal(mommyAnimal,daddyAnimal);
                    mommyAnimal.giveBirth(childCost,babyAnimal);
                    daddyAnimal.giveBirth(childCost,babyAnimal);
                    animalsAtPosition.add(babyAnimal);
                    aliveAnimals.add(babyAnimal);
                }

            }

        }
    }

    private void pollutingStage(){
        for (int numberOfGrass = 0; numberOfGrass < grassPerDay; numberOfGrass++) {
            try {
                Grass grass = grassGenerator.iterator().next();
                this.grasses.put(grass.getPosition(), grass);
            } catch (NoSuchElementException e) {
                break;
            }
        }
    }

    private void waterCycle(){
        for (WaterReservoir reservoir: this.reservoirs){
            reservoir.updateSize();
        }
    }

    public void dayPasses(){
        this.waterCycle();
        this.clearingStage();
        this.movingStage();
        this.eatingStage();
        this.lovingStage();
        this.pollutingStage();
        lordsDay+=1;
    }
}