package agh.ics.oop.model;

public class WaterReservoir {
    //proponuje prostokątne zbiorniki dla poprawy jakości snu
    private Vector2d rightUpCorner;
    private Vector2d leftDownCorner;
    private final Vector2d change = new Vector2d(1,1);

    public WaterReservoir(Vector2d rightUpCorner, Vector2d leftDownCorner) {
        this.rightUpCorner = rightUpCorner;
        this.leftDownCorner = leftDownCorner;
    }

    public void highTide(){
        this.rightUpCorner.add(change);
        this.leftDownCorner.subtract(change);
    }

    public void lowTide(){
        this.rightUpCorner.subtract(change);
        this.leftDownCorner.add(change);
    }


    public Vector2d getRightUpCorner() {
        return rightUpCorner;
    }

    public Vector2d getLeftDownCorner() {
        return leftDownCorner;
    }
}
