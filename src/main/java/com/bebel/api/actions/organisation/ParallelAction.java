package com.bebel.api.actions.organisation;

import com.bebel.api.actions.AutomatedAction;
import react.RList;

/**
 * Action executant d'autre actions de facon parallel
 */
public class ParallelAction extends AutomatedAction {
    protected RList<AutomatedAction> actions = RList.create();
    {
        actions.connect(new RList.Listener<AutomatedAction>() {
            @Override
            public void onRemove(AutomatedAction elem) {
                elem.free();
            }
        });
    }
    protected boolean complete = false;

    @Override
    public boolean act(float delta) {
        if (complete) return true;
        complete = true;
        actions.stream().forEach(action -> {
            if (!action.execute(delta)) complete = false;
        });
        return complete;
    }

    public void add(final AutomatedAction... actions) {
        for (final AutomatedAction action : actions) {
            this.actions.add(action);
        }
    }

    public void next(){}

    @Override
    public void stop() {
        actions.stream().forEach(a -> a.stop());
        complete = true;
        super.stop();
    }

    @Override
    public void restart() {
        complete = false;
        actions.stream().forEach(a -> a.restart());
        super.restart();
    }

    @Override
    public void reset() {
        complete = false; actions.clear();
        super.reset();
    }

    @Override
    public void free() {
        actions.clear(); super.free();
    }
}
