package com.bebel.api.resources.assets;

/**
 * Represente une ressource de type "fichier texte"
 */
public class StringAsset extends AbstractAsset<String> {
    public StringAsset(final String path) {
        super(path, String.class);
    }
}
