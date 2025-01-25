package agh.ics.oop.model.util;

import agh.ics.oop.model.Grass;
import agh.ics.oop.model.Vector2d;

import java.util.*;

import static java.util.Collections.swap;

public class GrassGenerator implements Iterable<Grass> {
    private final List<Grass> equator = new ArrayList<Grass>();
    private final List<Grass> steppes = new ArrayList<Grass>();
    private final static Random rand = new Random();
    private final int height;

    public GrassGenerator(int width, int height) {
        this.height = height;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((int)Math.ceil(0.4 * height) <= y + 1 && y + 1 <= (int)Math.floor(0.6 * height)) {
                    equator.add(new Grass(new Vector2d(x, y)));
                } else {
                    steppes.add(new Grass(new Vector2d(x, y)));
                }
            }
        }
    }

    public void addEatenGrassBack(Grass grass) {
        if ((int)Math.ceil(0.4 * height) <= grass.getPosition().getY() + 1 && grass.getPosition().getY() + 1 <= (int)Math.floor(0.6 * height)) {
            equator.add(grass);
        } else {
            steppes.add(grass);
        }
    }

    private Grass getGrass(List<Grass> grassList, int index) {
        swap(grassList, index, grassList.size() - 1);
        Grass grass = grassList.getLast();
        grassList.removeLast();
        return grass;
    }

    @Override
    public Iterator<Grass> iterator() {
        return new Iterator<Grass>() {

            @Override
            public boolean hasNext() {
                return !(equator.isEmpty() && steppes.isEmpty());
            }

            @Override
            public Grass next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements to generate.");
                }

                Grass grass;
                if (equator.isEmpty()) {
                    grass = getGrass(steppes, rand.nextInt(steppes.size()));
                } else if (steppes.isEmpty()) {
                    grass = getGrass(equator, rand.nextInt(equator.size()));
                } else {
                    int randomChoice = rand.nextInt(100);
                    grass = randomChoice < 80 ? getGrass(equator, rand.nextInt(equator.size())) : getGrass(steppes, rand.nextInt(steppes.size()));
                }
                return grass;
            }
        };
    }
}
