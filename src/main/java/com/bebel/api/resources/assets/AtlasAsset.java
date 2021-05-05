package com.bebel.api.resources.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.bebel.api.resources.animations.AnimationTemplate;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Represente une asset de type Atlas
 */
public class AtlasAsset extends AbstractAsset<TextureAtlas> {
    protected Map<String, AnimationTemplate> animations = new HashMap<>();

    public AtlasAsset(final String path) {
        super(path, TextureAtlas.class);
    }

    public TextureAsset findRegion(final String name) {
        return new TextureAsset(this, name);
    }

    /**
     * Creer un template d'animation à partir de toute les frames du fichier
     * @param frameDuration
     * @param playMode
     * @return
     */
    public AnimationTemplate createAnim(float frameDuration, final Animation.PlayMode playMode) {
        final String name = "total";
        if (!animations.containsKey(name))
            animations.put(name, new AnimationTemplate(frameDuration, get().getRegions(), playMode));

        return animations.get(name);
    }

    /**
     * Creer un template d'animation à partir des frames indiquées du fichier
     * @param name
     * @param frameDuration
     * @param playMode
     * @param frames
     * @return
     */
    public AnimationTemplate createAnim(final String name, float frameDuration, final Animation.PlayMode playMode, final String... frames) {
        if (!animations.containsKey(name)) {
            final TextureAtlas atlas = get();
            final Array<TextureAtlas.AtlasRegion> regions = new Array<>();

            for (final String frame : frames) {
                final Array<TextureAtlas.AtlasRegion> matched = atlas.findRegions(frame);
                regions.addAll(matched);
            }
            animations.put(name, new AnimationTemplate(frameDuration, regions, playMode));
        }
        return animations.get(name);
    }

    /**
     * Creer un template d'animation à partir des frames du fichier qui contienne le terme indiqué
     * @param name
     * @param frameDuration
     * @param playMode
     * @param frameModel
     * @return
     */
    public AnimationTemplate createAnimLike(final String name, float frameDuration, final Animation.PlayMode playMode, final String frameModel) {
        if (!animations.containsKey(name)) {
            final TextureAtlas atlas = get();
            final Array<TextureRegion> regions = new Array<>();
            for (final TextureAtlas.AtlasRegion region : atlas.getRegions()) {
                if (StringUtils.contains(region.name, frameModel)) {
                    regions.add(region);
                }
            }
            animations.put(name, new AnimationTemplate(frameDuration, regions, playMode));
        }
        return animations.get(name);
    }
}
