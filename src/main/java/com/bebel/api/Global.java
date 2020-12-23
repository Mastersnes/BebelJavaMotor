package com.bebel.api;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bebel.api.shaders.LightShader;

/**
 * Variabls globales utiles au bon deroulement du jeu
 */
public class Global {
    public static BebelGame game;

    public static SpriteBatch batch;
    public static ShapeRenderer shape;

    public static Input inputs;

    public static BitmapFont arialFont;
}
