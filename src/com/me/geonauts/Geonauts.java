package com.me.geonauts;

import com.badlogic.gdx.Game;
import com.me.geonauts.screens.GameScreen;
import com.me.geonauts.screens.ui.MainMenuScreen;
import com.me.geonauts.screens.ui.ShopScreen;

public class Geonauts extends Game {

	private GameScreen gameScreen;
	private MainMenuScreen mainMenu;
	private ShopScreen shop;
	
	@Override
	public void create() {
		// Create all the screens
		gameScreen = new GameScreen(this);
		mainMenu = new MainMenuScreen(this);
		shop = new ShopScreen(this);
		
		setScreen(mainMenu);	
	}
	// SET SCREEN METHOD IS EXTENDED BY GAME
	
	public MainMenuScreen getMainMenuScreen() {
		return mainMenu;
	}
	public ShopScreen getShopScreen() {
		return shop;
	}
	public GameScreen getGameScreen() {
		return gameScreen;
	}
}
