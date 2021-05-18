package com.bebel.api.actions.complex;

import com.badlogic.gdx.math.Vector2;
import com.bebel.api.actions.temporal.BindAction;
import com.bebel.api.elements.complex.Personnage;
import pythagoras.i.Point;

/**
 * Action de deplacement d'un personnage
 */
public class WalkAction extends BindAction<Personnage> {
    protected final Vector2 startPoint = new Vector2(), endPoint = new Vector2(), objectif = new Vector2();
    protected final Point direction = new Point();
    protected boolean isBy;

    @Override
    protected void begin() {
        super.begin();
        startPoint.set(target.x(), target.y());
        if (isBy) objectif.set(target.x() + endPoint.x, target.y() + endPoint.y);
        else objectif.set(endPoint);
    }

    @Override
    protected boolean act(float delta) {
        direction.set((int) (objectif.x - target.x()), (int) (target.y() - objectif.y));
        target.goTo(direction.x, direction.y);
        return direction.x == 0 && direction.y == 0;
    }

    public WalkAction to(final float ox, final float oy) {
        isBy = false;
        endPoint.set(ox, oy);
        return this;
    }

    public WalkAction by(final float ox, final float oy) {
        isBy = true;
        endPoint.set(ox, oy);
        return this;
    }

    @Override
    public void reset() {
        super.reset();
        startPoint.set(-1, -1); endPoint.set(startPoint);
    }

    @Override
    public String type() {
        return "Walk";
    }
}
