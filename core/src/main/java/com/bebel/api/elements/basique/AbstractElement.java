package com.bebel.api.elements.basique;

import com.badlogic.gdx.utils.Disposable;
import com.bebel.api.BebelScene;
import com.bebel.api.actions.temporal.BindAction;
import com.bebel.api.contrats.Updatable;
import com.bebel.api.events.BebelProcessor;
import react.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Calque de base, coeur de l'architecture
 */
public abstract class AbstractElement implements Closeable, Disposable, Updatable {
    protected String name;
    protected GroupElement parent;
    protected BebelScene scene;
    protected BebelProcessor input;
    protected boolean debug, created;


    public AbstractElement(final String name) {
        this.name = name;
        setFlag(Flag.VISIBLE, true);
        onDisposed(new Slot<AbstractElement>() {
            @Override
            public void onEmit(AbstractElement layer) {
                layer.dispose();
            }
        });
    }

    public String name() {return name;}
    public void name(String name) {
        this.name = name;
    }

    public GroupElement parent() {return parent;}
    public void setParent(GroupElement parent) {this.parent = parent;}

    public BebelScene scene() {return scene;}
    public void setScene(BebelScene scene) {
        this.scene = scene;
        if (scene != null) this.input = scene.input();
    }

    public AbstractElement remove() {
        if (parent != null) parent.remove(this);
        return this;
    }

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

    public void create() {}

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

    protected void setState(State state) {
        ((Value<State>) this.state).update(state);
    }
    public boolean isDisposed() {
        return state.get() == State.DISPOSED;
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
        if (isDisposed()) throw new IllegalStateException("Illegal to use isDisposed layer: " + this);
        setState(State.ADDED);
    }
    protected void removed() {
        setState(State.REMOVED);
    }

    /**
     * Flags
     */
    enum Flag {
        VISIBLE(1 << 0), INTERACTIVE(1 << 1),
        XFDIRTY(1 << 2), ODIRTY(1 << 3);
        public final int bitmask;
        Flag(int bitmask) {
            this.bitmask = bitmask;
        }
    }
    protected int flags;

    protected boolean isSet(Flag flag) {
        return (flags & flag.bitmask) != 0;
    }
    protected AbstractElement setFlag(Flag flag, boolean active) {
        if (active) flags |= flag.bitmask;
        else flags &= ~flag.bitmask;
        return this;
    }

    public boolean visible() {return isSet(Flag.VISIBLE);}
    public AbstractElement visible(boolean visible) { return setFlag(Flag.VISIBLE, visible);}
    public AbstractElement show() {return visible(true);}
    public AbstractElement hide() {return visible(false);}

    public boolean interactive() {
        return isSet(Flag.INTERACTIVE);
    }
    public AbstractElement interactive(boolean interactive) {
        if (interactive() != interactive) {
            // if we're being made interactive, active our setParent as well, if we have one
            if (interactive && parent != null) parent.interactive(interactive);
            setFlag(Flag.INTERACTIVE, interactive);
        }
        return this;
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
        binds.values().removeIf(a -> a.update(delta));
        return true;
    }
}
