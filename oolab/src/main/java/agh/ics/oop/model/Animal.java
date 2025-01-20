package agh.ics.oop.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal implements  WorldElement {
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position;
    private final int[] genotype;
    private int energy;
    private int dayOfCycle=0;
    private final Animal mommy;
    private final Animal daddy;
    private final List<Animal> babies = new ArrayList<Animal>();
    private int age=0;
    private final Random rand = new Random();
    private int dayOfDeath;
    private int grassConsumed=0;
    private final int rightEdge;

//    public Animal() {
//        this.genotype = new int[]{1, 0, 0, 1};
//        this.position = new Vector2d(2, 2);
//    }

    public Animal(Vector2d position, int[] genotype, int energy, Animal mommy, Animal daddy, int rightEdge) {
        this.position = position;
        this.genotype = genotype;
        this.energy = energy;
        this.mommy = mommy;
        this.daddy = daddy;
        this.rightEdge = rightEdge;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return this.orientation.toString();
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveValidator validator) {
        this.age++;

        boolean moves = rand.nextInt(age+100)>age;

        if (moves) {
            for (int i=0;i<this.genotype[this.dayOfCycle];i++) {
                this.orientation = orientation.next();
            }

            Vector2d newPosition = this.position.add(this.orientation.toUnitVector());

            if (!validator.canMoveHorizontal(newPosition)) {
                this.orientation=orientation.next().next().next().next();
            }
            else if (!validator.canMoveVertical(newPosition)) {

                this.position = new Vector2d(Math.abs(position.getX()-rightEdge), position.getY());
            }
            else this.position = newPosition;
        }
        this.energy-=1;
        this.dayOfCycle=(dayOfCycle+1)%genotype.length;
    }

    public int[] getGenotype(){
        return this.genotype;
    }

    public int getEnergy(){
        return this.energy;
    }

    public void consume(int calory){
        this.energy+=calory;
        this.grassConsumed++;
    }

    public void giveBirth(int childCost, Animal baby){
        this.energy-=childCost;
        this.babies.add(baby);
    }

    public void unlive(int dayOfSimulation){
        this.dayOfDeath=dayOfSimulation;
    }

    //to działa, prawda?
    public int countDescendants(){
        int babiesCount=this.babies.size();

        for (Animal baby : babies){
            babiesCount+=baby.countDescendants();
        }
        return babiesCount;
    }

}
