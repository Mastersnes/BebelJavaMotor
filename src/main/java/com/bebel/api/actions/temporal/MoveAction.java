package com.bebel.api.actions.temporal;

import com.badlogic.gdx.math.Interpolation;
import com.bebel.api.elements.basique.predicats.TransformableElement;
import pythagoras.f.Vector3;

import static com.bebel.api.elements.basique.predicats.MovableElement.*;

/**
 * Action de deplacement d'un point à un autre
 */
public class MoveAction extends BindAction<TransformableElement> {
    protected final Vector3 startPoint = new Vector3(), endPoint = new Vector3(), objectif = new Vector3();

    @Override
    protected void begin() {
        super.begin();
        startPoint.set(target.x(), target.y(), target.z());
        if (isBy) objectif.set(target.x() + endPoint.x, target.y() + endPoint.y, target.z() + endPoint.z);
        else objectif.set(endPoint);
    }

    @Override
    protected void actTime(final float percent) {
        target.position(
                startPoint.x + (objectif.x - startPoint.x) * percent,
                startPoint.y + (objectif.y - startPoint.y) * percent,
                startPoint.z + (objectif.z - startPoint.z) * percent, R_DOWN | R_LEFT);
    }

    public MoveAction to(final float ox, final float oy, final float oz, final int from, final float duration, final Interpolation interpolation) {
        float x = ox;
        float y = oy;
        float z = oz;

        if ((from & R_UP) != 0) {
            y = target.parentHeight() - target.height() - y;
        }
        if ((from & R_RIGHT) != 0) {
            x = target.parentWidth() - target.width() - x;
        }

        isBy = false;
        endPoint.set(x, y, z);
        return init(duration, interpolation);
    }
    public MoveAction to(final float x, final float y, final int from, final float duration, final Interpolation interpolation) {
        return to(x, y, target.z(), from, duration, interpolation);
    }
    public MoveAction to(final float x, final float y, final float z, final float duration, final Interpolation interpolation) {
        return to(x, y, z, R_UP | R_LEFT, duration, interpolation);
    }
    public MoveAction to(final float x, final float y, final float duration, final Interpolation interpolation) {
        return to(x, y, R_UP | R_LEFT, duration, interpolation);
    }
    public MoveAction bottomTo(final float x, final float y, final float z, final float duration, final Interpolation interpolation) {
        return to(x, y - target.height(), z, duration, interpolation);
    }
    public MoveAction bottomTo(final float x, final float y, final float duration, final Interpolation interpolation) {
        return to(x, y - target.height(), duration, interpolation);
    }

    public MoveAction by(final float ox, final float oy, final float z, final int from, final float duration, final Interpolation interpolation) {
        float x = ox, y = oy;

        if ((from & R_UP) != 0) y = -y;
        if ((from & R_RIGHT) != 0) x = -x;

        isBy = true;
        endPoint.set(x, y, z);
        return init(duration, interpolation);
    }
    public MoveAction by(final float x, final float y, final int from, final float duration, final Interpolation interpolation) {
        return by(x, y, 0, from, duration, interpolation);
    }
    public MoveAction by(final float x, final float y, final float z, final float duration, final Interpolation interpolation) {
        return by(x, y, z, R_UP | R_LEFT, duration, interpolation);
    }
    public MoveAction by(final float x, final float y, final float duration, final Interpolation interpolation) {
        return by(x, y, R_UP | R_LEFT, duration, interpolation);
    }

    @Override
    public void reset() {
        super.reset();
        startPoint.set(-1, -1, -1); endPoint.set(startPoint);
    }

    @Override
    public String type() {
        return "Move";
    }
}
