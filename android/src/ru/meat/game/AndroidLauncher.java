package ru.meat.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import ru.meat.game.settings.Constants;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
    config.numSamples = 2;
    Constants.MOBILE = true;
    Constants.MAIN_ZOOM = 3.5f;
		initialize(MyGame.getInstance(), config);
	}
}
