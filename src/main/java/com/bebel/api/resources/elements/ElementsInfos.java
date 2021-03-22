package com.bebel.api.resources.elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Represente un ensemble d'informations trié par le nom de leur element
 */
public class ElementsInfos {
    protected final Map<String, ElementInfos> elements = new HashMap<>();
    public void add(final ElementInfos element) {
        elements.put(element.name, element);
    }

    public ElementInfos get(final String name) {return elements.get(name);}
}