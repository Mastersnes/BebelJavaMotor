package com.bebel.core.tableaux;

import com.badlogic.gdx.Gdx;
import com.bebel.api.elements.basique.Element;
import com.bebel.api.pointAndClick.elements.Tableau;
import com.bebel.core.resources.Assets;

/**
 * Tableau du coffre
 */
public class Coffre extends Tableau {
    protected final Element serrure = new Element("Serrure", Assets.Coffre.SERRURE);
    protected final Element tige1 = new Element("Tige haut gauche", Assets.Coffre.TIGE);
    protected final Element tige2 = new Element("Tige haut droit", Assets.Coffre.TIGE);
    protected final Element tige3 = new Element("Tige bas gauche", Assets.Coffre.TIGE_ABIMEE);
    protected final Element tige4 = new Element("Tige bas droit", Assets.Coffre.TIGE);

    public Coffre() {super("Coffre");}

    @Override
    protected void createImpl() {
        super.createImpl();
//        background(Assets.Coffre.BACKGROUND);
        add(new Element("background", Assets.Coffre.BACKGROUND));

        add(serrure).at(330, 220, 150, 150);
        serrure.debugMe();
        add(tige1).at(256, 257, 88, 27);
        add(tige2).at(467, 258, 88, 27);

        add(tige3).at(257, 287, 88, 27);
        tige3.rotate(5);

        add(tige4).at(468, 290, 88, 27);

//        foreground(Assets.Coffre.FOREGROUND);
        add(new Element("foreground", Assets.Coffre.FOREGROUND));
    }

    @Override
    protected void makeEvents() {
        tige1.onClick(m -> showText("Cette tige semble intacte"));
        tige3.onClick(m -> goTo(Tableaux.COFFRE2)
        );
    }

    protected void showText(final String text) {
        Gdx.app.log("TEXT", text);
    }
}
