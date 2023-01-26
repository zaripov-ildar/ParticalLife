package ru.starstreet.particallife.model;

import java.util.List;

public class ParticleTypeIterator {
    private final boolean isUniformDistribution;
    private final List<ParticleType> types;
    private int count;

    public ParticleTypeIterator(boolean isUniformDistribution, List<ParticleType> types) {
        this.isUniformDistribution = isUniformDistribution;
        this.types = types;
        this.count = 0;
    }

    public ParticleType getNext() {
        int index = isUniformDistribution ? count++ % types.size() : (int) (Math.random() * types.size());
        return types.get(index);

    }
}
