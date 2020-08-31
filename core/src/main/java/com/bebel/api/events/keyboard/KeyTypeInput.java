package com.bebel.api.events.keyboard;

import com.bebel.api.events.SimpleInput;

import java.util.List;

/**
 * Evenement du clavier incluant une saisie de caractere
 */
public class KeyTypeInput extends SimpleInput {
    public char c;

    public KeyTypeInput() {}
    public KeyTypeInput(char c, final List<Integer> keys) {
        set(c, keys);
    }
    public KeyTypeInput set(char c, final List<Integer> keys) {
        super.set(keys);
        this.c = c;
        return this;
    }

    public char getC() {
        return c;
    }

    /**
     * Retourne vrai si le caractere tappé est c
     * @param c
     * @return
     */
    public boolean is(final char c) {
        return this.c == c;
    }

    @Override
    public void reset() {
        super.reset();
        c = (char) -1;
    }
}