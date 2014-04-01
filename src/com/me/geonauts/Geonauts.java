package com.me.geonauts;

import com.me.geonauts.screens.GameScreen;
import com.badlogic.gdx.Game;

public class Geonauts extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}

}
