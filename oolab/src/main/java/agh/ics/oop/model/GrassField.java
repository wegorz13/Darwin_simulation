package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class GrassField extends AbstractWorldMap {
    private final Map<Vector2d, Grass> grasses;
    private final int genotypeLength;
    private final int grassGrowth;
    private final int childCost;
    private final Vector2d jungleRightUpCorner;
    private final Vector2d jungleLeftDownCorner;
    private final int minMutations;
    private final int maxMutations;
    private final int grassCalory;

    public GrassField(int numberOfAnimals,int numberOfGrass,int width, int height,int grassGrowth, int genotypeLength, int childCost, int minMutations, int maxMutations, int grassCalory) {
        this.grassGrowth=grassGrowth;
        this.childCost=childCost;
        this.genotypeLength=genotypeLength;
        this.minMutations=minMutations;
        this.maxMutations=maxMutations;
        this.grassCalory=grassCalory;

        this.leftDownCorner = new Vector2d(0, 0);
        this.rightUpCorner = new Vector2d(width-1, height-1);

        this.grasses = new HashMap<>();

        Random rand = new Random();

        int jungleHeight = rand.nextInt((int)(0.2*height), height/3);
        int jungleWidth = (int)(0.2*width*height/jungleHeight);

        jungleLeftDownCorner = new Vector2d(width/2-jungleWidth/2, height/2-jungleHeight/2);
        jungleRightUpCorner = new Vector2d(jungleLeftDownCorner.getX()+jungleWidth, jungleLeftDownCorner.getY()+jungleHeight);

        for (int i = 0; i < numberOfAnimals; i++) {
            Vector2d position = new Vector2d(rand.nextInt(width), rand.nextInt(height));

            int[] genotype = new int[7];
            for (int j = 0; j < 7; j++) {
                genotype[j] = rand.nextInt(8); // Random integer between 0 and 7
            }

            this.place(new Animal(position,genotype,10,null,null));
        }

        //jak dziala ten iterator, jak przejde petla for drugi raz to count sie resetuje?
        // trzeba by zapisac go w atrybucie do tworzenia roślin każdego dnia i dodać tworzenie trawy częściej na równiku
        RandomPositionGenerator grassPositionGenerator = new RandomPositionGenerator(width, height, numberOfGrass);
        for(Vector2d grassPosition : grassPositionGenerator) {
            this.grasses.put(grassPosition,new Grass(grassPosition));
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

    // Założyłem że mapa może się rozszerzać i kurczyć, kurczenie jest problematyczne - każdy ruch może być potencjalnie kurczący
    @Override
    public void move(Animal animal, MoveDirection direction) {
        super.move(animal, direction);
    }

    @Override
    public Boundary getCurrentBounds()  {
        List<WorldElement> elements = getElements();
        Vector2d lowerCorner = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Vector2d upperCorner = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (WorldElement element : elements) {
            lowerCorner = lowerCorner.lowerLeft(element.getPosition());
            upperCorner = upperCorner.upperRight(element.getPosition());
        }
        return new Boundary(lowerCorner, upperCorner);
    }

    private Animal createAnimal(Animal mommyAnimal, Animal daddyAnimal) {
        int leftShare = (mommyAnimal.getEnergy() / (mommyAnimal.getEnergy() + daddyAnimal.getEnergy()));
        int rightShare = genotypeLength -leftShare;

        int[] leftGenes = mommyAnimal.getGenotype();
        int [] rightGenes = daddyAnimal.getGenotype();

        int[] newGenotype = new int[genotypeLength];

        // Copy the leftShare number of elements from leftGenes
        System.arraycopy(leftGenes, 0, newGenotype, 0, leftShare);
        // Copy the remaining elements from rightGenes
        System.arraycopy(rightGenes, genotypeLength-rightShare, newGenotype, leftShare, rightShare);

        //tu jeszcze losowe mutacje
        Random rand = new Random();
        int mutationNumber = rand.nextInt(minMutations,maxMutations);
        for (int i = 0; i < mutationNumber; i++) {
            int index = rand.nextInt(genotypeLength);
            int mutation = rand.nextInt(8);
            newGenotype[index] = mutation;
        }

        mommyAnimal.giveBirth(childCost);
        daddyAnimal.giveBirth(childCost);

        return new Animal(mommyAnimal.getPosition(),newGenotype,childCost*2,mommyAnimal,daddyAnimal);
    }

    private void movingStage(){
        for (List<Animal> animalsAtPosition: animals.values()){
            for (Animal animal : animalsAtPosition){
                animal.move(this);
            }
        }
    }

    private void eatingStage(){
        for (Map.Entry<Vector2d,Grass> entry : grasses.entrySet()){
            List<Animal> animalsAtPosition = animals.get(entry.getKey());
            Animal strongestAnimal = animalsAtPosition.getLast();

            if (strongestAnimal!=null){
                strongestAnimal.consume(grassCalory);
                grasses.remove(entry.getKey());
            }
        }
    }

    private void clearingStage(){
        for (List<Animal> animalsAtPosition: animals.values()){
            for (Animal animal : animalsAtPosition){
                if (animal.getEnergy()==0){
                    animalsAtPosition.remove(animal);
                }
            }
        }
    }

    private void lovingStage(){
        for (List<Animal> animalsAtPosition: animals.values()){
            int numberOfAnimals = animalsAtPosition.size();
            if (numberOfAnimals>=2){
                Animal mommyAnimal = animalsAtPosition.get(numberOfAnimals-1);
                Animal daddyAnimal = animalsAtPosition.get(numberOfAnimals-2);

                if (mommyAnimal.getEnergy()>3 && daddyAnimal.getEnergy()>1){
                    Animal babyAnimal = createAnimal(mommyAnimal,daddyAnimal);
                    animalsAtPosition.add(babyAnimal);
                }

            }

        }
    }

    private void pollutingStage(){
        //tylko przekleilem z konstruktora
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(rightUpCorner.getX()+1, rightUpCorner.getY()+1, grassGrowth);
        for(Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    public void dayPasses(){
        this.clearingStage();
        this.movingStage();
        this.eatingStage();
        this.lovingStage();
        this.pollutingStage();
    }


}