package com.bebel.api.resources.elements;

/**
 * Represente les informations d'un element
 */
public class ElementInfos {
    protected String name;
    protected Hitbox hitbox;

    public void name(final String name) {this.name = name;}
    public String name() {return name;}

    public void hitbox(final Hitbox hitbox) {this.hitbox = hitbox;}
    public Hitbox hitbox() {return hitbox;}
}