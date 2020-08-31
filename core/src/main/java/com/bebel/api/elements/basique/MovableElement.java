package com.bebel.api.elements.basique;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.lwjgl.opengl.Display;
import pythagoras.f.Dimension;
import pythagoras.f.MathUtil;
import react.Signal;
import react.SignalView;

import static com.bebel.api.elements.basique.AbstractElement.Overflow.HIDDEN;

/**
 * Represente un element pouvant être deplacé et redimenssionné
 */
public abstract class MovableElement extends AbstractElement {
    public static int R_LEFT = (1 << 0), R_RIGHT = (1 << 1), R_UP = (1 << 2), R_DOWN = (1 << 3);

    public MovableElement(final String name) {super(name);}

    /**
     * DEBUG
     */
    @Override
    public void debugMe() {
        super.debugMe();
        input.whileKeyDown(k -> {
           // POSITION
           if (k.are(Input.Keys.LEFT)) move(-1, 0);
           else if (k.are(Input.Keys.RIGHT)) move(1, 0);
           if (k.are(Input.Keys.UP)) move(0, -1);
           else if (k.are(Input.Keys.DOWN)) move(0, 1);

           // TAILLE
           if (k.are(Input.Keys.Q)) resize(-1, 0);
           else if (k.are(Input.Keys.D)) resize(1, 0);
           if (k.are(Input.Keys.Z)) resize(0, 1);
           else if (k.are(Input.Keys.S)) resize(0, -1);
        });
        input.onKeyDown(k -> {
            if (k.is(Input.Keys.COMMA)) {
                Gdx.app.log(name + "-POSITION", x + ", " + relativeY());
                Gdx.app.log(name + "-TAILLE", width + ", " + height);
            }
        });
    }

    /**
     * Movable
     */
    protected float x, y;
    protected Signal<Dimension> onSizeChanged;

    public MovableElement x(final float x) {this.x = x; return this;}
    public float x() {
        return x;
    }

    public MovableElement y(final float y) {this.y = y; return this;}
    public float y() {
        return y;
    }
    public float relativeY() {
        return parentHeight() - height() - y();
    }

    protected float maxX() {
        float x = x(), rotation = 0;
        if (this instanceof TransformableElement) rotation = ((TransformableElement) this).rotation();
        if (rotation == 0 && isOverflow(HIDDEN) && x < 0) return -x;
        else return 0;
    }

    protected float maxY() {
        float y = y(), rotation = 0;
        if (this instanceof TransformableElement) rotation = ((TransformableElement) this).rotation();
        if (rotation == 0 && isOverflow(HIDDEN) && y < 0) return -y;
        else return 0;
    }

    public MovableElement position(final float x, final float y) {return position(x, y, R_UP | R_LEFT);}
    public MovableElement position(final float ox, final float oy, final int from) {
        float x = ox, y = oy;
        if ((from & R_UP) != 0) y = parentHeight() - height() - y;
        if ((from & R_RIGHT) != 0) x = parentWidth() - width() - x;
        x(x);
        return y(y);
    }

    /**
     * Ajoute un element à une position donnée
     */
    public void bottomAt(float tx, float ty) {bottomAt(tx, ty, width(), height());}
    public void bottomAt(float tx, float ty, final float w, final float h) {bottomAt(tx, ty, w, h, R_UP | R_LEFT);}
    public void bottomAt(float tx, float ty, final int from) {bottomAt(tx, ty, width(), height(), from);}
    public void bottomAt(float tx, float ty, final float w, final float h, final int from) {
        size(w, h); position(tx, ty - height(), from);
    }
    public void at(float tx, float ty) {at(tx, ty, width(), height());}
    public void at(float tx, float ty, final float w, final float h) {at(tx, ty, w, h, R_UP | R_LEFT);}
    public void at(float tx, float ty, final int from) {at(tx, ty, width(), height(), from);}
    public void at(float tx, float ty, final float w, final float h, final int from) {
        size(w, h); position(tx, ty, from);
    }
    public void centerAt(float tx, float ty) {centerAt(tx, ty, width(), height());}
    public void centerAt(float tx, float ty, final float w, final float h) {centerAt(tx, ty, w, h, R_UP | R_LEFT);}
    public void centerAt(float tx, float ty, final int from) {centerAt(tx, ty, width(), height(), from);}
    public void centerAt(float tx, float ty, final float w, final float h, final int from) {
        size(w, h); position(tx - width()/2, ty - height()/2, from);
    }
    public void floorAt(float tx, float ty) {floorAt(tx, ty, 0, 0);}
    public void floorAt(float tx, float ty, final float w, final float h) {floorAt(tx, ty, w, h, R_UP | R_LEFT);}
    public void floorAt(float tx, float ty, final int from) {floorAt(tx, ty, 0, 0, from);}
    public void floorAt(float tx, float ty, final float w, final float h, final int from) {
        size(w, h); position(MathUtil.ifloor(tx), MathUtil.ifloor(ty), from);
    }

    public MovableElement moveFromUp(final float y) {
        return move(0, y, R_UP);
    }
    public MovableElement moveFromDown(final float y) {
        return move(0, y, R_DOWN);
    }
    public MovableElement moveFromLeft(final float x) {
        return move(x, 0, R_LEFT);
    }
    public MovableElement moveFromRight(final float x) {
        return move(x, 0, R_RIGHT);
    }
    public MovableElement moveToUp(final float y) {
        return move(0, y, R_DOWN);
    }
    public MovableElement moveToDown(final float y) {
        return move(0, y, R_UP);
    }
    public MovableElement moveToLeft(final float x) {
        return move(x, 0, R_RIGHT);
    }
    public MovableElement moveToRight(final float x) {return move(x, 0, R_LEFT);}
    public MovableElement move(final float x, final float y) {
        return move(x, y, R_UP | R_LEFT);
    }
    public MovableElement move(final float ox, final float oy, final int from) {
        float x = ox, y = oy;
        if ((from & R_UP) != 0) y = -y;
        if ((from & R_RIGHT) != 0) x = -x;
        x(x() + x);
        return y(y() + y);
    }

    /**
     * Sizable
     */
    protected float width, height;
    protected boolean fitContent;

    public float width() {return width;}
    public MovableElement width(final float width, boolean updateOrigin) {
        this.width = width;
        dontFitContent();
        if (updateOrigin) updateOrigins();
        if (onSizeChanged != null) onSizeChanged.emit(oldSize);
        return this;
    }

    public float height() {return height;}
    public MovableElement height(final float height) {
        this.height = height;
        dontFitContent();
        updateOrigins();
        if (onSizeChanged != null) onSizeChanged.emit(oldSize);
        return this;
    }

    public float parentWidth() {
        if (parent != null) return parent.width();
        else return Display.getWidth();
    }
    public float parentHeight() {
        if (parent != null) return parent.height();
        else return Display.getHeight();
    }

    protected float minWidth(float x) {
        float rotation = 0;
        if (this instanceof TransformableElement) rotation = ((TransformableElement) this).rotation();
        if (rotation == 0 && isOverflow(HIDDEN) && parent != null) {
            float parentMinWidth = parent.minWidth(x());
            if (parentMinWidth < (width() - x)) return parentMinWidth;
        } return width() - x;
    }

    protected float minHeight(float y) {
        float rotation = 0;
        if (this instanceof TransformableElement) rotation = ((TransformableElement) this).rotation();
        if (rotation == 0 && isOverflow(HIDDEN) && parent != null) {
            float parentMinHeight = parent.minHeight(y());
            if (parentMinHeight < (height() - y)) return parentMinHeight;
        } return height() - y;
    }

    protected final Dimension oldSize = new Dimension();
    public MovableElement size(final float w, final float h) {
        oldSize.setSize(w, h);
        width(w, false); height(h);
        if (onSizeChanged != null) onSizeChanged.emit(oldSize);
        return this;
    }
    public MovableElement resize(final float w, final float h) {
        width(width + w, false); height(height + h);
        if (onSizeChanged != null) onSizeChanged.emit(oldSize);
        return this;
    }

    public MovableElement onSizeChanged(final SignalView.Listener<Dimension> action) {
        if (onSizeChanged == null) onSizeChanged = Signal.create();
        onSizeChanged.connect(action);
        return this;
    }

    public MovableElement fitContent() {fitContent = true; return this;}
    public MovableElement dontFitContent() {fitContent = false; return this;}

    protected abstract MovableElement updateOrigins();
}
