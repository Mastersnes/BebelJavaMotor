package com.bebel.api.elements.complex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.bebel.api.elements.basique.AnimableElement;
import com.bebel.api.elements.basique.predicats.MovableElement;
import com.bebel.api.resources.animations.AnimationTemplate;
import com.bebel.api.resources.assets.PhysicsAsset;
import com.bebel.api.utils.BebelBodyDef;
import com.bebel.api.utils.BebelMathUtils;
import com.bebel.api.utils.Direction;
import org.apache.commons.lang3.StringUtils;
import pythagoras.i.MathUtil;
import pythagoras.i.Point;

import java.util.ArrayList;
import java.util.List;

public class Personnage extends AnimableElement {
    protected final Point lastDirection = new Point();
    protected final Point currentDirection = new Point();
    protected float speed;

    public Personnage(String name) {super(name);}
    public Personnage(String name, final PhysicsAsset physics) {super(name, physics);}

    public float speed() {return speed;}
    public Personnage speed(final float speed) {
        this.speed = speed;
        return this;
    }

    public Personnage goTo(final int x, final int y) {
        currentDirection.x = MathUtil.clamp(x, -1, 1);
        currentDirection.y = MathUtil.clamp(y, -1, 1);
        return this;
    }

    protected boolean log = false;
    public void startLog() {log = true;}
    public void endLog() {log = false;}

    /**
     * Compte le nombre de direction dont dispose le personnage
     * @return
     */
    public int directions() {return animations.size() / 2;}

    /**
     * ANIMABLE
     */
    public Personnage up(final AnimationTemplate animation, final AnimationTemplate idle) {return up(animation, idle, null, null);}
    public Personnage up(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {
        return addAnim(Direction.D_UP, animation, idle, bodyName, bodyType);
    }
    public Personnage up(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_UP, animation, idle, bodyDef);
    }
    public Personnage upleft(final AnimationTemplate animation, final AnimationTemplate idle) {return upleft(animation, idle, null, null);}
    public Personnage upleft(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {
        return addAnim(Direction.D_UP + Direction.D_LEFT, animation, idle, bodyName, bodyType);
    }
    public Personnage upleft(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_UP + Direction.D_LEFT, animation, idle, bodyDef);
    }
    public Personnage upright(final AnimationTemplate animation, final AnimationTemplate idle) {return upright(animation, idle, null, null);}
    public Personnage upright(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {
        return addAnim(Direction.D_UP + Direction.D_RIGHT, animation, idle, bodyName, bodyType);
    }
    public Personnage upright(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_UP + Direction.D_RIGHT, animation, idle, bodyDef);
    }

    public Personnage down(final AnimationTemplate animation, final AnimationTemplate idle) {return down(animation, idle, null, null);}
    public Personnage down(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {
        return addAnim(Direction.D_DOWN, animation, idle, bodyName, bodyType);
    }
    public Personnage down(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_DOWN, animation, idle, bodyDef);
    }
    public Personnage downleft(final AnimationTemplate animation, final AnimationTemplate idle) {return downleft(animation, idle, null, null);}
    public Personnage downleft(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {
        return addAnim(Direction.DOWN + Direction.D_LEFT, animation, idle, bodyName, bodyType);
    }
    public Personnage downleft(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_DOWN + Direction.D_LEFT, animation, idle, bodyDef);
    }
    public Personnage downright(final AnimationTemplate animation, final AnimationTemplate idle) {return downright(animation, idle, null, null);}
    public Personnage downright(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {
        return addAnim(Direction.D_DOWN + Direction.D_RIGHT, animation, idle, bodyName, bodyType);
    }
    public Personnage downright(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_DOWN + Direction.D_RIGHT, animation, idle, bodyDef);
    }

    public Personnage left(final AnimationTemplate animation, final AnimationTemplate idle) {return left(animation, idle, null, null);}
    public Personnage left(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {
        return addAnim(Direction.D_LEFT, animation, idle, bodyName, bodyType);
    }
    public Personnage left(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_LEFT, animation, idle, bodyDef);
    }

    public Personnage right(final AnimationTemplate animation, final AnimationTemplate idle) {return right(animation, idle, null, null);}
    public Personnage right(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {
        return addAnim(Direction.D_RIGHT, animation, idle, bodyName, bodyType);
    }
    public Personnage right(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_RIGHT, animation, idle, bodyDef);
    }

    protected Personnage addAnim(final String direction, final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {
        if (StringUtils.isEmpty(bodyName) || bodyType == null)
            return addAnim(direction, animation, idle, null);
        else return addAnim(direction, animation, idle, new BebelBodyDef(bodyName, bodyType));
    }
    protected Personnage addAnim(final String direction, final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        addAnim(direction, animation, bodyDef);
        if (idle != null) addAnim(direction + Direction.D_IDLE, idle, bodyDef);
        return this;
    }

    /**
     * MOVABLE
     */
    @Override
    public MovableElement move(float ox, float oy, int from) {
        if (body == null) super.move(ox, oy, from);
        else body.setLinearVelocity(ox, -oy);
        return this;
    }

    /**
     * DRAWABLE
     */
    @Override
    public boolean update(float delta) {
        checkDirection();

        move(currentDirection.x * speed * delta, currentDirection.y * speed * delta);
        currentDirection.set(0, 0);
        return super.update(delta);
    }

    /**
     * Permet de calculer l'animation de direction en fonction de la direction prise par le personnage
     */
    private void checkDirection() {
        final Direction xIndicator, yIndicator;
        if (currentDirection.x == 0 && currentDirection.y == 0) {
            if (log) Gdx.app.log("IDLE", "IDLE");
            xIndicator = Direction.find(lastDirection.x, false);
            yIndicator = Direction.find(lastDirection.y, true);

            if (playIfExist(yIndicator.code() + xIndicator.code() + Direction.D_IDLE)) return;
            else if (playIfExist(yIndicator.code() + Direction.D_IDLE)) return;
            else if (playIfExist(xIndicator.code() + Direction.D_IDLE)) return;
            else if (playIfExist(Direction.D_DOWN + Direction.D_IDLE)) return;
            else {
                final StringBuilder errorMsg = new StringBuilder("Erreur, L'idle n'existe pas : ");
                errorMsg.append("[").append(yIndicator.code() + xIndicator.code()).append("] ou ");
                errorMsg.append("[").append(yIndicator.code()).append("] ou ");
                errorMsg.append("[").append(xIndicator.code()).append("].");
                Gdx.app.error("Personnage", errorMsg.toString());
            }
        } else {
            xIndicator = Direction.find(currentDirection.x, false);
            yIndicator = Direction.find(currentDirection.y, true);
            if (log) Gdx.app.log("WALK", xIndicator + " | "+ yIndicator);

            if (playIfExist(yIndicator.code() + xIndicator.code())) return;
            else if (playIfExist(xIndicator.code())) return;
            else if (playIfExist(yIndicator.code())) return;
            else {
                final StringBuilder errorMsg = new StringBuilder("Erreur, La direction n'existe pas : ");
                errorMsg.append("[").append(yIndicator.code() + xIndicator.code()).append("] ou ");
                errorMsg.append("[").append(yIndicator.code()).append("] ou ");
                errorMsg.append("[").append(xIndicator.code()).append("].");
                Gdx.app.error("Personnage", errorMsg.toString());
            }
        }
    }

    public Personnage activeClavier() {
        input().whileKeyDown(k -> {
            if (!binds.isEmpty()) return;
            if (k.contains(Input.Keys.Z)) currentDirection.y = -1;
            else if (k.contains(Input.Keys.S)) currentDirection.y = 1;
            if (k.contains(Input.Keys.Q)) currentDirection.x = -1;
            else if (k.contains(Input.Keys.D)) currentDirection.x = 1;

            lastDirection.set(currentDirection);
        });
        return this;
    }
}
