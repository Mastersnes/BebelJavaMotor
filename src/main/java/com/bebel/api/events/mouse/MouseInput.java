package com.bebel.api.events.mouse;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bebel.api.events.SimpleInput;

import static com.badlogic.gdx.utils.Pools.obtain;

/**
 * Evenement de base de la souris
 */
public class MouseInput extends SimpleInput {
    protected final Vector2 position = new Vector2();
    public int pointer, button, scroll, clickNb;

    public MouseInput(){}

    public Vector2 position() {return position;}
    public float x() {return position.x;}
    public float y() {
        return position.y;
    }

    public int pointer() {
        return pointer;
    }

    public int button() {
        return button;
    }

    public int scroll() {
        return scroll;
    }
    public int clickNb() {
        return clickNb;
    }

    public boolean isLeft() {
        return this.button == Input.Buttons.LEFT;
    }

    public boolean isMiddle() {
        return this.button == Input.Buttons.MIDDLE;
    }

    public boolean isRight() {
        return this.button == Input.Buttons.RIGHT;
    }

    public boolean simpleClick() {return clickNb == 1;}
    public boolean doubleClick() {return clickNb == 2;}
    public boolean spamClick(final int min) {return clickNb >= min;}

    @Override
    public void reset() {
        super.reset();
        position.x = position.y = pointer = button = scroll = clickNb = -1;
    }

    /**
     * Permet de renseigner la position de la sourie dans le viewport
     */
    public void position(final float x, final float y, final Viewport viewport) {
        position.set(x, y);
        viewport.unproject(position);
    }
}