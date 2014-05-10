package com.me.geonauts;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.me.geonauts.model.ActionResolver;
import com.me.geonauts.screens.GameScreen;
import com.me.geonauts.screens.ui.CreditScreen;
import com.me.geonauts.screens.ui.EndGameScreen;
import com.me.geonauts.screens.ui.MainMenuScreen;
import com.me.geonauts.screens.ui.OptionsScreen;
import com.me.geonauts.screens.ui.ShopScreen;
import com.me.geonauts.screens.ui.TutorialScreen;

public class Geonauts extends Game {

	private GameScreen gameScreen;
	private MainMenuScreen mainMenu;
	private ShopScreen shop;
	private CreditScreen credits;
	private OptionsScreen options;
	private TutorialScreen tutorial;
	private EndGameScreen endGame;
	
	public Music shopMusicOgg;
	public Music menuMusicOgg;
	public Music gameMusicOgg;
	
	private ActionResolver actionResolver;
	
	public Geonauts (ActionResolver aResolver){
		actionResolver = aResolver;
		
		actionResolver.Login();
	}


	@Override
	public void create() {
		// Create all the screens
		gameScreen = new GameScreen(this);
		mainMenu = new MainMenuScreen(this);
		shop = new ShopScreen(this);
		options = new OptionsScreen(this);
		credits = new CreditScreen(this);
		tutorial = new TutorialScreen(this);
		endGame = new EndGameScreen(this);
		
		// AUDIO
		//oggShop = Gdx.audio.newSound(Gdx.files.internal("audio/shop_music.ogg"));
		shopMusicOgg = Gdx.audio.newMusic(Gdx.files.internal("audio/shop_music.ogg"));
		shopMusicOgg.setLooping(true);
		
		//oggIntro = Gdx.audio.newSound(Gdx.files.internal("audio/40Ringz_Intro.ogg"));
		menuMusicOgg = Gdx.audio.newMusic(Gdx.files.internal("audio/main_menu.ogg"));
		menuMusicOgg.setLooping(true);
		
		gameMusicOgg = Gdx.audio.newMusic(Gdx.files.internal("audio/game_music.ogg"));
		gameMusicOgg.setLooping(true);
		
		setScreen(mainMenu);	
		
		
	}
	// SET SCREEN METHOD IS EXTENDED BY GAME
	

	public ActionResolver getActionResolver() {
		return actionResolver;
	}

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
	public TutorialScreen getTutorial() {
		return tutorial;
	}
	public EndGameScreen getEndScreen() {
		return endGame;
	}
}
