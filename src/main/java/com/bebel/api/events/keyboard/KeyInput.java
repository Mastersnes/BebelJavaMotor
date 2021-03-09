package com.bebel.api.events.keyboard;

import com.bebel.api.events.SimpleInput;

import java.util.Arrays;
import java.util.List;

/**
 * Evenement de base du clavier
 */
public class KeyInput extends SimpleInput {
    public int key, clickNb;

    public KeyInput() {}
    public KeyInput(int key, final List<Integer> keys) {
        set(key, keys);
    }

    public KeyInput set(int key, final List<Integer> keys){
        super.set(keys);
        this.key = key;
        return this;
    }

    public int getKey() {
        return key;
    }

    public int clickNb() {
        return clickNb;
    }

    /**
     * Retourne vrai si la derniere touche pressée est "key"
     * @param key
     * @return
     */
    public boolean is(final int key) {
        return this.key == key;
    }

    public boolean simpleClick() {return clickNb == 0;}
    public boolean doubleClick() {return clickNb == 1;}

    public boolean spamClick(final int min) {return clickNb >= min-1;}


    @Override
    public void reset() {
        super.reset();
        key = clickNb = -1;
    }
}