package com.bebel.api.resources.assets;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;

/**
 * Represente une ressource de type "Particule"
 */
public class ParticleAsset extends AbstractAsset<ParticleEffect> {
    public ParticleAsset(final String path) {
        super(path, ParticleEffect.class);
    }
}
