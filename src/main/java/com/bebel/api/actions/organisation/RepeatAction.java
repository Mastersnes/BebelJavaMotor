package com.bebel.api.actions.organisation;

import com.bebel.api.actions.AutomatedAction;

/**
 * Action executant un nombre de fois determiné une action subordonnée
 */
public class RepeatAction extends AutomatedAction {
    public static final int FOREVER = -1;
    protected AutomatedAction delegatedAction;
    protected int repeatCount, executedCount;


    public void init(final AutomatedAction action, final int nb) {
        delegatedAction = action;
        repeatCount = nb; executedCount = 0;
    }

    @Override
    protected boolean act(float delta) {
        if (executedCount == repeatCount) return true;
        if (delegatedAction.execute(delta)) {
            if (finish) return true;
            if (repeatCount > 0) executedCount++;
            if (executedCount == repeatCount) return true;
            delegatedAction.restart();
        }
        return false;
    }

    @Override
    public RepeatAction repeat(int nb) {
        this.repeatCount = nb;
        return this;
    }

    @Override
    public void stop() {
        if (delegatedAction != null) delegatedAction.stop();
        super.stop();
    }

    @Override
    public void restart() {
        executedCount = 0;
        if (delegatedAction != null) delegatedAction.restart();
        super.restart();
    }

    @Override
    public void reset() {
        super.reset();
        executedCount = 0; delegatedAction = null;
    }

    @Override
    public void free() {
        if (delegatedAction != null) delegatedAction.free();
        super.free();
    }
}
