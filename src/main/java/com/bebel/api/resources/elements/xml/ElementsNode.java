package com.bebel.api.resources.elements.xml;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.bebel.api.resources.elements.ElementsInfos;

/**
 * Objet XML representant un ensemble d'elements
 */
public class ElementsNode {
    protected final Array<ElementNode> elementsNode;

    public ElementsNode(final XmlReader.Element data) {
        final Array<XmlReader.Element> elementsNode = data.getChildrenByName("element");
        this.elementsNode = new Array<>(elementsNode.size);

        for(final XmlReader.Element elementNode : elementsNode) {
            final ElementNode element = new ElementNode(elementNode);
            this.elementsNode.add(element);
        }
    }

    public ElementsInfos parse() {
        final ElementsInfos elements = new ElementsInfos();
        for (final ElementNode elementNode : this.elementsNode) {
            elements.add(elementNode.parse());
        }
        return elements;
    }
}