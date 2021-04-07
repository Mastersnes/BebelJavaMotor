package com.bebel.api.elements.complex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.bebel.api.elements.basique.AnimableElement;
import com.bebel.api.resources.animations.AnimationTemplate;
import com.bebel.api.resources.assets.PhysicsAsset;
import com.bebel.api.utils.BebelBodyDef;
import com.bebel.api.utils.Direction;
import pythagoras.i.Point;

import java.util.ArrayList;
import java.util.List;

public class Personnage extends AnimableElement {
    protected static final int MARGE_OBJECTIF = 2;

    protected final Point lastDirection = new Point();
    protected final Point currentDirection = new Point();
    protected float speed;

    protected List<Vector2> objectifs = new ArrayList<>();
    protected Vector2 currentObjectif;

    public Personnage(String name) {super(name);}
    public Personnage(String name, final PhysicsAsset physics) {super(name, physics);}

    public float speed() {return speed;}
    public Personnage speed(final float speed) {
        this.speed = speed;
        return this;
    }

    public void stopMove() {objectifs.clear(); currentObjectif = null;}
    public void goTo(float x, float y) {
        objectifs.add(new Vector2(x, y));
    }

    /**
     * ANIMABLE
     */
    public Personnage up(final AnimationTemplate animation, final AnimationTemplate idle) {return up(animation, idle, null, null);}
    public Personnage up(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {return up(animation, idle, new BebelBodyDef(bodyName, bodyType));}
    public Personnage up(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_UP, animation, idle, bodyDef);
    }
    public Personnage upleft(final AnimationTemplate animation, final AnimationTemplate idle) {return upleft(animation, idle, null, null);}
    public Personnage upleft(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {return upleft(animation, idle, new BebelBodyDef(bodyName, bodyType));}
    public Personnage upleft(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_UP + Direction.D_LEFT, animation, idle, bodyDef);
    }
    public Personnage upright(final AnimationTemplate animation, final AnimationTemplate idle) {return upright(animation, idle, null, null);}
    public Personnage upright(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {return upright(animation, idle, new BebelBodyDef(bodyName, bodyType));}
    public Personnage upright(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_UP + Direction.D_RIGHT, animation, idle, bodyDef);
    }

    public Personnage down(final AnimationTemplate animation, final AnimationTemplate idle) {return down(animation, idle, null, null);}
    public Personnage down(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {return down(animation, idle, new BebelBodyDef(bodyName, bodyType));}
    public Personnage down(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_DOWN, animation, idle, bodyDef);
    }
    public Personnage downleft(final AnimationTemplate animation, final AnimationTemplate idle) {return downleft(animation, idle, null, null);}
    public Personnage downleft(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {return downleft(animation, idle, new BebelBodyDef(bodyName, bodyType));}
    public Personnage downleft(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_DOWN + Direction.D_LEFT, animation, idle, bodyDef);
    }
    public Personnage downright(final AnimationTemplate animation, final AnimationTemplate idle) {return downright(animation, idle, null, null);}
    public Personnage downright(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {return downright(animation, idle, new BebelBodyDef(bodyName, bodyType));}
    public Personnage downright(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_DOWN + Direction.D_RIGHT, animation, idle, bodyDef);
    }

    public Personnage left(final AnimationTemplate animation, final AnimationTemplate idle) {return left(animation, idle, null, null);}
    public Personnage left(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {return left(animation, idle, new BebelBodyDef(bodyName, bodyType));}
    public Personnage left(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_LEFT, animation, idle, bodyDef);
    }

    public Personnage right(final AnimationTemplate animation, final AnimationTemplate idle) {return right(animation, idle, null, null);}
    public Personnage right(final AnimationTemplate animation, final AnimationTemplate idle, final String bodyName, final BodyDef.BodyType bodyType) {return right(animation, idle, new BebelBodyDef(bodyName, bodyType));}
    public Personnage right(final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        return addAnim(Direction.D_RIGHT, animation, idle, bodyDef);
    }

    protected Personnage addAnim(final String direction, final AnimationTemplate animation, final AnimationTemplate idle, final BebelBodyDef bodyDef) {
        addAnim(direction, animation, bodyDef);
        if (idle != null) addAnim(direction + Direction.D_IDLE, idle, bodyDef);
        return this;
    }

    /**
     * DRAWABLE
     */
    @Override
    public boolean update(float delta) {
        checkObjectifs();
        checkDirection();

        if (body == null) move(currentDirection.x * speed, currentDirection.y * speed);
        else body.setLinearVelocity(currentDirection.x * speed, -currentDirection.y * speed);

        currentDirection.set(0, 0);
        return super.update(delta);
    }

    /**
     * Permet de rediriger le personnage en fonction des objectifs specifiés
     */
    private void checkObjectifs() {
        if (currentObjectif == null && !objectifs.isEmpty()) currentObjectif = objectifs.remove(objectifs.size()-1);
        if (currentObjectif != null) {
            currentDirection.x = (int) Math.signum(currentObjectif.x - x);
            currentDirection.y = (int) Math.signum(y - currentObjectif.y);

            if ((currentObjectif.x >= x - MARGE_OBJECTIF && currentObjectif.x <= (x + width + MARGE_OBJECTIF)) &&
                (currentObjectif.y >= y - MARGE_OBJECTIF && currentObjectif.y <= (y + height + MARGE_OBJECTIF))) {
                currentObjectif = null;
            }
        }
    }

    /**
     * Permet de calculer l'animation de direction en fonction de la direction prise par le personnage
     */
    private void checkDirection() {
        final Direction xIndicator, yIndicator;
        if (currentDirection.x == 0 && currentDirection.y == 0) {
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

            if (playIfExist(yIndicator.code() + xIndicator.code())) return;
            else if (playIfExist(yIndicator.code())) return;
            else if (playIfExist(xIndicator.code())) return;
            else {
                final StringBuilder errorMsg = new StringBuilder("Erreur, La direction n'existe pas : ");
                errorMsg.append("[").append(yIndicator.code() + xIndicator.code()).append("] ou ");
                errorMsg.append("[").append(yIndicator.code()).append("] ou ");
                errorMsg.append("[").append(xIndicator.code()).append("].");
                Gdx.app.error("Personnage", errorMsg.toString());
            }
        }
    }

    @Override
    public AnimableElement stop() {
        objectifs.clear(); currentObjectif = null;
        return super.stop();
    }


    public Personnage activeClavier() {
        input.whileKeyDown(k -> {
            if (k.contains(Input.Keys.Z)) currentDirection.y = -1;
            else if (k.contains(Input.Keys.S)) currentDirection.y = 1;
            if (k.contains(Input.Keys.Q)) currentDirection.x = -1;
            else if (k.contains(Input.Keys.D)) currentDirection.x = 1;

            lastDirection.set(currentDirection);
        });
        return this;
    }
}
