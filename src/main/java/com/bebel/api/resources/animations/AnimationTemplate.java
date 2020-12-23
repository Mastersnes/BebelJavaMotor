package com.bebel.api.resources.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Represente un template d'animation
 */
public class AnimationTemplate extends Animation {
    public AnimationTemplate(float frameDuration, Array<? extends TextureRegion> keyFrames) {
        super(frameDuration, keyFrames);
    }

    public AnimationTemplate(float frameDuration, Array<? extends TextureRegion> keyFrames, PlayMode playMode) {
        super(frameDuration, keyFrames, playMode);
    }

    public AnimationTemplate(float frameDuration, TextureRegion... keyFrames) {
        super(frameDuration, keyFrames);
    }

    /**
     * Instancie l'animation
     */
    public BebelAnimation instance() {return instance(null);}
    public BebelAnimation instance(final String name) {
        return instance(name, false, false);
    }
    public BebelAnimation instance(final String name, final boolean flipX) {
        return instance(name, flipX, false);
    }
    public BebelAnimation instance(final String name, final boolean flipX, final boolean flipY) {
        final BebelAnimation animation = BebelAnimation.obtain(name);
        animation.animation(this);
        animation.flipX(flipX);
        animation.flipY(flipY);
        return animation;
    }
}
