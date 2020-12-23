package com.bebel.api.elements.basique;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.SnapshotArray;
import com.bebel.api.resources.assets.TextureAsset;
import com.bebel.api.shaders.AbstractShader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represente un element dessinable regroupant d'autres elements
 */
//public class OldGroupElement extends Element implements Iterable<AbstractElement> {
//    protected final List<AbstractElement> children = new ArrayList<>();
//    protected final Matrix4 paintTx = new Matrix4();
//    protected final Color paintColor = new Color();
//
//    public OldGroupElement(final String name, float w, float h) {super(name, w, h);}
//    public OldGroupElement(final String name) {super(name);}
//
//    /**
//     * CHILDREN
//     */
//    public boolean isEmpty () { return children.size() == 0; }
//    public int size() {return children.size();}
//    public <ELEMENT extends AbstractElement> ELEMENT childAt(int index) {
//        if (index < 0 || index >= size()) return null;
//        return (ELEMENT) children.get(index);
//    }
//    @Override public Iterator<AbstractElement> iterator () {return children.iterator();}
//
//    public <ELEMENT extends AbstractElement> ELEMENT add(ELEMENT child) {return add(child, size());}
//    public <ELEMENT extends AbstractElement> ELEMENT add(ELEMENT child, final int cibleIndex) {
//        final OldGroupElement parent = child.parent();
//        if (parent == this) return child;
//
//        int index = cibleIndex;
//        if (index < 0) index = 0;
//        if (index > size()) index = size();
//
//        if (parent != null) parent.remove(child);
//        children.add(index, child);
//        child.setParent(this); child.setScene(scene);
//
//        if (state.get() == State.ADDED) child.added();
//        if (child.interactive()) interactive(true);
//
//        if (child instanceof TransformableElement) {
//            final TransformableElement tChild = (TransformableElement) child;
//            if (tChild.width() == 0 && tChild.height() == 0) {
//                tChild.size(width, height);
//                tChild.fitContent = true;
//            }
//        }
//
//        if (!child.created) {
//            child.created = true;
//            child.create();
//        }
//
//        return child;
//    }
//    public OldGroupElement add(AbstractElement... childN) {
//        for (final AbstractElement child : childN) add(child);
//        return this;
//    }
//
//    public <ELEMENT extends AbstractElement> ELEMENT addAfter(final AbstractElement oldChild, final ELEMENT newChild) {
//        final int index = children.indexOf(oldChild);
//        if (index < 0) return null;
//        return add(newChild, index);
//    }
//    public <ELEMENT extends AbstractElement> ELEMENT addBefore(final AbstractElement oldChild, final ELEMENT newChild) {
//        final int index = children.indexOf(oldChild);
//        if (index < 0) return null;
//        return add(newChild, index-1);
//    }
//
//    public <ELEMENT extends AbstractElement> ELEMENT remove(ELEMENT child) {
//        int index = children.indexOf(child);
//        if (index < 0) {
//            Gdx.app.error(name, "Could not remove Layer because it is not a child of the GroupLayer " +
//                            "[group=" + this + ", layer=" + child + "]");
//            return null;
//        }
//        return remove(index);
//    }
//    public OldGroupElement remove(AbstractElement... childN) {
//        for (AbstractElement child : childN) remove(child);
//        return this;
//    }
//    public void removeAll() {
//        while (!children.isEmpty()) remove(children.size()-1);
//    }
//    private <ELEMENT extends AbstractElement> ELEMENT remove(int index) {
//        final AbstractElement child = children.remove(index);
//        child.removed();
//        child.setParent(null); child.setScene(null);
//        return (ELEMENT) child;
//    }
//
//    public <ELEMENT extends AbstractElement> ELEMENT near(final ELEMENT element) {
//        final int index = children.indexOf(element);
//        if (index < 0) return null;
//        if (index == children.size()-1) return element;
//        Collections.swap(children, index, index+1);
//        return element;
//    }
//    public <ELEMENT extends AbstractElement> ELEMENT toNearest(final ELEMENT element) {
//        final int index = children.indexOf(element);
//        if (index < 0) return null;
//        remove(element);
//        return add(element, size());
//    }
//    public <ELEMENT extends AbstractElement> ELEMENT far(final ELEMENT element) {
//        final int index = children.indexOf(element);
//        if (index < 0) return null;
//        if (index == 0) return element;
//        Collections.swap(children, index, index-1);
//        return element;
//    }
//    public <ELEMENT extends AbstractElement> ELEMENT toFarthest(final ELEMENT element) {
//        final int index = children.indexOf(element);
//        if (index < 0) return null;
//        remove(element);
//        return add(element, 0);
//    }
//
//    /**
//     * EVENTABLE
//     */
//    @Override public EventableElement hitTestDefault(Vector2 point) {
//        float x = point.x, y = point.y;
//        boolean sawInteractiveChild = false;
//
//        // we check back to front as children are ordered "lowest" first
//        for (int ii = children.size()-1; ii >= 0; ii--) {
//            if (!(children.get(ii) instanceof EventableElement)) continue;
//            final EventableElement child = (EventableElement) children.get(ii);
//            if (!child.interactive()) continue; // ignore non-interactive children
//            sawInteractiveChild = true; // note that we saw an interactive child
//            if (!child.visible()) continue; // ignore invisible children
//            final EventableElement l = child.hitTest(point.set(x, y));
//            if (l != null) return l;
//        }
//
//        if (!sawInteractiveChild && !hasEventListeners()) interactive(false);
//        return super.hitTestDefault(point.set(x, y));
//    }
//
//    @Override
//    public boolean update(final float delta) {
//        super.update(delta);
//        List<AbstractElement> children = this.children;
//        for (int ii = 0, ll = children.size(); ii < ll; ii++) {
//            children.get(ii).update(delta);
//        }
//        return true;
//    }
//
//    /**
//     * DRAWABLE
//     */
//    /**
//     * Retourne la liste de tout les shaders et sous shader du group
//     * @return
//     */
//    protected final ArrayList<AbstractShader> shaders = new ArrayList<>();
//    public List<AbstractShader> getShaders() {
//        shaders.clear();
//        if (shader != null) shaders.add(shader);
//
//        for (int ii = 0, ll = children.size(); ii < ll; ii++) {
//            if (!(children.get(ii) instanceof DrawableElement)) continue;
//            final DrawableElement child = (DrawableElement) children.get(ii);
//            if (child.shader != null) shaders.add(child.shader);
//            if (child instanceof OldGroupElement) {
//                shaders.addAll(((OldGroupElement) child).getShaders());
//            }
//        }
//
//        return shaders;
//    }
//
//    protected final Rectangle area = new Rectangle();
//    protected final Rectangle scissors = new Rectangle();
//    @Override protected void paintImpl (final SpriteBatch batch) {
//        super.paintImpl(batch);
//
//        boolean alreadyPainted = false;
//        if (rotation == 0 && overflow == Overflow.HIDDEN) {
//            area.set(maxX(), maxY(), minWidth(0), minHeight(0));
//            ScissorStack.calculateScissors(scene.getCamera(), batch.getTransformMatrix(), area, scissors);
//
//            if (ScissorStack.pushScissors(scissors)) {
//                alreadyPainted = true; paintClip(batch);
//                ScissorStack.popScissors();
//            }
//        }
//        if (!alreadyPainted) paintClip(batch);
//    }
//
//    protected void paintClip (final SpriteBatch batch) {
//        // save our current transform and restore it before painting each child
//        paintTx.set(batch.getTransformMatrix());
//        paintColor.set(batch.getColor());
//        // iterate manually to avoid creating an Iterator as garbage, this is inner-loop territory
//        for (int ii = 0, ll = children.size(); ii < ll; ii++) {
//            final AbstractElement child = childAt(ii);
//            if (!(child instanceof DrawableElement)) continue;
//            batch.getTransformMatrix().set(paintTx);
//            batch.getColor().set(paintColor);
//            ((DrawableElement) child).paint(batch);
//        }
//    }
//
//    /**
//     * TRANSFORMABLE
//     */
//    @Override
//    public MovableElement width(float width, boolean updateOrigin) {
//        super.width(width, updateOrigin);
//        if (children != null) {
//            for (int ii = 0, ll = children.size(); ii < ll; ii++) {
//                if (!(children.get(ii) instanceof MovableElement)) continue;
//                final MovableElement child = (MovableElement) children.get(ii);
//                if (child.fitContent) {
//                    child.width(width, updateOrigin); child.fitContent();
//                }
//            }
//        }
//        return this;
//    }
//    @Override
//    public MovableElement height(float height) {
//        super.height(height);
//        if (children != null) {
//            for (int ii = 0, ll = children.size(); ii < ll; ii++) {
//                if (!(children.get(ii) instanceof MovableElement)) continue;
//                final MovableElement child = (MovableElement) children.get(ii);
//                if (child.fitContent) {
//                    child.height(height); child.fitContent();
//                }
//            }
//        }
//        return this;
//    }
//
//    /**
//     * STATABLE
//     */
//    public void disposeAll() {
//        final List<AbstractElement> toDispose = new ArrayList<>(children);
//        removeAll();
//        for (AbstractElement child : toDispose) child.close();
//    }
//    @Override
//    public void close() {
//        super.close(); disposeAll();
//    }
//
//    @Override
//    public void added() {
//        super.added();
//        for (int ii = 0, ll = children.size(); ii < ll; ii++) children.get(ii).added();
//    }
//
//    @Override
//    public void removed() {
//        super.removed();
//        for (int ii = 0, ll = children.size(); ii < ll; ii++) children.get(ii).removed();
//    }
//
//    @Override
//    public String toString () {
//        final StringBuilder buf = new StringBuilder(super.toString());
//        buf.append("children=").append(children.size());
//        return buf.toString();
//    }
//}
