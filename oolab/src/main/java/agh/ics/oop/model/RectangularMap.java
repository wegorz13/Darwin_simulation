package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class RectangularMap extends AbstractWorldMap {
    public RectangularMap(int width, int height) {
        this.leftDownCorner = new Vector2d(0, 0);
        this.rightUpCorner = new Vector2d(width - 1, height - 1);
        this.drawCorners = new Pair<>(this.leftDownCorner, this.rightUpCorner);
    }
}