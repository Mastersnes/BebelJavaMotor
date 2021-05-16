package com.bebel.api.elements.basique;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.bebel.api.elements.basique.predicats.CollisionableElement;
import com.bebel.api.elements.basique.predicats.DrawableElement;
import com.bebel.api.resources.animations.AnimationTemplate;
import com.bebel.api.resources.animations.BebelAnimation;
import com.bebel.api.resources.assets.PhysicsAsset;
import com.bebel.api.utils.BebelBodyDef;
import org.apache.commons.lang3.StringUtils;
import react.RMap;

/**
 * Represente un element animable
 */
public class AnimableElement extends CollisionableElement {
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

    protected RMap<String, BebelBodyDef> bodies = RMap.create();

    public AnimableElement(final String name) {super(name, null, null);}
    public AnimableElement(final String name, final PhysicsAsset physics) {super(name, null, physics);}

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
    public void addAnim(final String name, final BebelAnimation animation) {addAnim(name, animation, null, null);}
    public void addAnim(final String name, final BebelAnimation animation, final String bodyName, final BodyDef.BodyType bodyType) {
        if (StringUtils.isEmpty(bodyName) || bodyType == null)
            addAnim(name, animation, null);
        else addAnim(name, animation, new BebelBodyDef(bodyName, bodyType));
    }
    public void addAnim(final String name, final BebelAnimation animation, final BebelBodyDef body) {
        animation.name(name); animations.put(name, animation);
        if (body != null) bodies.put(name, body);
    }
    public void addAnim(final String name, final AnimationTemplate animation) {addAnim(name, animation, null);}
    public void addAnim(final String name, final AnimationTemplate animation, final String bodyName, final BodyDef.BodyType bodyType) {
        if (StringUtils.isEmpty(bodyName) || bodyType == null)
            addAnim(name, animation, null);
        else addAnim(name, animation, new BebelBodyDef(bodyName, bodyType));
    }
    public void addAnim(final String name, final AnimationTemplate animation, final BebelBodyDef body) {
        addAnim(name, animation.instance(name), body);
    }
    public void cloneAnim(final String src, final String cible) {
        cloneAnim(src, cible, false, false);
    }
    public void cloneAnim(final String src, final String cible, final boolean flipX, final boolean flipY) {
        addAnim(cible, getAnim(src).clone(flipX, flipY), bodies.get(src));
    }

    /**
     * Joue une animation.
     * Si l'animation n'a pas changé, on la continue simplement.
     */
    public boolean playIfExist(final String name) {
        if (animations.containsKey(name)) {
            return play(name) != null;
        }
        return false;
    }
    public BebelAnimation play(final String name) {
        if (currentAnim != null) {
            if (currentAnimIs(name)) return currentAnim.resume();
            else currentAnim.stop();
        }

        currentAnim = getAnim(name);
        currentAnim.restart();

        if (bodies.containsKey(name)) body(bodies.get(name));
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
    public DrawableElement flip(boolean x, boolean y) {
        super.flip(x, y);
        if (currentAnim != null) {
            currentAnim.flip(x, y);
        }
        return this;
    }

    @Override
    public boolean isFlipX() {return super.isFlipX() || (currentAnim != null && currentAnim.isFlipX());}
    @Override
    public boolean isFlipY() {return super.isFlipY() || (currentAnim != null && currentAnim.isFlipY());}

    @Override
    public void dispose() {animations.clear();}
}
