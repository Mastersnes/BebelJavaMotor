package com.bebel.api;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Variabls globales utiles au bon deroulement du jeu
 */
public class Global {
    public static boolean debugMode = false;
    public static BebelGame game;

    public static SpriteBatch batch;
    public static ShapeRenderer shape;

    public static Input inputs;
    public static List<Graphics.DisplayMode> displayModes = new ArrayList<>();
    public static float scale;

    public static BitmapFont arialFont;

    /**
     * Constantes de physics
     */
    public static final float STEP_TIME = 1f / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;
}
