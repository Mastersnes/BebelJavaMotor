package com.bebel.api.resources;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.bebel.api.resources.elements.ElementsInfos;
import com.bebel.api.resources.elements.xml.ElementsNode;
import com.codeandweb.physicseditor.PhysicsShapeCache;

/**
 * Un loader permettant de charger un fichier xml et de le parser en map de bodies
 */
public class PhysicsLoader extends AsynchronousAssetLoader<PhysicsShapeCache, PhysicsLoader.PhysicsParameter> {
    private PhysicsShapeCache physicsShapeCache;

    public PhysicsLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String filename, FileHandle file, PhysicsParameter parameter) {
        return null;
    }

    @Override
    public void loadAsync(AssetManager manager, String filename, FileHandle file, PhysicsParameter parameters) {
        this.physicsShapeCache = null;
        this.physicsShapeCache = loadSync(manager, filename, file, parameters);
    }

    @Override
    public PhysicsShapeCache loadSync(AssetManager manager, String filename, FileHandle file, PhysicsParameter parameters) {
        return new PhysicsShapeCache(file);
    }

    public static class PhysicsParameter extends AssetLoaderParameters<PhysicsShapeCache> {}
}