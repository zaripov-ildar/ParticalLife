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
            for (double i = recSize / 2; i < 1-recSize/2; i += recSize) {
                for (double j = recSize / 2; j < 1 - recSize && particles.size() < particlesAmount; j += recSize) {
                    particles.add(new Particle(new Coordinate(2 * i - 1, 2 * j - 1), iterator.getNext()));
                }
            }

            return particles;
        }

        private int getGridRectanglesAmount(int particlesSize) {
            double a = Math.sqrt(particlesSize);
            a += (int) a != particlesSize ? 1 : 0;
            a *= a;
            return (int) a;
        }
    };


    abstract List<Particle> generate(int newSize, boolean isUniformDistribution, List<ParticleType> types);
}
