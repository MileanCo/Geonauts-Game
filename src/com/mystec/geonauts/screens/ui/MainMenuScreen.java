package com.mystec.geonauts.screens.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mystec.geonauts.Geonauts;

/**
 * Resources used: -
 * http://libdgxtutorials.blogspot.com/2013/09/libgdx-tutorial-10-button-and-stage-2d.html 
 * https://github.com/libgdx/libgdx/wiki/Scene2d.ui
 * https://github.com/libgdx/libgdx/wiki/Scene2d
 */

public class MainMenuScreen extends AbstractScreen {
	//private Screen shopScreen;
	private Screen creditScreen;
	//private Screen optionsScreen;

	// Strings for mainmenu
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	private static final String TITLE = "G E O N A U T S";

	
	// Buttons
	private TextButton btnNewGame;
	//private TextButton btnShop;
	private TextButton btnOptions;
	private TextButton btnCredits;
	private TextButton btnQuit;
	
	// Labels
	private Label lblTitle;
	private Label lblScore;
	private Label lblDistance;
	
	private Table table;
	
	private int highscore  = 0;
	private int highdistance;
	
	// Load preferences
	Preferences prefs = Gdx.app.getPreferences("game-prefs");
	
	// Sound
	private Music menuMusicOgg;
			

	public MainMenuScreen(Geonauts game) {
		super(game);
		
		//oggIntro = Gdx.audio.newSound(Gdx.files.internal("audio/40Ringz_Intro.ogg"));
		menuMusicOgg = Gdx.audio.newMusic(Gdx.files.internal("audio/main_menu.ogg"));
		menuMusicOgg.setLooping(true);
		
		highscore = prefs.getInteger("highscore");

		// Table
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		// Skins
		Skin skin = new Skin(Gdx.files.internal("images/ui/default-skin.json"));
		Skin fancySkin = new Skin(Gdx.files.internal("images/ui/fancy-skin.json"));
		
		// Load textures
		TextureAtlas uiAtlas = new TextureAtlas(Gdx.files.internal("images/ui/ui.pack"));
		// TextureRegions
		TextureRegion upRegion = uiAtlas.findRegion("buttonNormal");
		TextureRegion downRegion = uiAtlas.findRegion("buttonPressed");

		// Styles
		TextButtonStyle style = new TextButtonStyle();
		style.up = new TextureRegionDrawable(upRegion);
		style.down = new TextureRegionDrawable(downRegion);
		style.font = new BitmapFont();
		style.font.setScale(2);

		// Labels
		lblTitle = new Label(TITLE, fancySkin, "gold");
		lblScore = new Label("High Score: " + highscore, skin);
		lblDistance = new Label("Furhtest Distance: " + highdistance + " m", skin);
		
		if(prefs.getInteger("games_played") == 0){
			btnNewGame = new TextButton("New Game", style);
		} else{
			btnNewGame = new TextButton("Continue", style);
		}
		btnNewGame.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				newGame();
			}
		});
		/*btnShop = new TextButton("Shop", style);
		btnShop.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				shop();
			}
		});*/
		btnOptions = new TextButton("Options", style);
		btnOptions.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				options();
			}
		});
		btnCredits = new TextButton("Credits", style);
		btnCredits.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				credit();
			}
		});
		btnQuit = new TextButton("Quit", style);
		btnQuit.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				System.exit(0);
			}
		});	
		
		table.add(lblTitle);
		table.row();
		table.add(btnNewGame);
		//table.row();
		//table.add(btnShop);
		//table.row();
		//table.add(btnOptions);
		table.row();
		table.add(btnCredits);
		table.row();
		table.add(btnOptions);
		table.row();
		table.add(btnQuit);
		table.row();
		table.add(lblScore);
		table.row();
		table.add(lblDistance);
	}

	public void render(float delta) {
		super.render(delta);
		
		//System.out.println(highscore);
		/**
		batch.begin();
			font.draw(batch, TITLE, stage.getWidth() / 2 - FONT_SIZE * TITLE.length(), stage.getHeight() - FONT_SIZE);
		batch.end();
		*/

	}

	public void hide() {
		Gdx.input.setInputProcessor(null);
		
		//oggIntro.stop();

	}

	public void show() {
		Gdx.input.setInputProcessor(stage);
		
		// Load preferences
		Preferences prefs = Gdx.app.getPreferences("game-prefs");
		highscore = prefs.getInteger("highscore");
		highdistance = prefs.getInteger("highdistance");
		
		// update menu labels
		lblDistance.setText("Best Distance: " + highdistance + " m");
		lblScore.setText("High Score: " + highscore);
		
		//oggIntro.loop();
		if (!menuMusicOgg.isPlaying())
			menuMusicOgg.play();

	}

	private void newGame() {
		//prefs.putInteger("games_played", 0);
		if ( prefs.getInteger("games_played") == 0 || prefs.getInteger("Attack") == 0) {
			resetPrefs();
			
		} 
		menuMusicOgg.stop();
		game.setScreen(game.getGameScreen());
	}
	
	private void resetPrefs() {
		//prefs.clear();
		prefs.putInteger("Reload", 1);
		prefs.putInteger("Attack", 1);
		prefs.putInteger("Health", 1);
		prefs.putInteger("Moneyx", 1);
		prefs.putInteger("max targets", 1);
		prefs.putInteger("Money", 200);
		prefs.putInteger("total upgrades", 5);
		prefs.flush();
	}
	

	private void credit() {
		game.setScreen(game.getCreditScreen());
	}
	
	private void options() {
		game.setScreen(game.getOptions());
	}
}
