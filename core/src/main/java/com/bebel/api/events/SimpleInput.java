package com.bebel.api.events;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Pool;
import com.bebel.api.elements.basique.EventableElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * Evenement de base
 */
public class SimpleInput implements Pool.Poolable {
    protected final List<Integer> keys = new ArrayList<>();
    protected EventableElement focus;

    public SimpleInput() {}
    public SimpleInput(List<Integer> keys) {
        set(keys);
    }

    public SimpleInput set(final List<Integer> keys) {
        this.keys.clear();
        this.keys.addAll(keys);
        return this;
    }
    public SimpleInput set(final EventableElement focus) {
        this.focus = focus;
        return this;
    }

    public EventableElement focus() {
        return this.focus;
    }
    public boolean focus(final EventableElement layer) {
        return this.focus == layer;
    }

    public List<Integer> keys() {
        return keys;
    }

    public boolean isAltDown () {return areAtLeast(ALT_LEFT, ALT_RIGHT);}
    public boolean isCtrlDown () { return areAtLeast(CONTROL_LEFT, CONTROL_RIGHT); }
    public boolean isShiftDown () { return areAtLeast(SHIFT_LEFT, SHIFT_RIGHT); }
    public boolean isMetaDown () { return areAtLeast(META_ALT_LEFT_ON, META_ALT_ON, META_ALT_RIGHT_ON, META_SHIFT_LEFT_ON, META_SHIFT_ON, META_SHIFT_RIGHT_ON, META_SYM_ON); }

    /**
     * Retourne vrai Si l'une de ces touches au moins est pressée
     * @param keys
     * @return
     */
    public boolean areAtLeast(final int... keys) {
        for (int key : keys) {
            if (this.keys.contains(key)) return true;
        }
        return false;
    }

    /**
     * Retourne vrai Si toutes ces touches sont pressées
     * @param keys
     * @return
     */
    public boolean are(final int... keys) {
        for (int key : keys) {
            if (!this.keys.contains(key)) return false;
        }
        return true;
    }

    /**
     * Retourne vrai Si toutes ces touches UNIQUEMENT sont pressées
     * @param keys
     * @return
     */
    public boolean areOnly(final int... keys) {
        if (keys.length != this.keys.size()) return false;
        for (int key : keys) {
            if (!this.keys.contains(key)) return false;
        }
        return true;
    }

    @Override
    public void reset() {
        keys.clear();
        focus = null;
    }
}