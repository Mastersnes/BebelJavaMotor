package com.bebel.api.elements.basique;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bebel.api.BebelScreen;
import com.bebel.api.resources.assets.AbstractAsset;
import com.bebel.api.resources.assets.TextureAsset;
import com.bebel.api.shaders.AbstractShader;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.Display;
import react.Signal;

import java.util.ArrayList;
import java.util.List;

/**
 * Represente un element dessinable regroupant d'autres elements
 */
public class GroupElement extends DrawableElement {
    private final SnapshotArray<AbstractElement> children = new SnapshotArray(true, 4, AbstractElement.class);
    private final Matrix4 oldBatchTransform = new Matrix4();
    private final Color oldBatchColor = new Color();

    private AbstractElement background = null, foreground = null;
    private Signal<Boolean> childrenChanged;

    public GroupElement(final String name) {this(name, Display.getWidth(), Display.getHeight());}
    public GroupElement(final String name, float w, float h) {super(name, null, w, h);}

    @Override
    protected void createImpl() {
        super.createImpl();
        onSizeChanged((oldDimension) -> checkSize());
    }

    public void disposeAll() {
        final SnapshotArray<AbstractElement> toDispose = new SnapshotArray<>(children);
        removeAll();
        for (AbstractElement child : toDispose) {
            if (child == null) continue;
            child.close();
        }
    }

    /**
     * Permet de debugguer l'ensemble des elements de la liste
     * @param elements
     */
    public void debug(final EventableElement... elements) {
        for (final EventableElement element : elements) {
            element.debugMe();
        }
    }

    /**
     * CHILDREN
     */
    public <ELEMENT extends AbstractElement> ELEMENT background(final TextureAsset asset) {
        return (ELEMENT) background(new DrawableElement("background", asset));
    }
    public <ELEMENT extends AbstractElement> ELEMENT background(final ELEMENT element) {
        if (background != null) remove(background);
        background = null;
        background = insert(0, element);
        return element;
    }
    public <ELEMENT extends AbstractElement> ELEMENT foreground(final TextureAsset asset) {
        return (ELEMENT) foreground(new DrawableElement("foreground", asset));
    }
    public <ELEMENT extends AbstractElement> ELEMENT foreground(final ELEMENT element) {
        if (foreground != null) remove(foreground);
        foreground = null;
        foreground = add(element);
        return element;
    }
    public <ELEMENT extends AbstractElement> ELEMENT add(final String name, final TextureAsset asset) {
        return (ELEMENT) add(new DrawableElement(name, asset));
    }
    public <ELEMENT extends MovableElement> ELEMENT add(final String name, final TextureAsset asset, final float w, final float h) {
        final ELEMENT element = add(name, asset);
        return (ELEMENT) element.size(w, h);
    }
    public <ELEMENT extends MovableElement> ELEMENT add(final String name, final TextureAsset asset, final float x, final float y, final float w, final float h) {
        final ELEMENT element = add(name, asset, w, h);
        return (ELEMENT) element.position(x, y);
    }
    public <ELEMENT extends AbstractElement> ELEMENT add(final ELEMENT element) {
        if (foreground != null) return addBefore(foreground, element);
        else return insert(children.size, element);
    }

    public <ELEMENT extends AbstractElement> ELEMENT addBefore(final AbstractElement elementBefore, final ELEMENT newElement) {
        int index = children.indexOf(elementBefore, true);
        return insert(index, newElement);
    }
    public <ELEMENT extends AbstractElement> ELEMENT addAfter(final AbstractElement elementAfter, final ELEMENT newElement) {
        int index = children.indexOf(elementAfter, true);
        return insert(index+1, newElement);
    }
    public <ELEMENT extends AbstractElement> ELEMENT insert(final int index, final ELEMENT element) {
        element.remove();
        if (index >= children.size) children.add(element);
        else children.insert(index, element);
        element.setParent(this); element.setScreen(screen);
        element.added();
        if (childrenChanged != null) childrenChanged.emit(true);
        if (element instanceof MovableElement) {
            final MovableElement te = (MovableElement) element;
            if (te.width() == 0 && te.height() == 0) {
                te.size(width(), height());
                te.fitContent();
            }
        }
        if (!element.created) element.create();
        return element;
    }

    public <ELEMENT extends AbstractElement> ELEMENT remove(final ELEMENT element) {
        if (!children.removeValue(element, true)) return null;
        else {
            if (screen != null) screen.unfocus(element);
            element.removed();
            element.setParent(null); element.setScreen(null);
            if (childrenChanged != null) childrenChanged.emit(false);
            return element;
        }
    }
    public GroupElement removeAll() {
        final SnapshotArray<AbstractElement> children = new SnapshotArray<>(this.children);
        this.children.clear();
        for(final AbstractElement child : children.begin()) {
            if (child == null || !child.isAdded()) continue;
            child.setScreen(null); child.setParent(null);
            child.removed();
        }
        children.end();

        if (childrenChanged != null) childrenChanged.emit(false);
        return this;
    }

    public <ELEMENT extends AbstractElement> ELEMENT find(final String name) {
        try {
            for (final AbstractElement child : children.begin()) {
                if (child == null || !child.isAdded()) continue;
                if (StringUtils.equals(child.name, name)) return (ELEMENT) child;
            }
        }finally {
            children.end();
        }
        try {
            for (final AbstractElement child : children.begin()) {
                if (child == null || !child.isAdded()) continue;
                if (child instanceof GroupElement) {
                    final ELEMENT element = ((GroupElement) child).find(name);
                    if (element != null) return element;
                }
            }
        }finally {
            children.end();
        }
        return null;
    }

    @Override
    public void setScreen(BebelScreen screen) {
        super.setScreen(screen);
        for (final AbstractElement child : children.begin()) {
            if (child == null || !child.isAdded()) continue;
            child.setScreen(screen);
        }
        children.end();
    }

    public void swap(final int _first, int _second) {
        int first = _first, second = _second;
        if (first < 0) first = 0;
        if (first > children.size-1) first = children.size-1;
        if (second < 0) second = 0;
        if (second > children.size-1) second = children.size-1;
        this.children.swap(first, second);
    }
    public void swap(final AbstractElement first, AbstractElement second) {
        if (first == null || second == null) return;
        int firstIndex = this.children.indexOf(first, true);
        int secondIndex = this.children.indexOf(second, true);
        if (firstIndex == -1 || secondIndex == -1) return;
        this.children.swap(firstIndex, secondIndex);
    }

    public void near(final AbstractElement element) {
        if (element == null) return;
        int index = this.children.indexOf(element, true);
        if (index == -1) return;
        swap(index, index+1);
    }
    public void toNearest(final AbstractElement element) {
        if (element == null) return;
        final boolean removed = children.removeValue(element, true);
        if (removed) children.add(element);
    }
    public void far(final AbstractElement element) {
        if (element == null) return;
        int index = this.children.indexOf(element, true);
        if (index == -1) return;
        swap(index, index-1);
    }
    public void toFarthest(final AbstractElement element) {
        if (element == null) return;
        final boolean removed = children.removeValue(element, true);
        if (removed) children.insert(0, element);
    }

    public boolean isEmpty() {return children.size == 0;}

    protected void checkSize() {
        for(final AbstractElement child : children.begin()) {
            if (child == null || !child.isAdded()) continue;
            if (child instanceof MovableElement) {
                final MovableElement te = (MovableElement) child;
                if (te.fitContent) {
                    te.size(width, height); te.fitContent();
                }
            }
            if (child instanceof GroupElement) {
                ((GroupElement) child).checkSize();
            }
        }
        children.end();
    }

    /**
     * DRAWABLE
     */
    protected final Rectangle area = new Rectangle();
    protected final Rectangle scissors = new Rectangle();
    @Override
    protected void paintImpl (final SpriteBatch batch) {
        super.paintImpl(batch);

        boolean alreadyPainted = false;
        if (rotation == 0 && overflow == AbstractElement.Overflow.HIDDEN) {
            area.set(0, 0, width, height);

            final Camera camera = screen.getCamera();
            final Viewport viewport = screen.getViewport();
            ScissorStack.calculateScissors(camera, viewport.getViewportX(), viewport.getViewportY(), viewport.getViewportWidth(), viewport.getViewportHeight(), batch.getTransformMatrix(), area, scissors);

            if (ScissorStack.pushScissors(scissors)) {
                alreadyPainted = true; paintClip(batch);
                ScissorStack.popScissors();
            }
        }
        if (!alreadyPainted) paintClip(batch);
    }

    protected void paintClip (final SpriteBatch batch) {
        oldBatchTransform.set(batch.getTransformMatrix());
        oldBatchColor.set(batch.getColor());

        for (final AbstractElement child : children.begin()) {
            if (child == null || !child.isAdded()) continue;
            if (!(child instanceof DrawableElement)) continue;
            batch.getTransformMatrix().set(oldBatchTransform);
            batch.getColor().set(oldBatchColor);
            ((DrawableElement) child).paint(batch);
        }
        children.end();
    }

    @Override
    public DrawableElement flip(boolean x, boolean y) {
        super.flip(x, y);
        for (final AbstractElement child : children.begin()) {
            if (!(child instanceof DrawableElement)) continue;
            ((DrawableElement) child).flip(x, y);
        }
        return this;
    }

    /**
     * Retourne la liste de tout les shaders et sous shader du group
     * @return
     */
    protected final ArrayList<AbstractShader> shaders = new ArrayList<>();
    public List<AbstractShader> getShaders() {
        shaders.clear();
        if (shader != null) shaders.add(shader);

        for (final AbstractElement child : children.begin()) {
            if (child == null || !child.isAdded()) continue;
            if (!(child instanceof DrawableElement)) continue;
            final DrawableElement drawableChild = (DrawableElement) child;
            if (drawableChild.shader != null) shaders.add(drawableChild.shader);
            if (child instanceof GroupElement) {
                shaders.addAll(((GroupElement) child).getShaders());
            }
        }

        return shaders;
    }

    /**
     * EVENTABLE
     */
    @Override
    public EventableElement hitTestDefault(Vector2 point) {
        float x = point.x, y = point.y;
        boolean sawInteractiveChild = false;

        // we check back to front as children are ordered "lowest" first
        try {
            final AbstractElement[] children = this.children.begin();
            for (int ii = children.length - 1; ii >= 0; ii--) {
                if (children[ii] == null || !children[ii].isAdded()) continue;
                if (!(children[ii] instanceof EventableElement)) continue;
                final EventableElement child = (EventableElement) children[ii];
                if (!child.interactive()) continue; // ignore non-interactive children
                sawInteractiveChild = true; // note that we saw an interactive child
                if (!child.visible()) continue; // ignore invisible children
                final EventableElement l = child.hitTest(point.set(x, y));
                if (l != null) return l;
            }
        }finally {
            this.children.end();
        }

        if (!sawInteractiveChild && !hasEventListeners()) interactive(false);
        return super.hitTestDefault(point.set(x, y));
    }

    @Override
    public boolean update(float delta) {
        super.update(delta);
        for (final AbstractElement child : children.begin()) {
            if (child == null || !child.isAdded()) continue;
            child.update(delta);
        }
        children.end();
        return true;
    }

    public GroupElement onChildrenChanged(final Signal.Listener<Boolean> event) {
        if (childrenChanged == null) childrenChanged = Signal.create();
        childrenChanged.connect(event); return this;
    }
}
