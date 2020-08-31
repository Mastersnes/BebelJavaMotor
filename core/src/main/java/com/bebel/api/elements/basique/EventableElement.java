package com.bebel.api.elements.basique;

import com.badlogic.gdx.math.Vector2;
import com.bebel.api.events.mouse.MouseInput;
import com.bebel.api.events.mouse.MouseInputType;
import react.Signal;
import react.SignalView;

import java.util.HashMap;
import java.util.Map;

/**
 * Represente un element animable et pouvant gerer des evenements
 */
public abstract class EventableElement extends AnimableElement {
    interface HitTester { EventableElement hitTest(EventableElement layer, Vector2 p);}

    public EventableElement(String name) {super(name);}

    @Override
    public final void create() {
        createImpl();
        makeEvents();
    }
    protected void createImpl() {}
    protected void makeEvents() {}

    /**
     * EVENTABLE
     */
    protected HitTester hitTester;
    protected final Map<MouseInputType, Signal<MouseInput>> events = new HashMap<>();

    public EventableElement setHitTester(HitTester tester) {
        hitTester = tester; return this;
    }
    public EventableElement hitTest(Vector2 p) {
        inverseTransform(p);
        return (hitTester == null) ? hitTestDefault(p) : hitTester.hitTest(this, p);
    }

    public EventableElement hitTestDefault(Vector2 p) {
        return (p.x >= maxX() && p.y >= maxY() &&
                p.x < minWidth(0) && p.y < minHeight(0)) ? this : null;
    }

    public EventableElement absorbHits() {
        return setHitTester(new HitTester() {
            public EventableElement hitTest(EventableElement layer, Vector2 p) {
                EventableElement hit = hitTestDefault(p);
                return (hit == null) ? EventableElement.this: hit;
            }

            @Override
            public String toString() {
                return "<all>";
            }
        });
    }

    public Signal<MouseInput> events(final MouseInputType type) {
        interactive(true);
        return events.computeIfAbsent(type, key -> Signal.create());
    }
    public boolean hasEventListeners() {
        for (final Signal event : events.values()) {
            if (event.hasConnections()) return true;
        } return false;
    }
    public EventableElement onMouseMove(final SignalView.Listener<MouseInput> slot) {
        events(MouseInputType.MOVE).connect(slot); return this;
    }
    public EventableElement onEnter(final SignalView.Listener<MouseInput> slot) {
        events(MouseInputType.ENTER).connect(slot); return this;
    }
    public EventableElement onExit(final SignalView.Listener<MouseInput> slot) {
        events(MouseInputType.EXIT).connect(slot); return this;
    }
    public EventableElement onHover(final SignalView.Listener<MouseInput> slot) {
        events(MouseInputType.HOVER).connect(slot); return this;
    }
    public EventableElement onTouchDown(final SignalView.Listener<MouseInput> slot) {
        events(MouseInputType.DOWN).connect(slot); return this;
    }
    public EventableElement onTouchUp(final SignalView.Listener<MouseInput> slot) {
        events(MouseInputType.UP).connect(slot); return this;
    }
    public EventableElement onClick(final SignalView.Listener<MouseInput> slot) {
        events(MouseInputType.CLICK).connect(slot); return this;
    }
    public EventableElement onScroll(final SignalView.Listener<MouseInput> slot) {
        events(MouseInputType.SCROLL).connect(slot); return this;
    }
    public EventableElement onDrag(final SignalView.Listener<MouseInput> slot) {
        events(MouseInputType.DRAG).connect(slot); return this;
    }
    public EventableElement onFocus(final SignalView.Listener<MouseInput> slot) {
        events(MouseInputType.FOCUS).connect(slot); return this;
    }
    public EventableElement onUnfocus(final SignalView.Listener<MouseInput> slot) {
        events(MouseInputType.UNFOCUS).connect(slot); return this;
    }

    /**
     * Redispatch les evenements de souris. Si le parametre bubble est à true, l'evenement sera transmis au pere
     *
     * @param mouse
     * @param bubble
     */
    public void dispatchEvent(final MouseInputType type, final MouseInput mouse, final boolean bubble) {
        if (mouse == null) return;
        events(type).emit(mouse);
        if (bubble && parent != null) {
            parent.dispatchEvent(type, mouse, true);
        }
    }

    public boolean isFocus() {
        return scene.isFocus(this);
    }
}
