package com.bebel.html;

import com.bebel.core.BebelMotor;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class BebelMotorHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new BebelMotor();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
