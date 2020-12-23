package com.bebel.api.resources.assets;

import com.badlogic.gdx.audio.Sound;

/**
 * Represente une ressource de type "Son"
 */
public class SoundAsset extends AbstractAsset<Sound> {
    public SoundAsset(final String path) {
        super(path, Sound.class);
    }
}
