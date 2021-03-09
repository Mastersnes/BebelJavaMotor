package com.bebel.api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bebel.api.actions.ActionManager;
import com.bebel.api.elements.basique.AbstractElement;
import com.bebel.api.elements.basique.EventableElement;
import com.bebel.api.elements.basique.GroupElement;
import com.bebel.api.events.BebelProcessor;
import org.lwjgl.opengl.Display;

import static com.bebel.api.Global.batch;

/**
 * Ecran de base de l'API Bebel
 */
public abstract class BebelScreen implements Screen, Disposable {
    protected BebelGame game;
    protected BebelProcessor input;

    protected Camera camera;
    protected Viewport viewport;

    protected GroupElement root;
    protected EventableElement focus = EventableElement.EMPTY;
    protected boolean created;

    public BebelScreen() {
        this(Display.getWidth(), Display.getHeight());
    }
    public BebelScreen(final float worldW, final float worldH) {
        setCamera(new OrthographicCamera());
        getCamera(OrthographicCamera.class)
                .setToOrtho(false, Display.getWidth(), Display.getHeight());

        viewport = new FitViewport(worldW, worldH, camera);

        game = Global.game;

        root = new GroupElement("root", Display.getWidth(), Display.getHeight());
        root.setScreen(this);
        root.create();

        input = new BebelProcessor(this, false);
    }

    /**
     * Permet de selectionner l'ecran
     */
    public void select() {
        Global.game.setScreen(this);

        if (created) return;
        created = true;
        create(); makeEvents();
    }
    protected abstract void create();
    protected abstract void makeEvents();

    @Override
    public void render(float delta) {
        input.processLoop();
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

    public void unfocus() {this.focus = EventableElement.EMPTY;}
    public void unfocus(AbstractElement focus) {if (this.focus == focus) unfocus();}
    public void focus(EventableElement focus) {
        if (focus == root) unfocus();
        else this.focus = focus;
    }
    public EventableElement focus() {
        return this.focus;
    }
    public boolean isFocus(final EventableElement layer) {
        return this.focus == layer;
    }

    public GroupElement getRoot() {
        return root;
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
        Gdx.input.setInputProcessor(input);
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
        Gdx.input.setInputProcessor(input);
    }

    public void alert(final String texte) {

    }

    public void activeClickChecker() {
        root.onClick(m -> {
            Gdx.app.log("Mouse", "[" + m.x + ", " + m.y + "]");
        });
    }

    public BebelProcessor input() {
        return input;
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        root.disposeAll();
    }
}
