package com.bebel.api.resources.assets;

import com.codeandweb.physicseditor.PhysicsShapeCache;

/**
 * Represente une ressource de type "Physics"
 */
public class PhysicsAsset extends AbstractAsset<PhysicsShapeCache> {
    public PhysicsAsset(final String path) {
        super(path, PhysicsShapeCache.class);
    }
}
