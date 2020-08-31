package com.bebel.api.elements.basique;

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
import com.bebel.api.shaders.AbstractShader;
import pythagoras.f.MathUtil;

/**
 * Represente un element transformable et dessinable
 */
public class DrawableElement extends TransformableElement {
    protected AbstractShader shader;
    protected TextureRegion image;
    protected boolean flipX, flipY;

    protected Color tint = Color.WHITE.cpy();
    protected float alpha = 1;


    public DrawableElement(String name) {super(name);}

    /**
     * DEBUG
     */
    @Override
    public void debugMe() {
        super.debugMe();
        input.onKeyDown(k -> {
            if (k.is(Input.Keys.F)) {
                if (k.isCtrlDown()) flipY();
                else flipX();
            }

            if (k.is(Input.Keys.COMMA)) {
                if (image.isFlipX()) Gdx.app.log(name + "-FLIP", "X");
                if (image.isFlipY()) Gdx.app.log(name + "-FLIP", "Y");
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

        float x = this.x;
        float y = this.y;

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
        if (this.debug) {
            batch.flush();
            final ShapeRenderer shape = Global.shape;
            shape.setProjectionMatrix(batch.getProjectionMatrix());
            shape.setTransformMatrix(batch.getTransformMatrix());
            shape.setColor(Color.RED.cpy());
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.rect(0, 0, width, height);
            shape.end();
        }
    }

    protected void draw(final SpriteBatch batch, final Texture image) {
        if (image != null) {
            batch.draw(image,
                    0, 0,
                    width, height);
        }
    }
    protected void draw(final SpriteBatch batch, final TextureRegion image) {
        if (image != null) {
            batch.draw(image,
                    0, 0,
                    width, height);
        }
    }

    /**
     * COULEUR et OPACITE
     */
    public float alpha() {return alpha;}
    public DrawableElement alpha(float alpha) {
        this.alpha = alpha;
        this.tint.a = MathUtil.clamp(alpha, 0, 1);
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
    public DrawableElement image(final TextureAsset image) {return image(new TextureRegion(image.get()));}
    public DrawableElement image(final TextureRegion image) {this.image = image; return this;}

    public DrawableElement flip(final boolean x, final boolean y) {image.flip(x, y); return this;}
    public DrawableElement flipX() {image.flip(true, false); return this;}
    public DrawableElement flipY() {image.flip(false, true); return this;}

    /**
     * SHADER
     */
    public void setShader(final AbstractShader shader) {
        this.shader = shader;
    }

    public AbstractShader getShader() {
        if (parent == null || shader != null) return shader;
        else return parent.getShader();
    }
    protected void processShader(final SpriteBatch batch) {
        final AbstractShader shader = getShader();
        if (shader != null) {
            batch.setShader(shader.shader());
            shader.bind(this);
        }
    }

    @Override
    public void dispose() {}
}
