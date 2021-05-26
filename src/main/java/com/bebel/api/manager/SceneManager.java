package com.bebel.api.manager;

/**
 * Manager permettant de passer d'une scene à une autre
 */
public class SceneManager {
//    protected static SceneManager instance;
//    protected BebelScene currentScene;
//
//    protected SceneManager(){}
//    public static SceneManager getInstance() {
//        if (instance == null) instance = new SceneManager();
//        return instance;
//    }
//
//    public void select(final BebelScene scene) {scene.select();}
//    public void changeTo(final BebelScene newScene) {
//        if (this.currentScene != null) {
//            final BebelScene oldScene = this.currentScene;
//            this.currentScene = newScene;
//            root.addBefore(oldScene, newScene).alpha(1);
//            ActionManager.newSequence(
//                    ActionManager.fadeOut(oldScene, 1, Interpolation.linear),
//                    ActionManager.run(()-> {
//                        root.remove(oldScene);
//                        oldScene.setScreen(null);
//                    })
//            );
//        } else this.currentScene = root.add(newScene);
//        this.currentScene.setScreen(this);
//    }
}
