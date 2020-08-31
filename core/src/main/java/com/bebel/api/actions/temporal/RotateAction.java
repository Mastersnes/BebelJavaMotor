package com.bebel.api.actions.temporal;

import com.badlogic.gdx.math.Interpolation;
import com.bebel.api.elements.basique.TransformableElement;

/**
 * Action de rotation d'un angle à un autre
 */
public class RotateAction extends BindAction<TransformableElement> {
    protected float startState = -1, endState = -1, objectif = -1;

    @Override
    protected void begin() {
        super.begin();
        startState = target.rotation();
        if (isBy) objectif = target.rotation() + endState;
    }

    @Override
    protected void actTime(final float percent) {
        target.rotation(startState + (objectif - startState) * percent);
    }

    public RotateAction to(final float amount, final float duration, final Interpolation interpolation) {
        endState = amount;
        return init(duration, interpolation);
    }
    public RotateAction by(final float amount, final float duration, final Interpolation interpolation) {
        isBy = true;
        return to(amount, duration, interpolation);
    }

    @Override
    public void reset() {
        super.reset();
        startState = endState = -1;
    }

    @Override
    public String type() {
        return "Rotate";
    }
}
