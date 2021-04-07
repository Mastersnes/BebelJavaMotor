package com.bebel.api.elements.basique.predicats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.bebel.api.BebelScreen;
import com.bebel.api.actions.temporal.BindAction;
import com.bebel.api.contrats.Updatable;
import com.bebel.api.events.BebelProcessor;
import com.bebel.api.shaders.AbstractShader;
import react.Closeable;
import react.Signal;
import react.Value;
import react.ValueView;

import java.util.HashMap;
import java.util.Map;

/**
 * Calque de base, coeur de l'architecture
 */
public abstract class AbstractElement implements Closeable, Disposable, Updatable {
    protected String name;
    protected GroupElement parent;
    protected AbstractShader shader;
    protected BebelScreen screen;
    protected BebelProcessor input;
    protected boolean debug, created;
    protected float delta;

    public AbstractElement(final String name) {
        this.name = name;
        visible(true);
    }

    public String name() {return name;}
    public void name(String name) {
        this.name = name;
    }

    public GroupElement parent() {return parent;}
    public void setParent(GroupElement parent) {this.parent = parent;}

    public BebelScreen screen() {return screen;}
    public void setScreen(BebelScreen screen) {
        this.screen = screen;
        if (screen != null) this.input = screen.input();
    }

    public AbstractElement remove() {
        if (parent != null) parent.remove(this);
        return this;
    }

    @Override
    public void dispose() {disposed();}

    /**
     * Envoi l'element plus pret dans la pile
     */
    public AbstractElement near() {
        if (parent != null) parent.near(this);
        return this;
    }
    public AbstractElement toNearest() {
        if (parent != null) parent.toNearest(this);
        return this;
    }

    /**
     * Envoi l'element plus loin dans la pile
     */
    public AbstractElement far() {
        if (parent != null) parent.far(this);
        return this;
    }
    public AbstractElement toFarthest() {
        if (parent != null) parent.toFarthest(this);
        return this;
    }

    @Override
    public void close() {
        if (parent != null) parent.remove(this);
        setState(State.DISPOSED);
    }
    @Override
    public String toString() {
        return name();
    }

    public void create() {created = true;}

    /**
     * DEBUG
     */
    public void debugMe() {debug = true;}
    public void stopDebug() {debug = false;}

    /**
     * OVERFLOW
     */
    public enum Overflow {VISIBLE, HIDDEN}
    protected Overflow overflow = Overflow.HIDDEN;

    public Overflow overflow() {
        return overflow;
    }
    public boolean isOverflow(final Overflow overflow) {
        if (parent == null) return overflow == Overflow.VISIBLE;
        else return parent.overflow() == overflow;
    }
    public AbstractElement setOverflow(Overflow overflow) {
        this.overflow = overflow;
        return this;
    }

    /**
     * States
     */
    public enum State {REMOVED, ADDED, DISPOSED}
    protected final ValueView<State> state = Value.create(State.REMOVED);

    public boolean isDisposed() {return state.get() == State.DISPOSED;}
    public boolean isAdded() {return state.get() == State.ADDED;}
    public boolean isRemoved() {return state.get() == State.REMOVED;}

    protected void setState(State state) {
        ((Value<State>) this.state).update(state);
    }

    protected AbstractElement onState(final State tgtState, final Signal.Listener<AbstractElement> action) {
        final AbstractElement that = this;
        state.connect(new Signal.Listener<State>() {
            public void onEmit(State state) {
                if (state == tgtState) action.onEmit(that);
            }
        });
        return that;
    }
    public AbstractElement onAdded(final Signal.Listener<AbstractElement> action) {
        return onState(State.ADDED, action);
    }
    public AbstractElement onRemoved(final Signal.Listener<AbstractElement> action) {
        return onState(State.REMOVED, action);
    }
    public AbstractElement onDisposed(final Signal.Listener<AbstractElement> action) {
        return onState(State.DISPOSED, action);
    }

    protected void added() {
        if (isDisposed()) throw new IllegalStateException("Illegal to use disposed layer: " + this);
        setState(State.ADDED);
    }
    protected void removed() {
        setState(State.REMOVED);
    }
    protected void disposed() {setState(State.DISPOSED);}

    /**
     * Flags
     */
    protected boolean visible, interactive;

    public boolean visible() {return visible;}
    public AbstractElement visible(boolean visible) { this.visible = visible; return this;}
    public AbstractElement show() {return visible(true);}
    public AbstractElement hide() {return visible(false);}

    public boolean interactive() {
        return interactive;
    }
    public AbstractElement interactive(boolean interactive) {
        if (interactive() != interactive) {
            // if we're being made interactive, active our setParent as well, if we have one
            if (interactive && parent != null) parent.interactive(interactive);
            this.interactive = interactive;
        }
        return this;
    }

    /**
     * SHADER
     */
    public void setShader(final AbstractShader shader) {
        this.shader = shader;
        shader.begin(this);
    }

    public AbstractShader getShader() {
        if (parent == null || shader != null) return shader;
        else return parent.getShader();
    }
    protected void processShader(final SpriteBatch batch) {
        final AbstractShader shader = getShader();
        if (shader != null) {
            batch.setShader(shader.shader());
            shader.refresh();
        }
    }

    /**
     * Permet de binder une action sur l'element si la place est libre
     * @param action
     * @return
     */
    protected Map<String, BindAction> binds = new HashMap<>();
    public boolean bindAction(final BindAction action) {
        if (!binds.containsKey(action.type())){
            binds.put(action.type(), action); return true;
        }
        return false;
    }

    public AbstractElement pause() {
        binds.values().stream().forEach(a -> a.pause());
        return this;
    }
    public AbstractElement resume() {
        binds.values().stream().forEach(a -> a.resume());
        return this;
    }
    public AbstractElement stop() {
        binds.values().removeIf(a -> {
            a.stop(); return true;
        });
        return this;
    }

    @Override
    public boolean update(float delta) {
        this.delta = Gdx.graphics.getDeltaTime();
        binds.values().removeIf(a -> a.update(delta));
        return true;
    }
}