package com.bebel.api.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.bebel.api.resources.elements.ElementsInfos;
import com.codeandweb.physicseditor.PhysicsShapeCache;

/**
 * Manager de ressource
 */
public class ResourceManager extends AssetManager {
    private static ResourceManager instance;

    private ResourceManager(final FileHandleResolver resolver) {
        super(resolver);
        this.setLoader(String.class, new StringLoader(resolver));
        this.setLoader(ElementsInfos.class, new ElementsInfosLoader(resolver));
        this.setLoader(PhysicsShapeCache.class, new PhysicsLoader(resolver));
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager(new InternalFileHandleResolver());
        }
        return instance;
    }

    @Override
    public synchronized <T> T get(final String fileName) {
        try {
            return super.get(fileName);
        } catch (final Exception e) {
            finishLoading();
            return super.get(fileName);
        }
    }

    @Override
    public synchronized <T> T get(String fileName, Class<T> type) {
        try {
            return super.get(fileName, type);
        } catch (final Exception e) {
            load(fileName, type);
            finishLoading();
            return super.get(fileName, type);
        }
    }
}
