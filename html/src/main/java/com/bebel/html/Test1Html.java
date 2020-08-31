package com.bebel.html;

import com.bebel.core.Test1;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class Test1Html extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new Test1();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
