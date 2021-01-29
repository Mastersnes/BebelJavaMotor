package com.bebel.api.manager;

import com.bebel.api.elements.basique.GroupElement;

/**
 * Element representant une scene d'un jeu
 */
public abstract class BebelScene extends GroupElement {
    public BebelScene(String name) {super(name);}

    public void goTo(final BebelScene scene) {
        SceneManager.getInstance().goTo(scene); }
    public void goTo() {
        SceneManager.getInstance().goTo(this); }
}
