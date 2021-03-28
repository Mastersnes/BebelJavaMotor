package com.bebel.api.actions.temporal;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.bebel.api.elements.basique.predicats.DrawableElement;

/**
 * Action de coloration
 */
public class ColorAction extends BindAction<DrawableElement> {
    protected Color startColor = new Color(), endColor = new Color();

    @Override
    protected void begin() {
        super.begin();
        startColor.set(target.tint());
    }

    @Override
    protected void actTime(final float percent) {
        target.tint().set(
                startColor.r + (endColor.r - startColor.r) * percent,
                startColor.g + (endColor.g - startColor.g) * percent,
                startColor.b + (endColor.b - startColor.b) * percent,
                startColor.a + (endColor.a - startColor.a) * percent
        );
    }

    public ColorAction to(final float r, final float g, final float b, final float a,
                      final float duration, final Interpolation interpolation) {
        endColor.set(r, g, b, a);
        return init(duration, interpolation);
    }
    public ColorAction to(final Color color, final float duration, final Interpolation interpolation) {
        return to(color.r, color.g, color.b, color.a, duration, interpolation);
    }

    public ColorAction to(final float r, final float g, final float b, final float duration, final Interpolation interpolation) {
        return to(r, g, b, target.alpha(), duration, interpolation);
    }

    public ColorAction to(final float a, final float duration, final Interpolation interpolation) {
        final Color targetColor = target.tint();
        return to(targetColor.r, targetColor.g, targetColor.b, a, duration, interpolation);
    }

    @Override
    public void reset() {
        super.reset();
        startColor.set(0, 0, 0, 0);
        endColor.set(startColor);
    }

    @Override
    public String type() {
        return "Color";
    }
}
