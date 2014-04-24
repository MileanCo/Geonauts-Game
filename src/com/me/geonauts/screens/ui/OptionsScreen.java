package com.me.geonauts.screens.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
import com.me.geonauts.Geonauts;

public class OptionsScreen extends AbstractScreen{
	private TextButton btnReset;
	private TextButton btnDisableMusic;
	private TextButton btnGoBack;
	
	private Table table;
	private Label lblReset;
	
	private Preferences prefs = Gdx.app.getPreferences("game-prefs");
	
	
	public OptionsScreen(Geonauts game) {
		super(game);
		
		//Table
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		
		Skin skin = new Skin(Gdx.files.internal("images/ui/default-skin.json"));
		
		//Texture
		// Load textures
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
		
		//Button
		btnReset = new TextButton("Reset Game", style);
		btnReset.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				prefs.putInteger("Reload", 1);
				prefs.putInteger("Attack", 1);
				prefs.putInteger("Health", 1);
				prefs.putInteger("Moneyx", 1);
				prefs.putInteger("max targets", 1);
				prefs.putInteger("Money", 200);
				prefs.putInteger("total upgrades", 5);
				prefs.flush();
				
			}
		});
		lblReset = new Label("Reset all game upgrades", skin);
		
		btnDisableMusic = new TextButton("Disable Music", style);
		btnDisableMusic.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				boolean music = prefs.getBoolean("play-music");
				if (music)
					prefs.putBoolean("play-music", true);
				else
					prefs.putBoolean("play-music", false);
				
				prefs.flush();
				
			}
		});

		btnGoBack = new TextButton("Return", style);
		btnGoBack.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				goToMenu();			
			}
		});
		
		table.add(lblReset);
		table.row();
		table.add(btnReset);
		table.row();
		//table.add(btnDisableMusic);
		table.row();
		table.add(btnGoBack);
		
		
	}
		
		
	
	
	public void render(float delta) {
		super.render(delta);
	}

	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	public void show() {
		Gdx.input.setInputProcessor(stage);
		
	}

	private void goToMenu() {
		game.setScreen(game.getMainMenuScreen());
	}
}