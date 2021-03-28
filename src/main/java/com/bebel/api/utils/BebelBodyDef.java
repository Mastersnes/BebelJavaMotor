package com.bebel.api.utils;

import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Definition d'un body en vue de sa creation
 */
public class BebelBodyDef {
    protected String bodyName;
    protected BodyDef.BodyType bodyType;

    public BebelBodyDef(final String name, final BodyDef.BodyType type) {
        this.bodyName = name;
        this.bodyType = type;
    }

    public String name() {return bodyName;}
    public void name(String bodyName) {this.bodyName = bodyName;}

    public BodyDef.BodyType bodyType() {return bodyType;}
    public void bodyType(BodyDef.BodyType bodyType) {this.bodyType = bodyType;}
}
