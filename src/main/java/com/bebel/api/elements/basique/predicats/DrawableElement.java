package com.bebel.api.elements.basique.predicats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.bebel.api.Global;
import com.bebel.api.resources.ResourceManager;
import com.bebel.api.resources.assets.TextureAsset;
import com.bebel.api.utils.SimpleVector;
import pythagoras.f.MathUtil;
import react.Signal;
import react.SignalView;

/**
 * Represente un element dessinable
 */
public class DrawableElement extends EventableElement {
    protected TextureRegion image;

    protected Color tint = Color.WHITE.cpy();
    protected float alpha = 1;

    public DrawableElement(final String name, final TextureAsset image) {this(name, image.getRegion());}
    public DrawableElement(final String name, final TextureRegion image) {
        super(name);
        if (image != null) image(image);
    }

    /**
     * DEBUG
     */
    @Override
    public void debugMe() {
        super.debugMe();
        showBounds(Color.RED.cpy());
        alpha(0.8f);
        input().onKeyDown(k -> {
            if (k.contains(Input.Keys.F)) {
                if (k.containsOneOf(Input.Keys.UP, Input.Keys.DOWN)) flipY();
                else if (k.containsOneOf(Input.Keys.LEFT, Input.Keys.RIGHT)) flipX();
            }

            if (k.is(Input.Keys.COMMA)) {
                if (image!=null && image.isFlipX()) Gdx.app.log(name + "-FLIP", "X");
                if (image!=null && image.isFlipY()) Gdx.app.log(name + "-FLIP", "Y");
            }
        });
    }

    /**
     * DRAWABLE
     */
    public final void paint(final SpriteBatch batch) {
        if (!visible()) return;
        final Color otint = batch.getColor().cpy();
        final Matrix4 otrans = new Matrix4(batch.getTransformMatrix());

        processShader(batch);

        // On teinte le crayon avec la couleur du calque
        batch.setColor(otint.mul(tint));

        final Matrix4 t = batch.getTransformMatrix();

        float x = this.x();
        float y = this.y();

        t.translate(x, y, 0);

        t.translate(scaleVector.x, scaleVector.y, 0);
        t.scale(scaleX, scaleY, 1);
        t.translate(-scaleVector.x, -scaleVector.y, 0);

        t.translate(rotateVector.x, rotateVector.y, 0);
        t.rotate(0, 0, 1, rotation);
        t.translate(-rotateVector.x, -rotateVector.y, 0);

        batch.setTransformMatrix(t);

        paintImpl(batch);

        //On reposition les valeurs de base
        batch.setTransformMatrix(otrans);
        batch.setColor(otint);

        batch.flush();
        batch.setShader(null);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
    }

    protected void paintImpl(final SpriteBatch batch){
        draw(batch, image);
        if (this instanceof GroupElement) return;
        if (this.boundsColor != null || parent.boundsColor != null) {
            batch.end();
            final ShapeRenderer shape = Global.shape;
            shape.setProjectionMatrix(batch.getProjectionMatrix());
            shape.setTransformMatrix(batch.getTransformMatrix());
            shape.setColor(boundsColor != null ? boundsColor : parent.boundsColor);
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.rect(0, 0, width, height);
            shape.end();
            batch.begin();
        }
    }

    protected void draw(final SpriteBatch batch, final Texture image) {
        if (image != null) {
            batch.draw(image,
                    0, 0,
                    width, height);
        }
    }

    protected boolean firstTime = true;
    protected void draw(final SpriteBatch batch, final TextureRegion image) {
        if (image != null) {
            batch.draw(image,
                    0, 0,
                    width, height);
        }
    }

    @Override
    public boolean update(float delta) {
        if (shader != null) {
            shader.update(delta);
        }
        return super.update(delta);
    }

    /**
     * COULEUR et OPACITE
     */
    protected Float oldAlpha;
    protected Signal<Float> onAlphaChanged;

    public float alpha() {return alpha;}
    public DrawableElement alpha(float alpha) {
        if (this.alpha == alpha) return this;
        oldAlpha = this.alpha;
        this.alpha = alpha;
        this.tint.a = MathUtil.clamp(alpha, 0, 1);
        if (onAlphaChanged != null) onAlphaChanged.emit(alpha);
        return this;
    }

    public DrawableElement onAlphaChanged(final SignalView.Listener<Float> action) {
        if (onAlphaChanged == null) onAlphaChanged = Signal.create();
        onAlphaChanged.connect(action);
        return this;
    }

    public Color tint() {return tint;}
    public DrawableElement tint(final Color color) {
        this.tint = color;
        this.alpha = color.a;
        return this;
    }

    /**
     * IMAGE
     */
    public DrawableElement image(final Color image) {
        final Pixmap pixmap = new Pixmap((int) width(), (int) height(), Pixmap.Format.RGBA8888);
        pixmap.setColor(image);
        pixmap.fillRectangle(0, 0, (int) width(), (int) height());
        return image(new Texture(pixmap));
    }
    public DrawableElement image(final String image) {
        final Texture texture = ResourceManager.getInstance().get(image);
        return image(texture);
    }
    public DrawableElement image(final Texture image) {return image(new TextureRegion(image));}
    public DrawableElement image(final TextureAsset image) {return image(image.get());}
    public DrawableElement image(final TextureRegion image) {this.image = image; return this;}

    protected SimpleVector<Boolean> oldFlip = new SimpleVector<>();
    protected Signal<SimpleVector<Boolean>> onFlipChanged;

    public DrawableElement flip(final boolean x, final boolean y) {
        if (image != null) {
            oldFlip.set(image.isFlipX(), image.isFlipY());
            image.flip(x, y);
            if (onFlipChanged != null) onFlipChanged.emit(oldFlip);
        }
        return this;
    }
    public DrawableElement flipX() {return flip(true, false);}
    public DrawableElement flipY() {return flip(false, true);}

    public boolean isFlipX() {return image!=null && image.isFlipX();}
    public boolean isFlipY() {return image!=null && image.isFlipY();}

    @Override
    public void dispose() {}
}
