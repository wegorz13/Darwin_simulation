package agh.ics.oop.model;

public class WaterReservoir {
    //proponuje prostokątne zbiorniki dla poprawy jakości snu
    private final Vector2d rightUpCorner;
    private final Vector2d leftDownCorner;
    private final Vector2d change = new Vector2d(1,1);
    private int tideCycle=0;

    public WaterReservoir(int width, int height,int numberOfReservoirs) {

        //poza mapą, trzeba zrobić losowanie rozmiarów
        this.rightUpCorner = new Vector2d(width,height);
        this.leftDownCorner = new Vector2d(2*width,2*height);
    }

    private void highTide(){
        this.rightUpCorner.add(change);
        this.leftDownCorner.subtract(change);
    }

    private void lowTide(){
        this.rightUpCorner.subtract(change);
        this.leftDownCorner.add(change);
    }

    public void updateSize(){
        //rośnie 2 razy, czeka 2 razy, maleje 2 razy
        if (tideCycle<=1){
            this.highTide();
        }
        else if (tideCycle>=4){
            this.lowTide();
        }
        tideCycle=(tideCycle+1)%6;
    }

    public Vector2d getRightUpCorner() {
        return rightUpCorner;
    }

    public Vector2d getLeftDownCorner() {
        return leftDownCorner;
    }
}
