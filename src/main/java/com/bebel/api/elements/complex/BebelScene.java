package com.bebel.api.elements.complex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.bebel.api.BebelScreen;
import com.bebel.api.Global;
import com.bebel.api.elements.basique.predicats.*;
import com.bebel.api.events.BebelProcessor;
import com.bebel.api.utils.Jalon;

import java.util.Comparator;
import java.util.NoSuchElementException;

import static com.bebel.api.Global.*;

/**
 * Element representant une scene d'un jeu
 */
public abstract class BebelScene extends GroupElement {
    protected final Comparator<AbstractElement> yComparator = (a, b) -> {
        if (!(a instanceof MovableElement && b instanceof MovableElement)) return 0;
        if (!(a.isDynamic() && b.isDynamic())) return 0;
        return (int) (((MovableElement) b).y() - ((MovableElement) a).y());
    };

    protected BebelScreen screen;
    protected BebelProcessor input;
    protected EventableElement focus = EventableElement.EMPTY;

    protected World world;
    protected Box2DDebugRenderer b2dr;
    protected Array<Body> bodies = new Array<>();
    protected float accumulator;

    protected float axeProfondeur, profondeur;
    protected Jalon rootJalon;

    protected boolean sortByY;

    public BebelScene(final String name) {
        super(name);
        input = new BebelProcessor(this, false);
    }

    public void setScreen(BebelScreen screen) {this.screen = screen;}
    public BebelScreen screen() {return screen;}

    /**
     * Permet de selectionner la scene
     */
    public void select() {Global.game.changeScene(this);}

    /**
     * Permet d'activer la profondeur de la scene
     * Les elements sensibles à la profondeur (non fixe) seront alors redimensionnés en fonction de leur position verticale
     * @param profondeur etendu de la profondeur
     * @param axe axe de la profondeur en pourcentage de la hauteur de la scene
     */
    public BebelScene activeProfondeur(final float profondeur, final float axe) {
        this.profondeur = profondeur;
        this.axeProfondeur = axe;
        return this;
    }

    /**
     * Permet d'activer la physique dans la scene.
     * @param gravityX
     * @param gravityY
     */
    public BebelScene activePhysics(final float gravityX, final float gravityY) {
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
        return this;
    }

    public World world() {return world;}

    /**
     * Permet d'activer le systeme de jalon à la scene
     */
    protected Jalon activeJalon(final float x, final float y) {
        this.rootJalon = new Jalon(x, y);
        return rootJalon;
    }

    /**
     * Permet de recuperer le repere le plus proche de l'origine indiquée
     * @param origine
     * @return
     */
    protected Jalon closestJalon(final Vector2 origine) {
        return rootJalon.listAll().stream()
                .min((jalon1, jalon2) -> (int) (jalon1.dst(origine) - jalon2.dst(origine)))
                .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Permet d'afficher les elements de facon triée sur le Y
     * @return
     */
    public BebelScene sortByY() {sortByY = true; return this;}

    /**
     * Permet de capter l'endroit où la souris a cliqué
     * (utile pour placer les jalons)
     * @return
     */
    public BebelScene debugMouse() {
        onClick(m -> {
            Gdx.app.log("CLICK", m.x() + ", " + m.y());
        });
        return this;
    }

    /**
     * DISPLAYABLE
     */
    @Override
    protected void paintImpl(final SpriteBatch batch) {
        super.paintImpl(batch);
        if (b2dr != null) b2dr.render(world, screen.getCamera().combined);
    }

    @Override
    protected void paintClip(SpriteBatch batch) {
        if (sortByY) children.sort(yComparator);
        super.paintClip(batch);
    }

    @Override
    public boolean update(float delta) {
        input.processLoop();
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
        if (insertedElement != null && insertedElement instanceof CollisionableElement)
            ((CollisionableElement) insertedElement).setScene(this);
        return insertedElement;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (world != null) world.dispose();
        if (b2dr != null) b2dr.dispose();
    }

    /**
     * EVENTABLE
     */
    public void unfocus() {this.focus = EventableElement.EMPTY;}
    public void unfocus(AbstractElement focus) {if (this.focus == focus) unfocus();}
    public void focus(EventableElement focus) {
        if (focus == this) unfocus();
        else this.focus = focus;
    }
    public EventableElement focus() {
        return this.focus;
    }
    public boolean isFocus(final EventableElement layer) {
        return this.focus == layer;
    }

    public BebelProcessor input() {
        return input;
    }
}
