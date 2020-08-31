package com.bebel.api.resources.assets;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bebel.api.resources.ResourceManager;

/**
 * Represente une ressource de type "Police de caractere"
 */
public class FontAsset extends AbstractAsset<BitmapFont> {
    public FontAsset(final String path) {
        super(path, BitmapFont.class);
    }
}
