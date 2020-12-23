package com.bebel.api.pointAndClick.scenes;

import com.badlogic.gdx.math.Interpolation;
import com.bebel.api.BebelScene;
import com.bebel.api.actions.ActionManager;
import com.bebel.api.actions.AutomatedAction;
import com.bebel.api.pointAndClick.elements.Tableau;

/**
 * Scene jouant le role de manager de tableau point and click
 */
public class PointAndClick extends BebelScene {
    protected static PointAndClick instance;
    protected Tableau tableau;

    protected PointAndClick(){}
    public static PointAndClick getInstance() {
        if (instance == null) instance = new PointAndClick();
        return instance;
    }

    public void select(final Tableau tableau) {
        super.select(); goTo(tableau);
    }

    public void goTo(final Tableau newTableau) {
        if (this.tableau != null) {
            final Tableau oldTableau = this.tableau;
            this.tableau = newTableau;
            root.addBefore(oldTableau, newTableau).alpha(1);
            ActionManager.newSequence(
                    ActionManager.fadeOut(oldTableau, 1, Interpolation.linear),
                    ActionManager.run(()->root.remove(oldTableau))
            );
        } else this.tableau = root.add(newTableau);
    }

    @Override
    protected void makeEvents() {}

    @Override
    protected void create() {}
}
