package com.bebel.api.resources.assets;

import com.badlogic.gdx.graphics.g3d.Model;

/**
 * Represente une ressource de type "Objet 3D"
 */
public class Object3DAsset extends AbstractAsset<Model> {
    public Object3DAsset(final String path) {
        super(path, Model.class);
    }
}
