package com.bebel.core.tableaux;

import com.bebel.api.elements.basique.Element;
import com.bebel.api.elements.basique.GroupElement;
import com.bebel.core.resources.Assets;

/**
 * Tableau du coffre
 */
public class Coffre2 extends Coffre {
    protected Element background;

    @Override
    protected void createImpl() {
        super.createImpl();
        removeAll();
        background = add(new Element("BACK", Assets.Coffre.BACKGROUND));
    }

    @Override
    protected void makeEvents() {
        background.onClick(m -> goTo(Tableaux.COFFRE));
    }
}
