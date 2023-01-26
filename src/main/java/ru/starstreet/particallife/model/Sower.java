package ru.starstreet.particallife.model;

import java.util.ArrayList;
import java.util.List;

public enum Sower {
    GRID {
        @Override
        List<Particle> generate(int particlesAmount, boolean isUniformDistribution, List<ParticleType> types) {
            List<Particle> particles = new ArrayList<>(particlesAmount);
            int rectangles = getGridRectanglesAmount(particlesAmount);

            double recSize = (1 / Math.sqrt(rectangles));
            ParticleTypeIterator iterator = new ParticleTypeIterator(isUniformDistribution, types);
            for (double i = recSize / 2; i < 1; i += recSize) {
                for (double j = recSize / 2; j < 1; j += recSize) {
                    particles.add(new Particle(new Coordinate(2 * i - 1, 2 * j - 1), iterator.getNext()));
                    if (particles.size() == particlesAmount) {
                        return particles;
                    }
                }
            }
            throw new RuntimeException("there's not enough particles!!!");
        }
    };


    abstract List<Particle> generate(int newSize, boolean isUniformDistribution, List<ParticleType> types);

    protected int getGridRectanglesAmount(int particlesAmount) {
        int a = (int) Math.sqrt(particlesAmount);
        if (a * a != particlesAmount) {
            a++;
        }
        a *= a;
        return a;
    }
}
