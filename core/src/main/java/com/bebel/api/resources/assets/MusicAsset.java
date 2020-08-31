package com.bebel.api.resources.assets;

import com.badlogic.gdx.audio.Music;

/**
 * Represente une ressource de type "Musique"
 */
public class MusicAsset extends AbstractAsset<Music> {
    public MusicAsset(final String path) {
        super(path, Music.class);
    }
}
