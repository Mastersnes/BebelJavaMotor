package com.bebel.api.actions.temporal;

import com.badlogic.gdx.math.Interpolation;
import com.bebel.api.actions.AutomatedAction;
import com.bebel.api.elements.basique.AbstractElement;

/**
 * Classe de base pour les actions lié à l'ecoulement du temps
 */
public abstract class TemporalAction<TARGET extends AbstractElement> extends AutomatedAction<TARGET> {
    protected boolean isBy;
    protected float duration, time;
    protected Interpolation interpolation;
    protected boolean reverse;

    @Override
    protected boolean act(final float delta) {
        time += delta;
        finish = time >= duration;
        float percent = 1f;
        if (!finish) {
            percent = time / duration;
            if (interpolation != null)
                percent = interpolation.apply(percent);
        }

        this.actTime(reverse ? 1.0F - percent : percent);
        return finish;
    }

    protected abstract void actTime(final float percent);

    public <ACTION extends TemporalAction> ACTION init(final float duration, final Interpolation interpolation) {
        this.duration = duration;
        this.interpolation = interpolation;
        return (ACTION) this;
    }

    public void reverse() {
        this.reverse = true;
    }

    public void restart() {
        this.time = 0.0F;
        super.restart();
    }

    /**
     * Poolable
     */
    public void reset() {
        super.reset();
        isBy = reverse = false;
        duration = time = 0;
        interpolation = null;
    }
}
