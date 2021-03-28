package com.bebel.api.actions;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.bebel.api.actions.organisation.RepeatAction;
import com.bebel.api.contrats.Updatable;
import com.bebel.api.elements.basique.predicats.AbstractElement;
import react.Signal;

/**
 * Classe de base pour les actions automatisées
 */
public abstract class AutomatedAction<TARGET extends AbstractElement> implements Updatable, Pool.Poolable {
    public static AutomatedAction EMPTY = new AutomatedAction() {
        @Override
        protected boolean act(float delta) {
            return true;
        }
    };

    protected TARGET target;
    protected boolean began, pause, finish;
    protected Signal<Boolean> onFinish;

    public boolean execute(final float delta) {return update(delta);}
    @Override
    public final boolean update(float delta) {
        if (finish) return true;
        if (!pause) {
            if (!began) begin();
            if (act(delta)) end();
        }
        return finish;
    }

    protected void begin() {began = true;}
    protected abstract boolean act(float delta);
    public void end() {finish();}

    public void pause() {this.pause = true;}
    public void resume() {this.pause = false;}
    public void stop() {pause(); finish();}

    public boolean finish() {
        if (onFinish != null) onFinish.emit(true);
        return this.finish = true;
    }
    public boolean isFinish() {return finish;}

    public void restart() {began = finish = pause = false;}

    public void target(final TARGET target) {this.target = target;}

    public RepeatAction repeat(final int nb) {
        final RepeatAction repeatAction = ActionManager.action(RepeatAction.class);
        repeatAction.init(this, nb);
        return repeatAction;
    }

    public void onFinish(final Signal.Listener<Boolean> slot) {
        if (onFinish == null) onFinish = Signal.create();
        onFinish.connect(slot);
    }

    /**
     * Poolable
     */
    @Override
    public void reset() {
        target = null;
        began = finish = pause = false;
    }

    public void free() {Pools.free(this);}
}