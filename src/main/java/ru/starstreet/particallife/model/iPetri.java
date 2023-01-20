package ru.starstreet.particallife.model;

import javafx.scene.paint.Color;

import java.util.List;

public interface iPetri {
    List<Particle> getParticles();

    void calculateNext();

    void setAmount(int amount);
    void setTypesAmount(int typeSize);


    void setTypeProperties(ParticleType type);

    List<ParticleType> getParticleTypes();

    ParticleType getType(int id);

    void setUniformTypesDistribution(boolean isUniformDistribution);
    void setScale(double scale);
    void setSower(Sower sower);
    int getTypesAmount();
    Color getColor(int id);
}
