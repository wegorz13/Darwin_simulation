package agh.ics.oop.model.util;

import agh.ics.oop.model.Grass;
import agh.ics.oop.model.Vector2d;

import java.util.*;

import static java.util.Collections.swap;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final int grassCount;
    private final List<Vector2d> permutationOfPositions;
    private final Map<Vector2d,Grass> grassMap;

    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount, Map<Vector2d, Grass> grasses) {
        this.grassCount = grassCount;
        this.grassMap = grasses;

        this.permutationOfPositions = new ArrayList<>(maxWidth * maxHeight);
        for (int x = 0; x < maxWidth; x++) {
            for (int y = 0; y < maxHeight; y++) {
                this.permutationOfPositions.add(new Vector2d(x, y));
            }
        }
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new Iterator<Vector2d>() {
            private int generatedCount = 0;
            private int iterationCount = 0;
            private final Random rand = new Random();
            private final int limit = permutationOfPositions.size();

            //zmienione bo teraz codziennie ma być x traw, a może się trafić już zajęte pole

            @Override
            public boolean hasNext() {
                return generatedCount < grassCount && iterationCount < limit;
            }

            @Override
            public Vector2d next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements to generate.");
                }

                while (true) { // Continue until a valid position is found
                    int randomIndex = iterationCount + rand.nextInt(permutationOfPositions.size() - iterationCount);
                    swap(permutationOfPositions, iterationCount, randomIndex);
                    Vector2d position = permutationOfPositions.get(iterationCount);

                    iterationCount++;

                    if (!grassMap.containsKey(position)) { // Check dynamically if the position is occupied
                        generatedCount++;
                        return position;
                    }

                    // If the position is occupied, keep looking
                }
            }
        };
    }
}
