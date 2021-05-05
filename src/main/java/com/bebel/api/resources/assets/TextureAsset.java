package com.bebel.api.resources.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.bebel.api.resources.animations.AnimationTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Represente une ressource de type "Image fixe"
 */
public class TextureAsset extends AbstractAsset<Texture> {
    protected AtlasAsset atlas;
    protected String regionToFind;

    protected Map<String, AnimationTemplate> animations = new HashMap<>();

    public TextureAsset(final AtlasAsset atlas, final String regionToFind) {
        super(null, null);
        this.atlas = atlas;
        this.regionToFind = regionToFind;
    }
    public TextureAsset(final String path) {
        super(path, Texture.class);
    }

    public TextureRegion getRegion() {
        if (atlas != null) return atlas.get().findRegion(regionToFind);
        return new TextureRegion(super.get());
    }

    /**
     * Creer une animation à partir de toute les frames de l'image
     * @param frameDuration
     * @param playMode
     * @param nbFramesX
     * @param nbFramesY
     * @return
     */
    public AnimationTemplate createAnim(float frameDuration, final Animation.PlayMode playMode, final int nbFramesX, final int nbFramesY) {
        final String name = "total";
        if (animations.containsKey(name))
            return animations.get(name);
        else {
            final Texture texture = get();
            final TextureRegion[][] splitted = TextureRegion.split(texture, texture.getWidth() / nbFramesX, texture.getHeight() / nbFramesY);
            final Array<TextureRegion> regions = new Array<>();

            for (final TextureRegion[] splittedRegions : splitted) {
                regions.addAll(splittedRegions);
            }

            return animations.put(name, new AnimationTemplate(frameDuration, regions, playMode));
        }
    }
}
