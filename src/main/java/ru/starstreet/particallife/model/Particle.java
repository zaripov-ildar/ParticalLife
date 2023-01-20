package ru.starstreet.particallife.model;

import lombok.Data;

@Data
public class Particle {


    private Coordinate coordinate;
    private ParticleType type;
    private Coordinate delta;


    public Particle(Coordinate coordinate, ParticleType type) {
        this.coordinate = coordinate;
        this.type = type;
        this.delta = new Coordinate(0, 0);
    }

    public void interact(Particle p, double DISTANCE_MIN, double DISTANCE_MAX) {
        double distance = Coordinate.getDistance(coordinate, p.coordinate);
        influence(this, p, distance, DISTANCE_MIN, DISTANCE_MAX);
        influence(p, this, distance, DISTANCE_MIN, DISTANCE_MAX);

    }

    private void influence(Particle p1, Particle p2, double distance, double DISTANCE_MIN, double DISTANCE_MAX) {
        double attractionCoefficient = getAttractionCoefficient(p1, p2, distance, DISTANCE_MIN, DISTANCE_MAX);
        double dx = p2.coordinate.getX() - p1.coordinate.getX();
        double dy = p2.coordinate.getY() - p1.coordinate.getY();
        delta.add(dx * attractionCoefficient + delta.getX(), dy * attractionCoefficient + delta.getY());
    }

    private double getAttractionCoefficient(Particle p1, Particle p2, double distance, double DISTANCE_MIN, double DISTANCE_MAX) {
        double baseAttraction = p1.getAttraction(p2);
        if (distance <= DISTANCE_MIN) return distance / DISTANCE_MIN - 1;
        if (distance < (DISTANCE_MAX - DISTANCE_MIN) / 2) return (distance - DISTANCE_MIN) * baseAttraction;
        if (distance < DISTANCE_MAX) return (DISTANCE_MIN - distance) * baseAttraction;
        return 0;
    }

    private double getAttraction(Particle p2) {
        return type.getAttraction(p2.type);
    }

    @Override
    public String toString() {
        return "Particle{" +
                "coordinate=" + coordinate +
                "}\n";
    }


}
