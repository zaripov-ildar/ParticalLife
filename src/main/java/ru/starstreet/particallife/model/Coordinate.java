package ru.starstreet.particallife.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coordinate {
    private double x;
    private double y;

    public static double getDistance(Coordinate c1, Coordinate c2) {
        return Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2));
    }

    public void add(Coordinate coordinate) {
        x += coordinate.getX();
        y += coordinate.getY();
    }

    public void add(double dx, double dy) {
        x += dx;
        y += dy;
    }

}
