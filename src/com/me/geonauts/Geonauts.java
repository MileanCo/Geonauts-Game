package com.me.geonauts;

import com.me.geonauts.screens.ui.MainMenuScreen;
import com.badlogic.gdx.Game;

public class Geonauts extends Game {

	@Override
	public void create() {
		MainMenuScreen menu = new MainMenuScreen(this);
		setScreen(menu);	
	}

	// SET SCREEN METHOD IS EXTENDED BY GAME
}
