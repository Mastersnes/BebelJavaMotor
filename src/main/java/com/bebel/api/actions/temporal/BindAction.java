package com.bebel.api.actions.temporal;

import com.bebel.api.actions.AutomatedAction;
import com.bebel.api.elements.basique.predicats.AbstractElement;

/**
 * Action se bindant à une cible
 */
public abstract class BindAction<TARGET extends AbstractElement> extends AutomatedAction<TARGET> {
    @Override
    public boolean execute(float delta) {
        if (target == null) return super.execute(delta);
        else if (!finish) target.bindAction(this);
        return finish;
    }

    public abstract String type();
}
