package agh.ics.oop.model;

public class WaterReservoir {
    //proponuje prostokątne zbiorniki dla poprawy jakości snu
    private Vector2d rightUpCorner;
    private Vector2d leftDownCorner;
    private final Vector2d change = new Vector2d(1,1);
    private int dayOfCycle=0;
    private final Runnable[] tideCycle = new Runnable[]{
            () -> {},
            () -> {},
            this::highTide,
            this::highTide,
            () -> {},
            () -> {},
            this::lowTide,
            this::lowTide};

    public WaterReservoir(int width, int height,int numberOfReservoirs) {

        //poza mapą, trzeba zrobić losowanie rozmiarów
        this.rightUpCorner = new Vector2d((width / 4), height / 4);
        this.leftDownCorner = new Vector2d(0, 0);
    }

    private void highTide(){
        this.rightUpCorner= this.rightUpCorner.add(change);
        this.leftDownCorner=this.leftDownCorner.subtract(change);
    }

    private void lowTide(){
        this.rightUpCorner=this.rightUpCorner.subtract(change);
        this.leftDownCorner=this.leftDownCorner.add(change);
    }

//    alternatywna wersja bez tablicy runnable
//    public void updateSize(){
//        // czeka 2 razy,rośnie 2 razy, czeka 2 razy, maleje 2 razy,
//        if (dayOfCycle==2 || dayOfCycle==3){
//            this.highTide();
//        }
//        else if (dayOfCycle==6 || dayOfCycle==7){
//            this.lowTide();
//        }
//        dayOfCycle=(dayOfCycle+1)%8;
//    }

    public void updateSize() {
        // czeka 2 razy,rośnie 2 razy, czeka 2 razy, maleje 2 razy,
        this.tideCycle[dayOfCycle].run();
        dayOfCycle = (dayOfCycle + 1) % 8;
    }


    public Vector2d getRightUpCorner() {
        return rightUpCorner;
    }

    public Vector2d getLeftDownCorner() {
        return leftDownCorner;
    }
}
