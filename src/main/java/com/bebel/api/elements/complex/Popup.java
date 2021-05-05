package com.bebel.api.elements.complex;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bebel.api.elements.basique.predicats.GroupElement;
import com.bebel.api.resources.assets.TextureAsset;

/**
 * Popup
 */
public class Popup extends GroupElement {
    public Popup(final TextureAsset background) {this(background.getRegion());}
    public Popup(final TextureRegion background) {
        super("Popup");
        background(background);
    }

    @Override
    protected void createImpl() {
        super.createImpl();
    }

    public Popup background(final TextureAsset asset) {
        background.image(asset);
        return this;
    }
}
