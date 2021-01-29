package com.bebel.api.elements.basique;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bebel.api.resources.animations.AnimationTemplate;
import com.bebel.api.resources.animations.BebelAnimation;
import org.apache.commons.lang3.StringUtils;
import react.RMap;

/**
 * Represente un element animable
 */
public class AnimableElement extends DrawableElement {
    protected BebelAnimation currentAnim;
    protected RMap<String, BebelAnimation> animations = RMap.create();
    {
        animations.connect(new RMap.Listener<String, BebelAnimation>() {
            @Override
            public void onRemove(String key, BebelAnimation oldValue) {
                oldValue.free();
            }
        });
    }

    public AnimableElement(final String name, final float w, final float h) {super(name, null, w, h);}

    public BebelAnimation getAnim(final String name) {
        if (!animations.containsKey(name)) {
            if (name != null)
                Gdx.app.error("Animation", "Erreur, le referentiel d'animation ne contient pas la clef :" + name);
            return BebelAnimation.EMPTY;
        }
        return animations.get(name);
    }
    public boolean currentAnimIs(final String name) {
        if (currentAnim == null) return name == null;
        else return StringUtils.equals(currentAnim.name(), name);
    }
    public void addAnim(final String name, final BebelAnimation animation) {
        animation.name(name); animations.put(name, animation);
    }
    public void addAnim(final String name, final AnimationTemplate animation) {
        addAnim(name, animation.instance(name));
    }
    public void cloneAnim(final String src, final String cible) {
        cloneAnim(src, cible, false, false);
    }
    public void cloneAnim(final String src, final String cible, final boolean flipX, final boolean flipY) {
        addAnim(cible, getAnim(src).clone(flipX, flipY));
    }

    /**
     * Joue une animation.
     * Si l'animation n'a pas changé, on la continue simplement.
     */
    public BebelAnimation play(final String name) {
        if (currentAnim != null) {
            if (currentAnimIs(name)) return currentAnim.resume();
            else currentAnim.stop();
        }

        currentAnim = getAnim(name);
        currentAnim.restart();
        return currentAnim;
    }

    @Override
    public AnimableElement pause() {
        super.pause();
        currentAnim.pause(); return this;
    }
    @Override
    public AnimableElement resume() {
        super.resume();
        currentAnim.resume(); return this;
    }
    @Override
    public AnimableElement stop() {
        super.stop();
        currentAnim.stop(); return this;
    }

    /**
     * DRAWABLE
     */
    protected void paintImpl(final SpriteBatch batch) {
        super.paintImpl(batch);
        draw(batch, currentAnim);
    }
    protected void draw(final SpriteBatch batch, final BebelAnimation animation) {
        if (animation != null) {
            draw(batch, animation.get());
        }
    }
    protected void draw(final SpriteBatch batch, final String animation) {
        draw(batch, getAnim(animation));
    }

    @Override
    public boolean update(final float delta) {
        super.update(delta);
        if (currentAnim != null) currentAnim.update(delta);
        return true;
    }

    @Override
    public void dispose() {animations.clear();}
}
