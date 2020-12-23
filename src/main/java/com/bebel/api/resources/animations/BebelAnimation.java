package com.bebel.api.resources.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.bebel.api.contrats.Updatable;
import org.apache.commons.lang3.StringUtils;
import react.Signal;
import react.Slot;

/**
 * Represente une instance d'animation
 */
public class BebelAnimation implements Pool.Poolable, Updatable {
    /**
     * Une animation vide pour eviter les null pointer
     */
    public static BebelAnimation EMPTY = new BebelAnimation("EMPTY_ANIM");

    protected String name;
    protected AnimationTemplate animation;
    protected boolean flipX, flipY;

    protected boolean finish;
    protected Signal<Void> onFinish;

    protected float elapsedTime;
    public boolean pause;

    public BebelAnimation() {
    }

    public BebelAnimation(final String name) {
        name(name);
    }

    public void animation(AnimationTemplate animation) {
        this.animation = animation;
    }

    public void name(String name) {
        this.name = name;
    }

    public void flipX(boolean flipX) {
        this.flipX = flipX;
    }

    public void flipY(boolean flipY) {
        this.flipY = flipY;
    }

    public String name() {
        return name;
    }

    /**
     * Met à jour l'animation.
     * Si celle ci est terminé, un evenement sera transmis
     * @param delta
     */
    @Override
    public boolean update(final float delta) {
        if (animation == null) return false;
        if (!pause) {
            elapsedTime += delta;
            if (onFinish != null && isFinish()) {
                onFinish.emit(null);
            }
        }
        return isFinish();
    }

    /**
     * Renvoi la texture courante de l'animation
     * @return
     */
    public TextureRegion get() {
        if (animation == null) return null;

        final TextureRegion region = animation.getKeyFrame(elapsedTime);
        boolean flipX = false, flipY = false;
        if (this.flipX != region.isFlipX()) flipX = true;
        if (this.flipY != region.isFlipY()) flipY = true;
        region.flip(flipX, flipY);
        return region;
    }

    public BebelAnimation pause() {
        this.pause = true;
        return this;
    }

    public BebelAnimation resume() {
        this.pause = false;
        return this;
    }

    public BebelAnimation stop() {
        pause();
        elapsedTime = 0;
        finish = false;
        return this;
    }

    public BebelAnimation restart() {
        stop();
        return resume();
    }

    public boolean isFinish() {
        if (animation == null) return false;
        else {
            switch (animation.getPlayMode()) {
                case NORMAL:
                case REVERSED:
                    return animation.isAnimationFinished(elapsedTime);
                default:
                    return false;
            }
        }
    }

    public BebelAnimation onFinish(final Runnable action) {
        if (onFinish == null) onFinish = Signal.create();
        onFinish.connect(new Slot<Void>() {
            @Override
            public void onEmit(final Void listener) {
                action.run();
            }
        });
        return this;
    }

    /**
     * Clone l'animation
     * @return
     */
    public BebelAnimation clone() {
        return clone(flipX, flipY);
    }
    public BebelAnimation clone(final boolean flipX) {
        return clone(flipX, flipY);
    }
    public BebelAnimation clone(final boolean flipX, final boolean flipY) {
        final BebelAnimation animation = Pools.obtain(BebelAnimation.class);
        animation.name(this.name);
        animation.animation(this.animation);
        animation.flipX(flipX);
        animation.flipY(flipY);
        return animation;
    }

    /**
     * Poolables
     */
    @Override
    public void reset() {
        animation = null;
        finish = false;
        elapsedTime = 0;
    }

    public static BebelAnimation obtain(final String name) {
        final BebelAnimation animation = Pools.obtain(BebelAnimation.class);
        animation.name(name);
        return animation;
    }

    public void free() {
        Pools.free(this);
    }
}
