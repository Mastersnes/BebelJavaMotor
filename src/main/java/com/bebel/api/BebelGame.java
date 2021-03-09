package com.bebel.api;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.bebel.api.resources.ResourceManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe de base de l'API Bebel
 */
public abstract class BebelGame extends Game implements Disposable {
    private Map<String, BebelScreen> screens = new HashMap<>();

    @Override
    public void create() {
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
    }

    /**
     * Ajoute un ecran à la map
     *
     * @param screenName
     */
    public void addScreen(final String screenName, final BebelScreen screen) {
        screens.put(screenName, screen);
    }

    /**
     * Change l'ecran courant par un autre ecran de la liste
     *
     * @param screenName
     */
    public void setScreen(final String screenName) {
        final Screen screen = screens.get(screenName);
        if (screen != null) {
            super.setScreen(screen);
        } else Gdx.app.error("Game", "Erreur, l'écran " + screenName + " n'existe pas");
    }

    @Override
    public void dispose() {
        super.dispose();
        Global.batch.dispose();
        ResourceManager.getInstance().dispose();
    }
}
