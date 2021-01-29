package com.bebel.api.elements.complex;

import com.bebel.api.elements.basique.DrawableElement;
import com.bebel.api.elements.basique.GroupElement;
import com.bebel.api.resources.assets.TextureAsset;

/**
 * Popup
 */
public class Popup extends GroupElement {
    protected DrawableElement background;

    public Popup(final TextureAsset background, final float w, final float h) {
        super("Popup", w, h);
        this.background = new DrawableElement("background", background);
    }

    @Override
    protected void createImpl() {
        super.createImpl();
        background(background);
    }

    public Popup background(final TextureAsset asset) {
        background.image(asset);
        return this;
    }
}
