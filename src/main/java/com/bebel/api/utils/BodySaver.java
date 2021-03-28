package com.bebel.api.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

/**
 * Outil permettant de sauvegarder les propriétés d'un Body
 */
public class BodySaver implements Pool.Poolable {
    protected BodyDef.BodyType type;
    protected float angularDamping;
    protected float angularVelocity;
    protected float linearDamping;
    protected final Vector2 linearVelocity = new Vector2();

    /**
     * Permet de sauvegarder les propriétés du body
     * @param body
     */
    public static BodySaver save(final Body body) {
        final BodySaver saver = Pools.obtain(BodySaver.class);
        saver.type = body.getType();
        saver.angularDamping = body.getAngularDamping();
        saver.angularVelocity = body.getAngularVelocity();
        saver.linearDamping = body.getLinearDamping();
        saver.linearVelocity.set(body.getLinearVelocity());
        return saver;
    }

    /**
     * Permet de restituer les propriétés du body
     * @param body
     */
    public void restitute(final Body body) {
        body.setType(type);
        body.setAngularDamping(angularDamping);
        body.setAngularVelocity(angularVelocity);
        body.setLinearDamping(linearDamping);
        body.setLinearVelocity(linearVelocity);
    }

    public void free() {Pools.free(this);}

    @Override
    public void reset() {
        type = null;
        angularDamping = 0; angularVelocity = 0;
        linearDamping = 0; linearVelocity.set(0,0);
    }

    public BodyDef.BodyType type() {return type;}
    public float angularDamping() {return angularDamping;}
    public float angularVelocity() {return angularVelocity;}
    public float linearDamping() {return linearDamping;}
    public Vector2 linearVelocity() {return linearVelocity;}
}
