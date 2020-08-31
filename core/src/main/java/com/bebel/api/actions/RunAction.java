package com.bebel.api.actions;

/**
 * Action permettant d'executer du code
 */
public class RunAction extends AutomatedAction {
    protected Runnable runnable;

    @Override
    public void begin() {
        super.begin();
        new Thread(() -> {
            if (runnable != null) runnable.run();
            finish = true;
        }).start();
    }

    @Override
    public boolean act(float delta) {
        return isFinish();
    }

    public void run(final Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void reset() {
        super.reset();
        runnable = null;
    }
}
