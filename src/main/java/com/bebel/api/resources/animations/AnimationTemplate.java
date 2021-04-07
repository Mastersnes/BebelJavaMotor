package com.bebel.api.resources.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Represente un template d'animation
 */
public class AnimationTemplate extends Animation<TextureRegion> {
    protected boolean defaultFlipX, defaultFlipY;

    public AnimationTemplate(float frameDuration, Array<? extends TextureRegion> keyFrames) {super(frameDuration, keyFrames);}
    public AnimationTemplate(float frameDuration, Array<? extends TextureRegion> keyFrames, PlayMode playMode) {super(frameDuration, keyFrames, playMode);}
    public AnimationTemplate(TextureRegion firstFrame) {this(1f, firstFrame);}
    public AnimationTemplate(float frameDuration, TextureRegion... keyFrames) {
        super(frameDuration, keyFrames);
    }

    public AnimationTemplate flip(boolean flipX, boolean flipY) {
        if (flipX) this.defaultFlipX = !this.defaultFlipX;
        if (flipY) this.defaultFlipY = !this.defaultFlipY;
        return this;
    }
    public boolean isFlipX() {return defaultFlipX;}
    public AnimationTemplate flipX() {return flip(true, false);}

    public boolean isFlipY() {return defaultFlipY;}
    public AnimationTemplate flipY() {return flip(true, false);}

    /**
     * Instancie l'animation
     */
    public BebelAnimation instance() {return instance(null);}
    public BebelAnimation instance(final String name) {
        return instance(name, defaultFlipX, defaultFlipY);
    }
    public BebelAnimation instance(final String name, final boolean flipX) {
        return instance(name, flipX, defaultFlipY);
    }
    public BebelAnimation instance(final String name, final boolean flipX, final boolean flipY) {
        final BebelAnimation animation = BebelAnimation.obtain(name);
        animation.animation(this);
        animation.flip(flipX, flipY);
        return animation;
    }
}
