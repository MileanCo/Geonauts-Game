package com.mystec.geonauts;

import com.badlogic.gdx.Game;
import com.mystec.geonauts.screens.GameScreen;
import com.mystec.geonauts.screens.ui.CreditScreen;
import com.mystec.geonauts.screens.ui.MainMenuScreen;
import com.mystec.geonauts.screens.ui.OptionsScreen;
import com.mystec.geonauts.screens.ui.ShopScreen;

public class Geonauts extends Game {

	private GameScreen gameScreen;
	private MainMenuScreen mainMenu;
	private ShopScreen shop;
	private CreditScreen credits;
	private OptionsScreen options;


	@Override
	public void create() {
		// Create all the screens
		gameScreen = new GameScreen(this);
		mainMenu = new MainMenuScreen(this);
		shop = new ShopScreen(this);
		credits = new CreditScreen(this);
		options = new OptionsScreen(this);
		
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
	public CreditScreen getCreditScreen() {
		return credits;
	}
	public OptionsScreen getOptions() {
		return options;
	}
}
