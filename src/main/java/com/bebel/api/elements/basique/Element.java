package com.bebel.api.elements.basique;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bebel.api.resources.animations.AnimationTemplate;
import com.bebel.api.resources.animations.BebelAnimation;
import com.bebel.api.resources.assets.TextureAsset;

/**
 * Represente un element complet et utilisable
 */
//public class Element extends EventableElement {
//    public static Element EMPTY = new Element("EMPTY");
//
//    public Element(final String name) {this(name, 0, 0);}
//    public Element(final String name, final TextureAsset image) {this(name, image, 0, 0);}
//    public Element(final String name, final Texture image) {this(name, image, 0, 0);}
//    public Element(final String name, final TextureRegion image) {this(name, image, 0, 0);}
//    public Element(final String name, final Color color) {this(name, color, 0, 0);}
//
//    public Element(final String name, final float w, final float h) {this(name, (TextureRegion) null, w, h);}
//    public Element(final String name, final Color color, final float w, final float h) {
//        this(name, (TextureRegion) null, w, h); image(color);
//    }
//    public Element(final String name, final TextureAsset image, final float w, final float h) {
//        this(name, (TextureRegion) null, w, h); image(image);
//    }
//    public Element(final String name, final Texture image, final float w, final float h) {
//        this(name, (TextureRegion) null, w, h); image(image);
//    }
//    public Element(final String name, final TextureRegion image, final float w, final float h) {
//        super(name); size(w, h);
//        if (image != null) image(image);
//    }
//
//    public Element(final String name, final AnimationTemplate anim, final float w, final float h) {
//        this(name, anim.instance(), w, h);
//    }
//    public Element(final String name, final AnimationTemplate anim) {this(name, anim.instance(), 0, 0);}
//    public Element(final String name, final BebelAnimation anim) {this(name, anim, 0, 0);}
//    public Element(final String name, final BebelAnimation anim, final float w, final float h) {
//        super(name); size(w, h);
//        if (anim != null) {
//            final String animName = name + "-Anim";
//            addAnim(animName, anim);
//            play(animName);
//        }
//    }
//}
