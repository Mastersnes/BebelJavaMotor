package com.bebel.api.events.mouse;

import com.badlogic.gdx.Input;
import com.bebel.api.events.SimpleInput;

/**
 * Evenement de base de la souris
 */
public class MouseInput extends SimpleInput {
    public int x, y, pointer, button, scroll, clickNb;

    public MouseInput(){}

    public int x() {
        return x;
    }

    public int y() {
        return y;
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
        x = y = pointer = button = scroll = clickNb = -1;
    }
}