package com.bebel.api.actions.temporal;

import com.badlogic.gdx.math.Interpolation;
import com.bebel.api.elements.basique.TransformableElement;
import pythagoras.f.Point;

/**
 * Action d'agrandissement d'une valeur à une autre
 */
public class ScaleAction extends BindAction<TransformableElement> {
    protected final Point startState = new Point(), endState = new Point(), objectif = new Point();

    @Override
    protected void begin() {
        super.begin();
        startState.set(target.scaleX(), target.scaleY());
        if (isBy) objectif.set(target.scaleX() + endState.x, target.scaleY() + endState.y);
    }

    @Override
    protected void actTime(final float percent) {
        target.scaleX(startState.x + (objectif.x - startState.x) * percent);
        target.scaleY(startState.y + (objectif.y - startState.y) * percent);
    }

    public ScaleAction to(final float x, final float y, final float duration, final Interpolation interpolation) {
        endState.set(x, y);
        return init(duration, interpolation);
    }
    public ScaleAction by(final float x, final float y, final float duration, final Interpolation interpolation) {
        isBy = true;
        return to(x, y, duration, interpolation);
    }

    @Override
    public void reset() {
        super.reset();
        startState.set(-1, -1); endState.set(startState);
    }

    @Override
    public String type() {
        return "Scale";
    }
}
