package agh.ics.oop.model;

import agh.ics.oop.model.util.*;
import org.w3c.dom.ls.LSOutput;

import java.util.*;


public class GrassField implements WorldMap {
    private final Map<Vector2d, Grass> grasses;
    public final List<Animal> aliveAnimals = new ArrayList<Animal>();
    private final List<Animal> deadAnimals = new ArrayList<Animal>();
    private final SimulationConfig config;
    private final GrassGenerator grassGenerator;
    private int lordsDay = 0;
    //atrybuty z abstractworldmap
    protected final UUID id = UUID.randomUUID();
    protected Vector2d leftDownCorner;
    protected Vector2d rightUpCorner;
    protected final Map<Vector2d, ArrayList<Animal>> animals = new HashMap<Vector2d, ArrayList<Animal>>();
    protected final List<WaterReservoir> reservoirs = new ArrayList<>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    protected final List<MapChangeListener> listeners = new ArrayList<>();

    public GrassField( SimulationConfig config) {
        this.config = config;

        this.grasses = new HashMap<Vector2d, Grass>();
        this.grassGenerator = new GrassGenerator(config.mapWidth(), config.mapHeight());

        Random rand = new Random();

        this.leftDownCorner = new Vector2d(0, 0);
        this.rightUpCorner = new Vector2d(config.mapWidth() - 1, config.mapHeight() - 1);

        // place animals
        for (int i = 0; i < config.basePopulation(); i++) {
            Vector2d position = new Vector2d(rand.nextInt(config.mapWidth()), rand.nextInt(config.mapHeight()));
            Genotype genotype = new Genotype(config.genotypeLength());

            Animal newAnimal = new Animal(position, genotype, config.baseEnergy(), config.mapWidth() - 1, config.oldNotGold(), rand.nextInt(config.genotypeLength()));
            this.place(newAnimal);
            this.aliveAnimals.add(newAnimal);
        }

        // spawn grass
        for (int numberOfGrass = 0; numberOfGrass < config.baseGrassNumber(); numberOfGrass++) {
            try {
                Grass grass = grassGenerator.iterator().next();
                this.grasses.put(grass.getPosition(), grass);
            } catch (NoSuchElementException e) {
                break;
            }
        }

        //create reservoirs
        for (int i = 0; i < config.numberOfReservoirs(); i++) {
            this.reservoirs.add(new WaterReservoir(config.mapWidth(), config.mapHeight(), config.numberOfReservoirs()));
        }
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        for (WaterReservoir reservoir : reservoirs) {
            if (position.follows(reservoir.getLeftDownCorner()) & position.precedes(reservoir.getRightUpCorner())) {
                return new Water();
            }
        }
        List<Animal> list = animals.get(position);
        if (list!=null && !list.isEmpty()) return list.getFirst();
        else return grasses.getOrDefault(position, null);
    }

    @Override
    public List<WorldElement> getElements() {
        List<WorldElement> elements = new LinkedList<>();
        for (List<Animal> animalsList : animals.values()) {
            elements.addAll(animalsList);
        }
        elements.addAll(grasses.values());
        return elements;
    }

    private Animal createAnimal(Animal parent1, Animal parent2) {
        return new Animal(parent1.getPosition(), new Genotype(parent1, parent2, config.minMutations(), config.maxMutations()),config.childCost() * 2, rightUpCorner.getX(),config.oldNotGold(),(int)(Math.random()*(parent1.getGenotype().getSize())));
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
            List<Animal> animalsAtPosition = animals.get(grass.getPosition());
            Animal strongestAnimal = animalsAtPosition.getFirst();
            for (Animal animal : animalsAtPosition) {
                strongestAnimal = strongestAnimal.compare(animal);
            }

            strongestAnimal.consume(config.grassCalory());
            grassGenerator.addEatenGrassBack(grass);
            grasses.remove(grass.getPosition());
        }
    }

    private void clearingStage() {
        int recentlyDead = 0;

        for (Map.Entry<Vector2d, ArrayList<Animal>> entry : this.animals.entrySet()) {
            Vector2d position = entry.getKey();
            List<Animal> animalsAtPosition = entry.getValue();

            //usuwamy zwierzęta które znalazły się w wodzie
            for (WaterReservoir reservoir : reservoirs) {
                if (position.follows(reservoir.getLeftDownCorner()) && position.precedes(reservoir.getRightUpCorner())) {
                    for (Animal animal : animalsAtPosition) {
                        if (animal.getEnergy()>0){
                            animal.unlive(lordsDay);
                            recentlyDead++;
                            deadAnimals.add(animal);
                        }
                    }
                }
            }
            //usuwamy zwierzęta bez energii
            for (Animal animal : animalsAtPosition){
                if (animal.getEnergy() <= 0) {
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
            aliveAnimals.remove(animal);
            if (animalsAtPosition.isEmpty()) {
                this.animals.remove(animal.getPosition());
            }
        }
    }

    private void lovingStage(){
        for (List<Animal> animalsAtPosition: animals.values()){
            Animal daddyAnimal;
            Animal mommyAnimal;
            int numberOfAnimals = animalsAtPosition.size();
            if (numberOfAnimals >= 2) {
                daddyAnimal = animalsAtPosition.getFirst();
                for (Animal animal : animalsAtPosition) { //wybieramy tate jako najsilniejszego zwierzaka
                    daddyAnimal = animal.compare(daddyAnimal);
                }

                if (animalsAtPosition.getLast()!=daddyAnimal) {mommyAnimal = animalsAtPosition.getLast();}
                else {mommyAnimal = animalsAtPosition.getFirst();} //ustawiamy mamę na dowolnego innego od juz wybranego

                for (Animal animal : animalsAtPosition) {
                    if (animal!=daddyAnimal){ //wybieramy mamę
                        mommyAnimal=mommyAnimal.compare(animal);
                    }
                }
                if (mommyAnimal.getEnergy() >= config.readyToParent() && daddyAnimal.getEnergy() >= config.readyToParent()){
                    Animal babyAnimal = createAnimal(mommyAnimal, daddyAnimal);
                    mommyAnimal.giveBirth(config.childCost(), babyAnimal);
                    daddyAnimal.giveBirth(config.childCost(), babyAnimal);
                    animalsAtPosition.add(babyAnimal);
                    aliveAnimals.add(babyAnimal);
                }
            }
        }
    }

    private void pollinationStage(){
        for (int numberOfGrass = 0; numberOfGrass < config.grassPerDay(); numberOfGrass++) {
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
        this.pollinationStage();
        lordsDay+=1;
    }

    //metody z abstractworldmap
    @Override
    public boolean canMoveTo(Vector2d position) {
        for (WaterReservoir reservoir : reservoirs){
            if (position.follows(reservoir.getLeftDownCorner()) & position.precedes(reservoir.getRightUpCorner())){
                return false;
            }
        }
        return (position.getX() >= leftDownCorner.getX() && position.getX()<= rightUpCorner.getX());
    }

    @Override
    public boolean aroundTheWorld(Vector2d position) {
        return (position.getY() >= leftDownCorner.getY() && position.getY()<= rightUpCorner.getY());
    }

    @Override
    public void place(Animal animal)  {
        Vector2d position = animal.getPosition();
        if (!animals.containsKey(position)) animals.put(position, new ArrayList<>());
        this.animals.get(position).add(animal);
        mapChanged("Animal placed at %s".formatted(position));
    }

    @Override
    public void move(Animal animal) {
        Vector2d fromPosition = animal.getPosition();

        ArrayList<Animal> animalsAtFrom = this.animals.get(fromPosition);
        if (animalsAtFrom != null) {
            animalsAtFrom.remove(animal);

            //nie wiem czemu sie psuje jak nie usuwam, moja propozycja to tworzyć nową hashmapę codziennie na podstawie aliveAnimals,
            // i tak trzeba przesunąć każde zwierzę, równie dobrze można je wstawić na nowo
            if (animalsAtFrom.isEmpty()) {
                this.animals.remove(fromPosition);
            }
        }

        animal.move(this);

        this.place(animal);

        mapChanged("Animal moved from %s to %s".formatted(fromPosition, animal.getPosition()));
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public String toString() {
        Boundary drawCorners = getCurrentBounds();
        return visualizer.draw(drawCorners.leftDownCorner(), drawCorners.rightUpCorner());
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(leftDownCorner, rightUpCorner);
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    public void addListener(MapChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MapChangeListener listener) {
        listeners.remove(listener);
    }

    protected void mapChanged(String message) {
        for (MapChangeListener listener : listeners)
            listener.mapChanged(this, message);
    }

    @Override
    public Statistics getStatistics(){
        int animalsNumber = this.aliveAnimals.size();
        int grassNumber = this.grasses.size();
        // to z genotypem trzeba zrobic ale teraz mi sie nie chce
        Genotype mostPopularGenotype = new Genotype(config.genotypeLength());
        int freePositionsNumber = config.mapWidth()*config.mapHeight()-animals.size()-grasses.size();
        int sumOfEnergy=0;
        int sumOfAge=0;
        int sumOfChildren=0;

        for (Animal animal : aliveAnimals) {
            sumOfEnergy+=animal.getEnergy();
            sumOfChildren+=animal.getNumberOfChildren();
        }
        for (Animal animal : deadAnimals){
            sumOfAge+=animal.getAge();
        }

        double averageEnergy = (double) sumOfEnergy / animalsNumber;
        double averageLifetime = (double) sumOfAge / animalsNumber;
        double averageChildren = (double) sumOfChildren / animalsNumber;

        for (WaterReservoir reservoir : reservoirs){
            int area = (reservoir.getRightUpCorner().getX()-reservoir.getLeftDownCorner().getX())*(reservoir.getRightUpCorner().getY()-reservoir.getLeftDownCorner().getY());
            freePositionsNumber-=area;
        }

        return new Statistics(animalsNumber,grassNumber,freePositionsNumber,averageEnergy,averageLifetime,averageChildren,mostPopularGenotype);
    }
}