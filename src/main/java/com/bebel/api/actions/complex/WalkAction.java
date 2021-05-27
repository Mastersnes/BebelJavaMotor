package com.bebel.api.actions.complex;

import com.badlogic.gdx.math.Vector2;
import com.bebel.api.actions.temporal.BindAction;
import com.bebel.api.elements.complex.Personnage;
import com.bebel.api.utils.BebelMathUtils;
import pythagoras.i.Point;

/**
 * Action de deplacement d'un personnage
 */
public class WalkAction extends BindAction<Personnage> {
    protected final int MARGE_ERREUR = 0;
    protected final Vector2 endPoint = new Vector2(), objectif = new Vector2();
    protected final Point direction = new Point();
    protected boolean isBy;

    @Override
    protected void begin() {
        super.begin();
        if (isBy) objectif.set(target.x() + endPoint.x, target.y() + endPoint.y);
        else objectif.set(endPoint);
        target.stopIdle();
    }

    @Override
    protected boolean act(float delta) {
        direction.set(Math.round(objectif.x - target.x()), Math.round(target.y() - objectif.y));
        if (BebelMathUtils.isBetween(direction.x, -MARGE_ERREUR, MARGE_ERREUR) &&
                BebelMathUtils.isBetween(direction.y, -MARGE_ERREUR, MARGE_ERREUR)) {
            target.goTo(0, 0);
            return true;
        }

        // Si on est sur 4 direction, on bouge le X puis le Y ensuite
        if (target.directions() <=4) {
            if (!BebelMathUtils.isBetween(direction.x, -MARGE_ERREUR, MARGE_ERREUR)) target.goTo(direction.x, 0);
            else target.goTo(0, direction.y);
        }else target.goTo(direction.x, direction.y);
        return false;
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
        endPoint.set(-1, -1);
    }

    @Override
    public String type() {
        return "Walk";
    }

    @Override
    public String toString() {
        return "WalkAction : (to:" + objectif + ")";
    }
}
