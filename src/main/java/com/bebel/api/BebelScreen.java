package com.bebel.api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bebel.api.actions.ActionManager;
import com.bebel.api.elements.basique.predicats.GroupElement;
import com.bebel.api.elements.complex.BebelScene;
import org.lwjgl.opengl.Display;

import static com.bebel.api.Global.batch;

/**
 * Ecran de base de l'API Bebel
 */
public class BebelScreen implements Screen, Disposable {
    protected BebelGame game;

    protected Camera camera;
    protected Viewport viewport;

    protected GroupElement root;

    public BebelScreen() {
        this(Display.getWidth(), Display.getHeight());
    }
    public BebelScreen(final float worldW, final float worldH) {
        setCamera(new OrthographicCamera());
        getCamera(OrthographicCamera.class)
                .setToOrtho(false, worldW * Global.scale, worldH * Global.scale);

        viewport = new FitViewport(camera.viewportWidth, camera.viewportHeight, camera);

        game = Global.game;
        root = new GroupElement("root");
        root.size(viewport.getWorldWidth(), viewport.getWorldHeight());
        root.create();
    }

    public void changeScene(final BebelScene newScene) {
        newScene.setScreen(this);
        Gdx.input.setInputProcessor(newScene.input());

        final BebelScene oldScene = root.scene();
        if (oldScene != null) {
            root.setScene(newScene);
            root.addBefore(oldScene, newScene).alpha(1);
            ActionManager.newSequence(
                    ActionManager.fadeOut(oldScene, 1, Interpolation.linear),
                    ActionManager.run(()-> {
                        root.remove(oldScene);
                    })
            );
        }else root.setScene(root.add(newScene));
    }

    @Override
    public void render(float delta) {
        ActionManager.update(delta);
        root.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        Global.shape.setProjectionMatrix(batch.getProjectionMatrix());

        batch.begin();
        root.paint(batch);
        batch.end();
    }

    public Camera getCamera() {
        return camera;
    }
    public <CAMERA extends Camera> CAMERA getCamera(final Class<CAMERA> clazz) {
        return (CAMERA) camera;
    }

    public void setCamera(final Camera camera) {
        this.camera = camera;
    }

    public Viewport getViewport() {
        return viewport;
    }
    public <VIEWPORT extends Viewport> VIEWPORT getViewport(final Class<VIEWPORT> clazz) {
        return (VIEWPORT) viewport;
    }

    public float delta() {
        return Gdx.graphics.getDeltaTime();
    }

    @Override
    public void resize(int w, int h) {
        viewport.update(w, h, false);
        root.getShaders().stream().forEach(s -> s.resize(w, h));
    }

    @Override
    public void show() {
        if (root.scene() != null)
            Gdx.input.setInputProcessor(root.scene().input());
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void resume() {
        if (root.scene() != null)
            Gdx.input.setInputProcessor(root.scene().input());
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        root.disposeAll();
    }
}
