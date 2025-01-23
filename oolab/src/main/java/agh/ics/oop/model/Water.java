package agh.ics.oop.model;

public class Water implements WorldElement {
    //troche bez sensu klasa, ale nie ma tez sensu tworzyć mapy trawy a visualiser potrzebuje obiektu

    @Override
    public String toString() {
        return "~";
    }

    @Override
    public Vector2d getPosition() {
        return null;
    }
}
