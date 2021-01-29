package com.bebel.api.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bebel.api.BebelScreen;
import com.bebel.api.elements.basique.EventableElement;
import com.bebel.api.elements.basique.GroupElement;
import com.bebel.api.events.keyboard.KeyInput;
import com.bebel.api.events.keyboard.KeyTypeInput;
import com.bebel.api.events.mouse.ClickWatcher;
import com.bebel.api.events.mouse.MouseInput;
import com.bebel.api.events.mouse.MouseInputType;
import react.Signal;
import react.SignalView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.badlogic.gdx.utils.Pools.obtain;
import static com.bebel.api.events.mouse.MouseInputType.*;

/**
 * Manager d'evenements
 */
public class BebelProcessor implements InputProcessor {
    protected final boolean bubble;
    protected final BebelScreen screen;

    protected final List<Integer> keysDown = new ArrayList<>();

    protected final KeyInput key = obtain(KeyInput.class);
    protected final KeyTypeInput keyTyped = obtain(KeyTypeInput.class);
    protected Signal<SimpleInput> whileKeyDown;
    protected Signal<KeyInput> onKeyDown, onKeyUp, onKeyClick;
    protected Signal<KeyTypeInput> onKeyType;
    protected final ClickWatcher keyClickWatcher = new ClickWatcher();

    protected final Vector2 scratch = new Vector2();
    protected final MouseInput mouse = obtain(MouseInput.class);
    protected final ClickWatcher clickWatcher = new ClickWatcher();

    protected EventableElement hoverLayer = null;

    /**
     * Demare le manager, si bubble est a true, les evenements seront partagés a travers les calques
     *
     * @param screen
     * @param bubble
     */
    public BebelProcessor(final BebelScreen screen, boolean bubble) {
        this.bubble = bubble;
        this.screen = screen;
    }

    /**
     * Permet de dispatcher les evenements souris aux calques
     * Accorde ou enleve le focus sur le calque cliqué
     * Detecte les clicks souris
     *
     * @param type
     */
    protected void dispatchMouse(final MouseInputType type) {
        mouse.set(keysDown);
        final GroupElement root = screen.getRoot();
        final EventableElement hitLayer = root.hitTest(this.scratch.set(mouse.x, mouse.y));
        if (hitLayer != null) {
            if (type == DOWN) {
                if (mouse.isLeft() && !screen.isFocus(hitLayer)) {
                    screen.focus().dispatchEvent(UNFOCUS, mouse, bubble);
                    screen.focus(hitLayer);
                    screen.focus().dispatchEvent(FOCUS, mouse, bubble);
                } else if (mouse.isRight()) {
                    screen.focus().dispatchEvent(UNFOCUS, mouse, bubble);
                    screen.focus(EventableElement.EMPTY);
                }
            }
            hitLayer.dispatchEvent(type, mouse, bubble);
        }

        if (hitLayer != hoverLayer) {
            if (hoverLayer != null) hoverLayer.dispatchEvent(EXIT, mouse, bubble);
            hoverLayer = hitLayer;
            if (hoverLayer != null) hoverLayer.dispatchEvent(ENTER, mouse, bubble);
        }

        mouse.clickNb = this.clickWatcher.checkClick(type, mouse, hitLayer);
        if (mouse.clickNb > 0 && hoverLayer != null)
            hoverLayer.dispatchEvent(CLICK, mouse, bubble);
    }

    public BebelProcessor whileKeyDown(final SignalView.Listener<SimpleInput> callback) {
        whileKeyDown = checkCreate(whileKeyDown);
        whileKeyDown.connect(callback);
        return this;
    }

    /**
     * Est appelé avant chaque render pour les evenements redondant
     */
    public void processLoop() {
        for (final Iterator<Integer> key = keysDown.iterator(); key.hasNext(); ) {
            if (!Gdx.input.isKeyPressed(key.next()))
                key.remove();
        }
        if (whileKeyDown != null && keysDown.size() > 0) {
            whileKeyDown.emit(key.set(keysDown));
        }

        if (hoverLayer != null) hoverLayer.dispatchEvent(HOVER, mouse, bubble);
    }

    public BebelProcessor onKeyDown(final SignalView.Listener<KeyInput> callback) {
        onKeyDown = checkCreate(onKeyDown);
        onKeyDown.connect(callback);
        return this;
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (!keysDown.contains(keyCode)) keysDown.add(keyCode);
        if (onKeyDown != null) onKeyDown.emit(key.set(keyCode, keysDown));

        key.clickNb = this.keyClickWatcher.checkClick(DOWN, key);
        return true;
    }

    public BebelProcessor onKeyClick(final SignalView.Listener<KeyInput> callback) {
        onKeyClick = checkCreate(onKeyClick);
        onKeyClick.connect(callback);
        return this;
    }

    public BebelProcessor onKeyUp(final SignalView.Listener<KeyInput> callback) {
        onKeyUp = checkCreate(onKeyUp);
        onKeyUp.connect(callback);
        return this;
    }

    @Override
    public boolean keyUp(int keyCode) {
        if (keysDown.contains(keyCode))
            keysDown.remove(keysDown.indexOf(keyCode));
        if (onKeyUp != null) onKeyUp.emit(key.set(keyCode, keysDown));

        key.clickNb = this.keyClickWatcher.checkClick(UP, key);
        if (onKeyClick != null && key.clickNb > 0) onKeyClick.emit(key);
        return true;
    }

    public BebelProcessor onKeyTyped(final SignalView.Listener<KeyTypeInput> callback) {
        onKeyType = checkCreate(onKeyType);
        onKeyType.connect(callback);
        return this;
    }

    @Override
    public boolean keyTyped(char c) {
        if (onKeyType != null) onKeyType.emit(keyTyped.set(c, keysDown));
        return true;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        mouse.position(x, y, screen.getViewport());
        mouse.pointer = pointer; mouse.button = button;
        dispatchMouse(DOWN);
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        mouse.position(x, y, screen.getViewport());
        mouse.pointer = pointer; mouse.button = button;
        dispatchMouse(UP);
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        mouse.position(x, y, screen.getViewport());
        mouse.pointer = pointer;
        dispatchMouse(DRAG);
        return true;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        mouse.position(x, y, screen.getViewport());
        dispatchMouse(MOVE);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        mouse.scroll = amount;
        dispatchMouse(SCROLL);
        return true;
    }

    /**
     * Permet de creer le signal s'il n'existe pas
     * @param signal
     * @return
     */
    protected Signal checkCreate(final Signal signal) {
        if (signal != null) return signal;
        else return Signal.create();
    }
}