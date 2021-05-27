package com.bebel.api;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.bebel.api.elements.complex.BebelScene;
import com.bebel.api.resources.ResourceManager;
import org.lwjgl.opengl.Display;

import java.util.Arrays;

/**
 * Classe de base de l'API Bebel
 */
public abstract class BebelGame extends Game implements Disposable {
    public BebelGame() {this(1);}
    public BebelGame(final float scale) {Global.scale = scale;}

    public void create() {create(Display.getWidth(), Display.getHeight());}
    public void create(final float worldW, final float worldH) {
        Global.game = this;
        Global.batch = new SpriteBatch();
        Global.shape = new ShapeRenderer();
        Global.arialFont = new BitmapFont();
        Global.inputs = Gdx.input;

        int betterRefreshRate = Arrays.stream(Gdx.graphics.getDisplayModes())
                .mapToInt(display -> display.refreshRate)
                .max().orElse(0);

        for (final Graphics.DisplayMode displayMode : Gdx.graphics.getDisplayModes()) {
            if (displayMode.width*displayMode.height < 1024*768) continue;
            if (displayMode.refreshRate < betterRefreshRate) continue;
            Global.displayModes.add(displayMode);
        }

        setScreen(new BebelScreen(worldW, worldH));
    }

    /**
     * Change la scene courante pour une autre scene
     *
     * @param newScene
     */
    public void changeScene(final BebelScene newScene) {
        if (screen instanceof BebelScreen) {
            ((BebelScreen) screen).changeScene(newScene);
        }else Gdx.app.error("GAME", "Erreur, l'ecran doit être de type BebelScreen");
    }
    public void setScreen(final BebelScreen screen) {
        super.setScreen(screen);
        this.screen = screen;
        if (screen.root.scene() != null) {
            screen.root.scene().setScreen(screen);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        Global.batch.dispose();
        ResourceManager.getInstance().dispose();
    }
}
