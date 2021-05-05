package com.bebel.api.elements.basique.predicats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import com.bebel.api.Global;
import com.bebel.api.elements.complex.BebelScene;
import com.bebel.api.resources.assets.PhysicsAsset;
import com.bebel.api.resources.assets.TextureAsset;
import com.bebel.api.utils.BebelBodyDef;
import com.bebel.api.utils.BodySaver;
import com.codeandweb.physicseditor.PhysicsShapeCache;
import react.Signal;
import react.SignalView;

/**
 * Represente un element pouvant subir des collisions
 */
public class CollisionableElement extends DrawableElement {
    public CollisionableElement(final String name, final TextureRegion image, final PhysicsAsset physics) {
        super(name, image);
        if (physics != null) activePhysics(physics);
    }

    /**
     * BOX2D
     */
    protected BebelScene scene;
    protected Signal<Contact> onContactBegin;
    protected Signal<Contact> onContactEnd;
    protected PhysicsShapeCache physics;
    protected BebelBodyDef bodyDef;
    protected Body body;

    public BebelScene scene() {return scene;}
    public void setScene(BebelScene scene) {
        this.scene = scene;
    }

    public PhysicsShapeCache physics() {return physics;}
    public CollisionableElement activePhysics(final PhysicsAsset physics) {
        this.physics = physics.get();
        return this;
    }

    public Body body() {return body;}
    public Body body(final String bodyName, final BodyDef.BodyType bodyType) {
        return body(new BebelBodyDef(bodyName, bodyType));
    }
    public Body body(final BebelBodyDef bodyDef) {
        if (scene != null && scene.world() == null) {
            Gdx.app.error("Physics", "Erreur, vous devez d'abord activer la physique de la scene");
            Gdx.app.exit();
        }
        if (physics == null) {
            Gdx.app.error("Physics", "Erreur, vous devez d'abord activer la physique de l'element'");
            Gdx.app.exit();
        }
        this.bodyDef = bodyDef;
        final World world = scene.world();

        BodySaver saver = null;
        if (body() != null) {
            saver = BodySaver.save(body);
            world.destroyBody(body);
        }

        if (scaleX + scaleY != 2) scaleOrigin(R_DOWN | R_LEFT);
        body = physics.createBody(bodyDef.name(), world, Global.scale * scaleX, Global.scale * scaleY);
        body.setType(bodyDef.bodyType());
        body.setTransform(x(), y(), rotation);
        body.setUserData(this);

        if (saver != null) {
            saver.restitute(body); saver.free();
        }

        return body;
    }

    public void contactBegin(final Contact contactInfos) {if (onContactBegin != null) onContactBegin.emit(contactInfos);}
    public CollisionableElement onContactBegin(final SignalView.Listener<Contact> action) {
        if (onContactBegin == null) onContactBegin = Signal.create();
        onContactBegin.connect(action);
        return this;
    }

    public void contactEnd(final Contact contactInfos) {if (onContactEnd != null) onContactEnd.emit(contactInfos);}
    public CollisionableElement onContactEnd(final SignalView.Listener<Contact> action) {
        if (onContactEnd == null) onContactEnd = Signal.create();
        onContactEnd.connect(action);
        return this;
    }

    @Override
    protected void makeEvents() {
        super.makeEvents();
        onScaleChanged(oldScale -> {
            if (bodyDef != null) body(bodyDef);
        });
    }

    @Override
    public void dispose() {
        super.dispose();
        physics.dispose();
    }
}
