package ru.starstreet.particallife.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PetriDish implements iPetri {
    private final int INITIAL_PARTICLES_AMOUNT = 500;
    private boolean isUniformDistribution;
    private Sower sower;
    private int particlesAmount;
    private int typesAmount;
    private double scale;
    private List<Particle> particles;
    private List<ParticleType> types;

    private double VELOCITY;
    private double DISTANCE_MIN = 0.001;
    private double DISTANCE_MAX = 0.3;


    public PetriDish() {
        this.isUniformDistribution = false;
        this.particlesAmount = INITIAL_PARTICLES_AMOUNT;
        this.typesAmount = 1;
        this.sower = Sower.GRID;
        this.scale = 300;
        this.VELOCITY = 0.00001;
        generateTypes();
        generate();
    }

    @Override
    public List<Particle> getParticles() {
        return particles;
    }

    @Override
    public void calculateNext() {
        if (particles.isEmpty()) generate();
        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size(); j++) {
                Particle p1 = particles.get(i);
                Particle p2 = particles.get(j);
                double distance = getDistanceBetween(p1, p2);
                p1.interact(p2, DISTANCE_MIN, DISTANCE_MAX, distance);
                p2.interact(p1, DISTANCE_MIN, DISTANCE_MAX, distance);

            }
        }
        particles.forEach(p -> {
            update(p);
            pushToCenter(p.getCoordinate());
        });
    }

    private double getDistanceBetween(Particle p1, Particle p2) {
        return Coordinate.getDistance(p1.getCoordinate(), p2.getCoordinate());
    }

    public void update(Particle p) {
        double dx = p.getDelta().getX();
        double dy = p.getDelta().getY();
        double k = 1d;
        double length = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        if (length != 0) {
            k = VELOCITY / length;
        }
        p.getCoordinate().add(k * dx, k * dy);
        p.setDelta(new Coordinate(0, 0));

    }

    private void pushToCenter(Coordinate coordinate) {
        double x = coordinate.getX();
        double y = coordinate.getY();
        if (Math.abs(x) > 1) {
            coordinate.setX(jumpToOtherSide(x));

        }
        if (Math.abs(y) > 1) {
            coordinate.setY(jumpToOtherSide(y));
        }
    }

    private static double jumpToOtherSide(double coordinate) {
        return -Math.signum(coordinate) * (Math.abs(coordinate) - 0.005);
    }

    @Override
    public void setAmount(int amount) {
        this.particlesAmount = amount;
        generate();
    }


    @Override
    public void setTypeProperties(ParticleType type) {
        types.get(type.getId()).setAttraction(type.getAttraction());
        generate();
    }

    @Override
    public List<ParticleType> getParticleTypes() {
        return types;
    }

    @Override
    public void setUniformTypesDistribution(boolean isUniformDistribution) {
        this.isUniformDistribution = !isUniformDistribution;
        generate();
    }

    @Override
    public void setScale(double scale) {
        this.scale = scale;
    }


    @Override
    public void setSower(Sower sower) {
        this.sower = sower;
        generate();
    }


    @Override
    public void setTypesAmount(int typesAmount) {
        this.typesAmount = Math.min(Math.max(1, typesAmount), 5);
        generateTypes();
        generate();
    }

    @Override
    public int getTypesAmount() {
        return typesAmount;
    }

    private void generateTypes() {
        types = new ArrayList<>(typesAmount);
        for (int i = 0; i < typesAmount; i++) {
            types.add(new ParticleType(i, typesAmount));
        }
    }

    private void generate() {
        particles = sower.generate(particlesAmount, isUniformDistribution, types);
    }

    @Override
    public Color getColor(int id) {
        return types.get(id).getColor();
    }

    @Override
    public ParticleType getType(int id) {
        return types.get(id);
    }

    public double getX(int i) {

        return 0.5 * scale * (1 + particles.get(i).getCoordinate().getX());
    }

    public double getY(int i) {
        return 0.5 * scale * (1 + particles.get(i).getCoordinate().getY());
    }

    public Paint getParticleColor(int i) {
        return particles.get(i).getType().getColor();
    }

    public void setParticlesAmount(int particlesAmount) {
        this.particlesAmount = Math.max(particlesAmount, 50);
        generate();
        System.out.println(particles.size());
    }
}
