package com.bebel.api.manager;

import com.badlogic.gdx.math.Interpolation;
import com.bebel.api.BebelScreen;
import com.bebel.api.actions.ActionManager;

/**
 * Manager permettant de passer d'une scene à une autre
 */
public class SceneManager extends BebelScreen {
    protected static SceneManager instance;
    protected BebelScene scene;

    protected SceneManager(){}
    public static SceneManager getInstance() {
        if (instance == null) instance = new SceneManager();
        return instance;
    }

    public void select(final BebelScene scene) {
        super.select(); goTo(scene);
    }

    public void goTo(final BebelScene newScene) {
        if (this.scene != null) {
            final BebelScene oldScene = this.scene;
            this.scene = newScene;
            root.addBefore(oldScene, newScene).alpha(1);
            ActionManager.newSequence(
                    ActionManager.fadeOut(oldScene, 1, Interpolation.linear),
                    ActionManager.run(()->root.remove(oldScene))
            );
        } else this.scene = root.add(newScene);
    }

    @Override
    protected void makeEvents() {}

    @Override
    protected void create() {}
}
