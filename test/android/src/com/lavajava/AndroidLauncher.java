package com.lavajava;

import android.os.Bundle;

import com.lavajava.DBManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		// use antialiasing
		config.numSamples = 2;

		initialize(new LJGame(), config);

//		DBManager db = DBManager.getInstance();

//		db.configure("192.168.50.122", 3000);

//		db.login();
	}
}
