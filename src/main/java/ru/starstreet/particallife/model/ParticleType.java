package ru.starstreet.particallife.model;

import javafx.scene.paint.Color;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Data
public class ParticleType {
    private int id;
    private Color color;
    private Map<Integer, Double> attraction;


    public ParticleType(int id, int typeAmount) {
        this.id = id;
        generateColor();
        generateAttraction(typeAmount);
    }

    public void generateColor() {
        Random rnd = new Random();
        float r = rnd.nextFloat();
        float g = rnd.nextFloat();
        float b = rnd.nextFloat();
        this.color = new Color(r, g, b, 1);
    }

    private void generateAttraction(int typeAmount) {
        this.attraction = new HashMap<>();
        for (int i = 0; i < typeAmount; i++) {
            attraction.put(i, 0d);
        }
    }

    public double getAttraction(ParticleType t2) {
        return attraction.get(t2.id);
    }
}
