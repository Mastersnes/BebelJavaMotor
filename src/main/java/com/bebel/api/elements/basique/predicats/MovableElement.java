package com.bebel.api.elements.basique.predicats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.bebel.api.Global;
import pythagoras.f.Dimension;
import pythagoras.f.MathUtil;
import react.Signal;
import react.SignalView;

import static com.bebel.api.elements.basique.predicats.AbstractElement.Overflow.HIDDEN;

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
        input().whileKeyDown(k -> {
            // POSITION
            if (k.containsOnlyOneOf(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.UP, Input.Keys.DOWN)) {
                if (k.contains(Input.Keys.LEFT)) move(-Global.scale, 0);
                else if (k.contains(Input.Keys.RIGHT)) move(Global.scale, 0);
                if (k.contains(Input.Keys.UP)) move(0, -Global.scale);
                else if (k.contains(Input.Keys.DOWN)) move(0, Global.scale);
            }

            //TAILLE
            if (k.contains(Input.Keys.S)) {
                if (k.contains(Input.Keys.LEFT)) resize(-Global.scale, 0);
                else if (k.contains(Input.Keys.RIGHT)) resize(Global.scale, 0);
                if (k.contains(Input.Keys.UP)) resize(0, Global.scale);
                else if (k.contains(Input.Keys.DOWN)) resize(0, -Global.scale);
            }
        });
        input().onKeyDown(k -> {
           if (k.contains(Input.Keys.C)) {
               if (k.containsOneOf(Input.Keys.UP, Input.Keys.DOWN)) centerY();
               else if (k.containsOneOf(Input.Keys.LEFT, Input.Keys.RIGHT)) centerX();
           }

           if (k.is(Input.Keys.COMMA)) {
               Gdx.app.log(name + "-POSITION", x() + ", " + relativeY());
               Gdx.app.log(name + "-TAILLE", width + ", " + height);
           }
        });
    }

    /**
     * Movable
     */
    protected final Vector2 oldPosition = new Vector2();
    protected final Vector2 position = new Vector2();
    protected Signal<Vector2> onPositionChanged;

    public MovableElement x(final float x) {
        if (this.x() == x) return this;
        oldPosition.set(this.x(), this.y());
        this.position.x = x;
        if (onPositionChanged != null) onPositionChanged.emit(oldPosition);
        return this;
    }
    public float x() {return position.x;}

    public MovableElement y(final float y) {
        if (this.y() == y) return this;
        oldPosition.set(this.x(), this.y());
        this.position.y = y;
        if (onPositionChanged != null) onPositionChanged.emit(oldPosition);
        return this;
    }
    public float y() {
        return position.y;
    }
    public float relativeY() {
        return relativeY(y());
    }
    public float relativeY(final float y) {
        return parentHeight() - height() - y;
    }

    public MovableElement onMove(final SignalView.Listener<Vector2> action) {return onPositionChanged(action);}
    public MovableElement onPositionChanged(final SignalView.Listener<Vector2> action) {
        if (onPositionChanged == null) onPositionChanged = Signal.create();
        onPositionChanged.connect(action);
        return this;
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
    public MovableElement bottomAt(float tx, float ty) {return bottomAt(tx, ty, width(), height());}
    public MovableElement bottomAt(float tx, float ty, final float w, final float h) {return bottomAt(tx, ty, w, h, R_UP | R_LEFT);}
    public MovableElement bottomAt(float tx, float ty, final int from) {return bottomAt(tx, ty, width(), height(), from);}
    public MovableElement bottomAt(float tx, float ty, final float w, final float h, final int from) {
        size(w, h); return position(tx, ty - height(), from);
    }
    public MovableElement floorAt(float tx, float ty) {return floorAt(tx, ty, 0, 0);}
    public MovableElement floorAt(float tx, float ty, final float w, final float h) {return floorAt(tx, ty, w, h, R_UP | R_LEFT);}
    public MovableElement floorAt(float tx, float ty, final int from) {return floorAt(tx, ty, 0, 0, from);}
    public MovableElement floorAt(float tx, float ty, final float w, final float h, final int from) {
        size(w, h); return position(MathUtil.ifloor(tx), MathUtil.ifloor(ty), from);
    }
    public MovableElement at(float tx, float ty) {return at(tx, ty, width(), height());}
    public MovableElement at(float tx, float ty, final float w, final float h) {return at(tx, ty, w, h, R_UP | R_LEFT);}
    public MovableElement at(float tx, float ty, final int from) {return at(tx, ty, width(), height(), from);}
    public MovableElement at(float tx, float ty, final float w, final float h, final int from) {
        size(w, h); return position(tx, ty, from);
    }

    /**
     * Place le centre le l'element à la position indiquée
     */
    public MovableElement centerAt(float tx, float ty) {return centerAt(tx, ty, width(), height());}
    public MovableElement centerAt(float tx, float ty, final float w, final float h) {return centerAt(tx, ty, w, h, R_UP | R_LEFT);}
    public MovableElement centerAt(float tx, float ty, final int from) {return centerAt(tx, ty, width(), height(), from);}
    public MovableElement centerAt(float tx, float ty, final float w, final float h, final int from) {
        size(w, h); return position(tx - width()/2, ty - height()/2, from);
    }

    public MovableElement centerX() {return x(parent.width()/2 - width()/2);}
    public MovableElement centerY() {return y(parent.height()/2 - height()/2);}
    public MovableElement center() {centerX(); return centerY();}
    public MovableElement center(final boolean x, final boolean y) {
        if (x) centerX();
        if (y) centerY();
        return this;
    }

    /**
     * Deplace l'element
     */
    public MovableElement moveFromUp(final float y) {return move(0, y, R_UP);}
    public MovableElement moveFromDown(final float y) {return move(0, y, R_DOWN);}
    public MovableElement moveFromLeft(final float x) {return move(x, 0, R_LEFT);}
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
    public MovableElement move(final float x, final float y) {return move(x, y, R_UP | R_LEFT); }
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
    protected final Dimension oldSize = new Dimension();
    protected Signal<Dimension> onSizeChanged;
    protected float width, height;
    protected boolean fitContent;

    public float width() {return width;}
    public MovableElement width(final float width, boolean updateOrigin) {
        if (this.width == width) return this;
        oldSize.setSize(this.width, this.height);
        this.width = width;
        dontFitContent();
        if (updateOrigin) updateOrigins();
        if (onSizeChanged != null) onSizeChanged.emit(oldSize);
        return this;
    }

    public float height() {return height;}
    public MovableElement height(final float height) {
        if (this.height == height) return this;
        oldSize.setSize(this.width, this.height);
        this.height = height;
        dontFitContent();
        updateOrigins();
        if (onSizeChanged != null) onSizeChanged.emit(oldSize);
        return this;
    }

    public float parentWidth() {
        if (parent != null) return parent.width();
        else return scene.width();
    }
    public float parentHeight() {
        if (parent != null) return parent.height();
        else return scene().height;
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

    public MovableElement size(final float w, final float h) {
        width(w, false); height(h);
        return this;
    }
    public MovableElement resize(final float w, final float h) {
        width(width + w, false); height(height + h);
        return this;
    }

    public MovableElement onSizeChanged(final SignalView.Listener<Dimension> action) {
        if (onSizeChanged == null) onSizeChanged = Signal.create();
        onSizeChanged.connect(action);
        return this;
    }

    public MovableElement fitContent() {fitContent = true; return this;}
    public MovableElement dontFitContent() {fitContent = false; return this;}

    protected abstract <ELEMENT extends TransformableElement> ELEMENT updateOrigins();
}
