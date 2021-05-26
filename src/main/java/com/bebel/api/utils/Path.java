package com.bebel.api.utils;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represente un chemin allant d'un repere vers un autre repere
 */
public class Path extends ArrayList<Jalon> {
    public Path(final Jalon firstJalon) {
        super(); add(firstJalon);
    }
    public float dst(final Vector2 origine) {
        return this.stream()
            .collect(Collectors.summingDouble(jalon -> jalon.dst(origine)))
            .floatValue();
    }
}
