package com.bebel.api.actions.temporal;

import com.bebel.api.elements.basique.predicats.AbstractElement;

/**
 * Action se bindant à une cible
 */
public abstract class BindAction<TARGET extends AbstractElement> extends TemporalAction<TARGET> {
    @Override
    public boolean execute(float delta) {
        if (!finish && target != null) target.bindAction(this);
        return finish;
    }

    public abstract String type();
}
