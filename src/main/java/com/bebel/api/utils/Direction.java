package com.bebel.api.utils;

import com.badlogic.gdx.Input;

/**
 * Enumeration des differentes directions possibles
 */
public enum Direction {
    NULL("", 0, false),
    UP(-1, true),
    DOWN(1, true),
    LEFT(-1, false),
    RIGHT(1, false);

    protected final String code;
    protected final int sens;
    protected final boolean vertical;

    public static final String D_NULL = NULL.code;
    public static final String D_IDLE = "_IDLE";
    public static final String D_UP = UP.code;
    public static final String D_DOWN = DOWN.code;
    public static final String D_LEFT = LEFT.code;
    public static final String D_RIGHT = RIGHT.code;

    Direction(final int sens, final boolean vertical) {
        this(null, sens, vertical);
    }

    Direction(final String code, final int sens, final boolean vertical) {
        this.code = code != null ? code : name();
        this.sens = sens;
        this.vertical = vertical;
    }

    /**
     * Permet de retrouver une direction en fonction de son sens et de sa verticalité
     * @param sens
     * @param vertical
     * @return
     */
    public static Direction find(final int sens, final boolean vertical) {
        if (vertical) {
            if (sens > 0) return DOWN;
            else if (sens < 0) return UP;
        }else {
            if (sens > 0) return RIGHT;
            else if (sens < 0) return LEFT;
        }
        return NULL;
    }

    /**
     * Permet de retrouver une direction en fonction de la clef de clavier
     * @param key
     * @return
     */
    public static Direction byKey(final int key) {
        switch (key) {
            case Input.Keys.UP: case Input.Keys.Z: case Input.Keys.W:
                return UP;
            case Input.Keys.DOWN: case Input.Keys.S:
                return DOWN;
            case Input.Keys.LEFT: case Input.Keys.Q: case Input.Keys.A:
                return LEFT;
            case Input.Keys.RIGHT: case Input.Keys.D:
                return RIGHT;
            default: return NULL;
        }
    }

    public int sens() {
        return sens;
    }

    public String code() {
        return code;
    }

    public boolean vertical() {
        return vertical;
    }

    @Override
    public String toString() {
        return code;
    }}
