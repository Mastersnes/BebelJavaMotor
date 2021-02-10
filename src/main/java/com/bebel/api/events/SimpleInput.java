package com.bebel.api.events;

import com.badlogic.gdx.utils.Pool;
import com.bebel.api.elements.basique.EventableElement;
import com.bebel.api.manager.CollectionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * Evenement de base
 * Pourrait etendre un CollectionManager afin de pouvoir comparer des collections selon plusieurs criteres
 */
public class SimpleInput extends CollectionManager<Integer> {
    protected EventableElement focus;

    public SimpleInput() {}
    public SimpleInput(List<Integer> keys) {set(keys);}

    public SimpleInput set(final EventableElement focus) {
        this.focus = focus; return this;
    }

    public EventableElement focus() {
        return this.focus;
    }
    public boolean focus(final EventableElement layer) {
        return this.focus == layer;
    }

    public boolean isAltDown () {return containsOneOf(ALT_LEFT, ALT_RIGHT);}
    public boolean isCtrlDown () { return containsOneOf(CONTROL_LEFT, CONTROL_RIGHT); }
    public boolean isShiftDown () { return containsOneOf(SHIFT_LEFT, SHIFT_RIGHT); }
    public boolean isMetaDown () { return containsOneOf(META_ALT_LEFT_ON, META_ALT_ON, META_ALT_RIGHT_ON, META_SHIFT_LEFT_ON, META_SHIFT_ON, META_SHIFT_RIGHT_ON, META_SYM_ON); }

    @Override
    public void reset() {
        super.reset();
        focus = null;
    }
}