package com.bebel.api.actions.complex;

import com.badlogic.gdx.Gdx;
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
    protected final Vector2 startPoint = new Vector2(), endPoint = new Vector2(), objectif = new Vector2();
    protected final Point direction = new Point();
    protected boolean isBy;

    //TODO: Trouver pourquoi il y a deux temps en IDLE entre chaque etapes
    // -> L'action de marche se fait une fois dans le vent lorsqu'elle verifie qu'on est à 0,0. Elle devrait se terminer l'etape d'avant, il faudrait faire l'action puis verifier la faisabilité de la prochaine
    // -> Le bindAction à besoin que la target ne soit pas binder pour fonctionner. Il faudrait faire l'etape de liberation de la target avant le bindAction

    @Override
    protected void begin() {
        super.begin();
        startPoint.set(target.x(), target.y());
        if (isBy) objectif.set(target.x() + endPoint.x, target.y() + endPoint.y);
        else objectif.set(endPoint);
        Gdx.app.log("ETAPE", objectif.x + ", " + objectif.y);
    }

    @Override
    protected boolean act(float delta) {
        direction.set((int) (objectif.x - target.x()), (int) (target.y() - objectif.y));
        if (BebelMathUtils.isBetween(direction.x, -MARGE_ERREUR, MARGE_ERREUR) &&
                BebelMathUtils.isBetween(direction.y, -MARGE_ERREUR, MARGE_ERREUR)) {
            return true;
        }

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
        startPoint.set(-1, -1); endPoint.set(startPoint);
    }

    @Override
    public String type() {
        return "Walk";
    }
}
