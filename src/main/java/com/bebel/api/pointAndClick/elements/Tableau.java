package com.bebel.api.pointAndClick.elements;

import com.bebel.api.elements.basique.GroupElement;
import com.bebel.api.pointAndClick.scenes.PointAndClick;

/**
 * Element representant un tableau point and click
 */
public abstract class Tableau extends GroupElement {
    public Tableau(String name) {super(name);}

    public void goTo(final Tableau tableau) {PointAndClick.getInstance().goTo(tableau); }
    public void goTo() {PointAndClick.getInstance().goTo(this); }
}
