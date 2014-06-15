package com.me.geonauts.screens.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.geonauts.Geonauts;

public class CreditScreen extends AbstractScreen{
	private TextButton btnCredit;
	private Table table;
	private Label lblInfo;
	private TextButton btnGoBack;
	private TextureAtlas uiAtlas;
	private TextButtonStyle style;
	
	private final String CREDITS = "Lead programmer: Joel Stenkvist \n"
			+ "Game Design & Dev: William Jamar and Joel\n"
			+ "Art by OpenGameArt.org, Joel, and Tristan Gaskins\nMusic by 40Ringz\n"
			+ "\nPowered by libGDX";
	
	public CreditScreen(Geonauts game) {
		super(game);
		
		
		
		//Table
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		Skin skin = new Skin(Gdx.files.internal("images/ui/default-skin.json"));
		Skin fancySkin = new Skin(Gdx.files.internal("images/ui/fancy-skin.json"));
		
		uiAtlas = new TextureAtlas(Gdx.files.internal("images/ui/ui.pack"));
		// TextureRegions
		TextureRegion upRegion = uiAtlas.findRegion("buttonNormal");
		TextureRegion downRegion = uiAtlas.findRegion("buttonPressed");

		// Styles
		style = new TextButtonStyle();
		style.up = new TextureRegionDrawable(upRegion);
		style.down = new TextureRegionDrawable(downRegion);
		style.font = new BitmapFont(
				Gdx.files.internal("fonts/regular.fnt"),
				Gdx.files.internal("fonts/regular.png"), false);	
		
		
		btnGoBack = new TextButton("Return", style);
		btnGoBack.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				goToMenu();			
			}
		});
		
		lblInfo = new Label(CREDITS, skin, "red");
		
		table.add(lblInfo);
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
		
		//table.setBackground(background);
		
	}

	private void goToMenu() {
		game.setScreen(game.getMainMenuScreen());
	}
}
