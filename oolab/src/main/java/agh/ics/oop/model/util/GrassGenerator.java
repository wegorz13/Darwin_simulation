package agh.ics.oop.model.util;

import agh.ics.oop.model.Grass;
import agh.ics.oop.model.Vector2d;

import java.util.*;

import static java.util.Collections.swap;

public class GrassGenerator implements Iterable<Grass> {
    private final List<Grass> equator = new ArrayList<Grass>();
    private final List<Grass> steppes = new ArrayList<Grass>();
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

    @Override
    public Iterator<Grass> iterator() {
        return new Iterator<Grass>() {
            private final Random rand = new Random();

            @Override
            public boolean hasNext() {
                return !(equator.isEmpty() && steppes.isEmpty());
            }

            @Override
            public Grass next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements to generate.");
                }

                Grass grass = null;
                if (equator.isEmpty()) {
                    int randomIndex = rand.nextInt(steppes.size());
                    swap(steppes, randomIndex, steppes.size() - 1);
                    grass = steppes.getLast();
                    steppes.removeLast();
                } else if (steppes.isEmpty()) {
                    int randomIndex = rand.nextInt(equator.size());
                    swap(equator, randomIndex, equator.size() - 1);
                    grass = equator.getLast();
                    equator.removeLast();
                } else {
                    int randomChoice = rand.nextInt(100);
                    if (randomChoice < 80) {
                        int randomIndex = rand.nextInt(equator.size());
                        swap(equator, randomIndex, equator.size() - 1);
                        grass = equator.getLast();
                        equator.removeLast();
                    } else {
                        int randomIndex = rand.nextInt(steppes.size());
                        swap(steppes, randomIndex, steppes.size() - 1);
                        grass = steppes.getLast();
                        steppes.removeLast();
                    }
                }
                return grass;
            }
        };
    }
}
