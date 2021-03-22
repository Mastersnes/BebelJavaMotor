package com.bebel.api.resources.assets;

import com.bebel.api.resources.elements.ElementsInfos;

/**
 * Represente une ressource de type "Information d'elements"
 */
public class ElementsInfosAsset extends AbstractAsset<ElementsInfos> {
    public ElementsInfosAsset(final String path) {
        super(path, ElementsInfos.class);
    }
}
