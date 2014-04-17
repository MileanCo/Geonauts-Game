package com.me.geonauts.screens.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.geonauts.screens.GameScreen;

/**
 * Resources used: -
 * http://libdgxtutorials.blogspot.com/2013/09/libgdx-tutorial-
 * 10-button-and-stage-2d.html - https://github.com/libgdx/libgdx/wiki/Scene2d.ui
 * - https://github.com/libgdx/libgdx/wiki/Scene2d
 */

public class MainMenuScreen extends AbstractScreen {
	/** GameScreen object where main gameplay takes place */
	private Screen gameScreen;
	private Screen shopScreen;

	// Strings for mainmenu
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	private static final String TITLE = "G E O N A U T S";
	private int FONT_SIZE = 32;

	// Buttons
	private TextButton btnNewGame;
	private TextButton btnShop;
	private TextButton btnOptions;
	private TextButton btnCredits;
	private TextButton btnQuit;

	public MainMenuScreen(Game game) {
		super(game);

		this.font = new BitmapFont(
				Gdx.files.internal("fonts/fipps/fipps_gray.fnt"),
				Gdx.files.internal("fonts/fipps/fipps_gray.png"), false);

		// Table
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		// Skins
		Skin skin = new Skin();//Gdx.files.internal("images/ui/uiskin.json"));
		//skin.getAtlas().getTextures().iterator().next().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		// btnSkin.addRegions(buttonsAtlas);

		// TextureRegions
		TextureRegion upRegion = new TextureRegion(new Texture(Gdx.files.internal("images/ui/button_up.png")));
		TextureRegion downRegion = new TextureRegion(new Texture(Gdx.files.internal("images/ui/button_down.png")));

		// Styles
		TextButtonStyle style = new TextButtonStyle();
		style.up = new TextureRegionDrawable(upRegion);
		style.down = new TextureRegionDrawable(downRegion);
		style.font = new BitmapFont();

		File f = new File("game.dat");
		// Buttons
		if(!f.exists()){
			//make button say new game and put default values into player stats file
			btnNewGame = new TextButton("New Game", style);
			PrintWriter writer = null;
			try {
				writer = new PrintWriter("game.dat");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// THIS DOESN'T WORK ON ANDROID. It throws the above exception, and then tries to write to a 'writer' that is null....
			//health, attack, reload time, money, targets
			//writer.print("100\n25\n.5\n100\n1");
			//writer.close();
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
		btnShop = new TextButton("Shop", style);
		btnShop.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				shop();
			}
		});
		btnOptions = new TextButton("Options", style);
		btnOptions.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				//newGame();
			}
		});
		btnCredits = new TextButton("Credits", style);
		btnCredits.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				//newGame();
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
		table.add(btnNewGame);
		table.row();
		table.add(btnShop);
		table.row();
		table.add(btnOptions);
		table.row();
		table.add(btnCredits);
		table.row();
		table.add(btnQuit);

	}

	public void render(float delta) {
		super.render(delta);
		
		/**
		batch.begin();
			font.draw(batch, TITLE, stage.getWidth() / 2 - FONT_SIZE * TITLE.length(), stage.getHeight() - FONT_SIZE);
		batch.end();
		*/

	}

	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	private void newGame() {
		gameScreen = new GameScreen(game);
		game.setScreen(gameScreen);

	}
	
	private void shop(){
		shopScreen = new ShopScreen(game);
		game.setScreen(shopScreen);
	}
}
