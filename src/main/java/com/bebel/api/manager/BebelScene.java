package com.bebel.api.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.bebel.api.elements.basique.predicats.AbstractElement;
import com.bebel.api.elements.basique.predicats.CollisionableElement;
import com.bebel.api.elements.basique.predicats.GroupElement;
import com.bebel.api.elements.basique.predicats.TransformableElement;

import static com.bebel.api.Global.*;

/**
 * Element representant une scene d'un jeu
 */
public abstract class BebelScene extends GroupElement {
    protected World world;
    protected Box2DDebugRenderer b2dr;
    protected Array<Body> bodies = new Array<>();
    protected float accumulator;

    protected float axeProfondeur, profondeur;


    public BebelScene(String name) {super(name);}

    public void goTo(final BebelScene scene) {
        SceneManager.getInstance().goTo(scene); }
    public void goTo() {
        SceneManager.getInstance().goTo(this); }

    /**
     * Permet d'activer la profondeur de la scene
     * Les elements sensibles à la profondeur (non fixe) seront alors redimensionnés en fonction de leur position verticale
     * @param profondeur etendu de la profondeur
     * @param axe axe de la profondeur en pourcentage de la hauteur de la scene
     */
    public void activeProfondeur(final float profondeur, final float axe) {
        this.profondeur = profondeur;
        this.axeProfondeur = axe;
    }

    /**
     * Permet d'activer la physique dans la scene.
     * @param gravityX
     * @param gravityY
     */
    public void activePhysics(final float gravityX, final float gravityY) {
        Box2D.init();
        world = new World(new Vector2(gravityX, gravityY), true);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                final CollisionableElement elementA = (CollisionableElement) contact.getFixtureA().getBody().getUserData();
                final CollisionableElement elementB = (CollisionableElement) contact.getFixtureB().getBody().getUserData();
                elementA.contactBegin(contact);
                elementB.contactBegin(contact);
            }

            @Override
            public void endContact(Contact contact) {
                final CollisionableElement elementA = (CollisionableElement) contact.getFixtureA().getBody().getUserData();
                final CollisionableElement elementB = (CollisionableElement) contact.getFixtureB().getBody().getUserData();
                elementA.contactEnd(contact);
                elementB.contactEnd(contact);
            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {}
            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {}
        });
        if (debugMode) b2dr = new Box2DDebugRenderer();
    }

    public World world() {return world;}

    /**
     * DISPLAYABLE
     */
    @Override
    protected void paintImpl(final SpriteBatch batch) {
        super.paintImpl(batch);
        if (b2dr != null) b2dr.render(world, screen.getCamera().combined);
    }

    @Override
    public boolean update(float delta) {
        super.update(delta);

        if (world != null) {
            accumulator += Math.min(delta, 0.25f);

            if (accumulator >= STEP_TIME) {
                accumulator -= STEP_TIME;
                world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            }

            world.getBodies(bodies);
            for (final Body body : bodies) {
                final CollisionableElement element = (CollisionableElement) body.getUserData();
                element.position(body.getPosition().x, body.getPosition().y, R_LEFT | R_DOWN);
                element.rotation(MathUtils.radiansToDegrees * body.getAngle());
            }
        }
        return true;
    }

    @Override
    protected void updateChild(AbstractElement child, float delta) {
        super.updateChild(child, delta);
        if (profondeur == 0) return;
        if (child instanceof TransformableElement) {
            final TransformableElement movable = (TransformableElement) child;
            movable.z(1 + (profondeur * ((axeProfondeur*height) - movable.y())));
        }
    }

    /**
     * GROUP
     */
    @Override
    public <ELEMENT extends AbstractElement> ELEMENT insert(int index, ELEMENT element) {
        final ELEMENT insertedElement = super.insert(index, element);
        if (insertedElement != null) insertedElement.setScene(this);
        return insertedElement;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (world != null) world.dispose();
        if (b2dr != null) b2dr.dispose();
    }
}
