package com.bebel.api.resources;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * Un loader permettant de charger un simple fichier texte
 */
public class StringLoader extends AsynchronousAssetLoader<String, StringLoader.StringParameter> {
    private String text;

    public StringLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String filename, FileHandle file, StringParameter parameter) {
        return null;
    }

    @Override
    public void loadAsync(AssetManager manager, String filename, FileHandle file, StringParameter parameters) {
        this.text = null;
        this.text = file.readString();
    }

    @Override
    public String loadSync(AssetManager assetManager, String s, FileHandle fileHandle, StringParameter stringParameter) {
        return fileHandle.readString();
    }

    public static class StringParameter extends AssetLoaderParameters<String> {}
}