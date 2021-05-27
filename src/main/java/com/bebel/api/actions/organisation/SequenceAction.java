package com.bebel.api.actions.organisation;

import com.bebel.api.actions.AutomatedAction;
import com.bebel.api.actions.complex.WalkAction;

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
        if (current.execute(delta)) {
            final AutomatedAction next = next();

            if (current instanceof WalkAction) {
                if (next == null || !(next instanceof WalkAction)) {
                    ((WalkAction)current).target().resumeIdle();
                }
            }

            if (next != null) next.execute(delta);
        }
        return cursor >= actions.size();
    }

    public AutomatedAction next() {
        cursor++;
        if (cursor >= actions.size()) {
            finish = true;
            return null;
        }else return actions.get(cursor);
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

    @Override
    public String toString() {
        return "SequenceAction : " + actions.toString();
    }
}
