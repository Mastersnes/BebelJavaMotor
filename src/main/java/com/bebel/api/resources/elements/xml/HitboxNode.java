package com.bebel.api.resources.elements.xml;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.bebel.api.resources.elements.Hitbox;

/**
 * Objet XML representant une hitbox
 */
public class HitboxNode {
    protected final Array<PolygonNode> polygonsNode;

    public HitboxNode(final XmlReader.Element data) {
        final Array<XmlReader.Element> polygonsNode = data.getChildrenByName("polygon");
        this.polygonsNode = new Array(polygonsNode.size);

        for(final XmlReader.Element polygonNode : polygonsNode) {
            this.polygonsNode.add(new PolygonNode(polygonNode));
        }
    }

    public Hitbox parse() {
        final Hitbox hitbox = new Hitbox();
        for (final PolygonNode polygonNode : polygonsNode) {
            hitbox.add(polygonNode.getPolygon());
        }
        return hitbox;
    }
}