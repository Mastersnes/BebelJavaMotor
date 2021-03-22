package com.bebel.api.resources.elements.xml;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.XmlReader;
import com.bebel.api.utils.BebelXmlUtils;

/**
 * Objet XML representant un polygon
 */
public class PolygonNode {
    protected final Polygon polygon;

    public PolygonNode(final XmlReader.Element data) {
        float[] vertices = BebelXmlUtils.parseFloatsCSV(data.getText());
        this.polygon = new Polygon(vertices);
    }

    public Polygon getPolygon() {
        return polygon;
    }
}