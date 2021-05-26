package com.bebel.api.actions.organisation;

import com.bebel.api.actions.AutomatedAction;

/**
 * Action executant une sequence d'action
 */
public class SequenceAction extends ParallelAction {
    protected int cursor;

    @Override
    protected void begin() {
        super.begin();
    }

    @Override
    public boolean act(float delta) {
        if (cursor >= actions.size()) return true;
        final AutomatedAction current = actions.get(cursor);
        current.execute(delta);
        if (current.isFinish()) next();
        return cursor >= actions.size();
    }

    @Override
    public void next() {
        cursor++;
        if (cursor >= actions.size()) finish = true;
    }

    @Override
    public void restart() {
        cursor = 0;
        super.restart();
    }

    @Override
    public void reset() {
        super.reset(); cursor = 0;
    }
}
