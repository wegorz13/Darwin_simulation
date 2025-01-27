package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.*;

import static java.util.Collections.swap;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final int grassCount;
    private final List<Vector2d> permutationOfPositions;
    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount) {
        this.grassCount = grassCount;
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
            private final Random rand = new Random();

            @Override
            public boolean hasNext() {
                return generatedCount < grassCount;
            }

            @Override
            public Vector2d next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements to generate.");
                }

                int randomIndex = generatedCount + rand.nextInt(permutationOfPositions.size() - generatedCount);
                swap(permutationOfPositions, generatedCount, randomIndex);
                Vector2d position = permutationOfPositions.get(generatedCount);
                generatedCount++;
                return position;
            }
        };
    }
}