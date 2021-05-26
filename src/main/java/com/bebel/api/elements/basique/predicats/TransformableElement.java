package com.bebel.api.elements.basique.predicats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.bebel.api.Global;
import pythagoras.f.Point;
import react.Signal;
import react.SignalView;

/**
 * Represente un element pouvant être transformé
 */
public abstract class TransformableElement extends MovableElement {
    public static int CENTERX = (1 << 4), CENTERY = (1 << 5);

    public TransformableElement(String name) {super(name);}

    /**
     * DEBUG
     */
    @Override
    public void debugMe() {
        super.debugMe();
        input().whileKeyDown(k -> {
            // ROTATION
            if (k.contains(Input.Keys.R)) {
                if (k.contains(Input.Keys.LEFT)) rotate(-Global.scale);
                else if (k.contains(Input.Keys.RIGHT)) rotate(Global.scale);
            }
        });
        input().onKeyDown(k -> {
            if (k.is(Input.Keys.R) && k.doubleClick()) rotation(0);
            if (k.is(Input.Keys.COMMA))
                Gdx.app.log(name + "-ROTATION", ""+rotation);
        });
    }

    /**
     * Movable - Ajout du z
     * Indique que l'element est destiné à être deplacer dans l'espace
     */
    protected boolean zActive;

    public TransformableElement activeZ() {zActive = true; return this;}
    public TransformableElement unactiveZ() {zActive = false; return this;}
    public boolean isZActive() { return zActive;}
    public TransformableElement z(final float z) {
        if (!zActive) return this;
        return scale(z);
    }
    public float z() {return scaleX;}

    public TransformableElement position(final float x, final float y, final float z) { return position(x, y, z, R_UP | R_LEFT); }
    public TransformableElement position(final float ox, final float oy, final float oz, final int from) {
        position(ox, oy, from); return z(oz);
    }

    public TransformableElement moveZ(final float z) {return this.z(z() + z);}

    /**
     * Transformable
     */
    protected final Point oldScale = new Point();
    protected Signal<Point> onScaleChanged;
    protected Float oldRotation;
    protected Signal<Float> onRotationChanged;

    protected float scaleX = 1, scaleY = 1, rotation = 0;
    protected int rotateOrigin = CENTERX | CENTERY, scaleOrigin = CENTERX | CENTERY;
    protected Vector2 rotateVector = new Vector2(), scaleVector = new Vector2();

    public float rotation() {
        return rotation;
    }
    public TransformableElement rotation(final float rotation) {
        if (rotation == this.rotation) return this;
        oldRotation = this.rotation;
        this.rotation = rotation;
        updateOrigin(rotateOrigin, rotateVector);
        if (onRotationChanged != null) onRotationChanged.emit(oldRotation);
        return this;
    }

    public float scaleY() {
        return scaleY;
    }
    public TransformableElement scaleY(final float scale) {
        if (this.scaleY == scale) return this;
        oldScale.set(scaleX, scaleY);
        this.scaleY = scale;
        updateOrigin(scaleOrigin, scaleVector);
        if (onScaleChanged != null) onScaleChanged.emit(oldScale);
        return this;
    }

    public float scaleX() {return scaleX;}
    public TransformableElement scaleX(final float scale) {
        if (this.scaleX == scale) return this;
        oldScale.set(scaleX, scaleY);
        this.scaleX = scale;
        updateOrigin(scaleOrigin, scaleVector);
        if (onScaleChanged != null) onScaleChanged.emit(oldScale);
        return this;
    }

    public TransformableElement onScaleChanged(final SignalView.Listener<Point> action) {
        if (onScaleChanged == null) onScaleChanged = Signal.create();
        onScaleChanged.connect(action);
        return this;
    }
    public TransformableElement onRotationChanged(final SignalView.Listener<Float> action) {
        if (onRotationChanged == null) onRotationChanged = Signal.create();
        onRotationChanged.connect(action);
        return this;
    }


    public TransformableElement rotateOrigin(final int from) {
        if (this.rotateOrigin == from) return this;
        this.rotateOrigin = from;
        updateOrigin(rotateOrigin, rotateVector);
        return this;
    }
    public TransformableElement scaleOrigin(final int from) {
        if (this.scaleOrigin == from) return this;
        this.scaleOrigin = from;
        updateOrigin(scaleOrigin, scaleVector);
        return this;
    }

    public TransformableElement rotate(final float amount) {return rotation(rotation() + amount);}

    public TransformableElement grow(final float amount) {growX(amount); return growY(amount);}
    public TransformableElement growX(final float amount) {return scaleX(scaleX() + amount);}
    public TransformableElement growY(final float amount) {return scaleY(scaleY() + amount);}

    public TransformableElement scale(final float scale) {scaleX(scale); return scaleY(scale);}

    @Override
    protected TransformableElement updateOrigins() {
        updateOrigin(scaleOrigin, scaleVector);
        return updateOrigin(rotateOrigin, rotateVector);
    }
    protected TransformableElement updateOrigin(final int origin, final Vector2 vector) {return updateOrigin(origin, vector, true);}
    protected TransformableElement updateOrigin(final int origin, final Vector2 vector, final boolean withSize) {
        if (vector == null) return this;
        float w = width(), h = height();
        if (!withSize) {
            w = 1; h = 1;
        }

        vector.set(0, 0);
        if ((origin & CENTERX) != 0) vector.x = w / 2;
        else if ((origin & R_RIGHT) != 0) vector.x = w;

        if ((origin & CENTERY) != 0) vector.y = h / 2;
        else if ((origin & R_UP) != 0) vector.y = h;
        return this;
    }

    /**
     * Applique les transformations inverse au point en parametre
     *
     * @param coords
     * @return
     */
    public Vector2 inverseTransform(Vector2 coords) {
        coords.x -= x();
        coords.y -= y();

        coords.x -= scaleVector.x;
        coords.y -= scaleVector.y;
        coords.x /= scaleX;
        coords.y /= scaleY;
        coords.x += scaleVector.x;
        coords.y += scaleVector.y;

        coords.x -= rotateVector.x;
        coords.y -= rotateVector.y;
        coords.rotate(-rotation);
        coords.x += rotateVector.x;
        coords.y += rotateVector.y;

        return coords;
    }

    /**
     * Applique les transformations au point en parametre
     *
     * @param coords
     * @return
     */
    public Vector2 appliqueTransform(Vector2 coords) {
        coords.x += x();
        coords.y += y();

        coords.x += scaleVector.x;
        coords.y += scaleVector.y;
        coords.x *= scaleX;
        coords.y *= scaleY;
        coords.x -= scaleVector.x;
        coords.y -= scaleVector.y;

        coords.x += rotateVector.x;
        coords.y += rotateVector.y;
        coords.rotate(rotation);
        coords.x -= rotateVector.x;
        coords.y -= rotateVector.y;

        return coords;
    }
}
