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

/**
 * Un loader permettant de charger un fichier xml et de le parser en map d'informations d'elements
 */
public class ElementsInfosLoader extends AsynchronousAssetLoader<ElementsInfos, ElementsInfosLoader.ElementsInfosParameter> {
    private ElementsInfos elements;

    public ElementsInfosLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String filename, FileHandle file, ElementsInfosParameter parameter) {
        return null;
    }

    @Override
    public void loadAsync(AssetManager manager, String filename, FileHandle file, ElementsInfosParameter parameters) {
        this.elements = null;
        this.elements = loadSync(manager, filename, file, parameters);
    }

    @Override
    public ElementsInfos loadSync(AssetManager manager, String filename, FileHandle file, ElementsInfosParameter parameters) {
        final XmlReader reader = new XmlReader();
        final XmlReader.Element rootNode = reader.parse(file);
        final ElementsNode elements = new ElementsNode(rootNode);
        return elements.parse();
    }

    public static class ElementsInfosParameter extends AssetLoaderParameters<ElementsInfos> {}
}