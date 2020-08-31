package com.bebel.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bebel.core.Test1;

public class Test1Desktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Test";
		config.width = 800;
		config.height = 480;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		new LwjglApplication(new Test1(), config);
	}
}
