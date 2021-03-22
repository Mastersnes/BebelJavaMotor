package com.bebel.api.resources.elements.xml;

import com.badlogic.gdx.utils.XmlReader;
import com.bebel.api.resources.elements.ElementInfos;

/**
 * Objet XML representant un element
 */
public class ElementNode {
    protected final String name;
    protected final HitboxNode hitboxNode;

    public ElementNode(final XmlReader.Element data) {
        this.name = data.getAttribute("name");

        final XmlReader.Element hitboxNode = data.getChildByName("hitbox");
        this.hitboxNode = new HitboxNode(hitboxNode);
    }

    public ElementInfos parse() {
        final ElementInfos element = new ElementInfos();
        element.name(name);
        element.hitbox(hitboxNode.parse());
        return element;
    }
}