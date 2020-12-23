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
     * Retourne vrai si la touche est pressée
     * @param key
     * @return
     */
    public boolean is(final int key) {
        return this.key == key;
    }

    @Override
    public boolean are(int... keys) {
        for (int key : keys) {
            if (this.key != key && !this.keys.contains(key)) return false;
        }
        return true;
    }

    public boolean simpleClick() {return clickNb == 1;}
    public boolean doubleClick() {return clickNb == 2;}
    public boolean spamClick(final int min) {return clickNb >= min;}


    @Override
    public boolean areAtLeast(int... keys) {
        return Arrays.asList(keys).contains(key) || super.areAtLeast(keys);
    }

    @Override
    public void reset() {
        super.reset();
        key = clickNb = -1;
    }
}