package com.me.geonauts.screens.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.geonauts.Geonauts;

public class CreditScreen extends AbstractScreen{
	private TextButton btnCredit;
	
	public CreditScreen(Geonauts game) {
		super(game);
		
		
		
		//Table
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		//Texture
		// Load textures
		TextureAtlas uiAtlas = new TextureAtlas(Gdx.files.internal("images/ui/ui.pack"));
		
		TextureRegion upCredits = uiAtlas.findRegion("hjm-small_gas_planet"); //new TextureRegion(new Texture(Gdx.files.internal("images/ui/hjm-small_gas_planet_0.png")));
		TextureRegion downCredits = uiAtlas.findRegion("planet_down");

		//Style
		TextButtonStyle styleC = new TextButtonStyle();
		styleC.up = new TextureRegionDrawable(upCredits);
		styleC.down = new TextureRegionDrawable(downCredits);
		styleC.font = new BitmapFont();
		
		//Button
		btnCredit = new TextButton("Joel Stenkvist & William Jamar\nArt by: OpenGameArt.org", styleC);
		btnCredit.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				menu();
			}
		});
		
		table.add(btnCredit);
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

	private void menu() {
		game.setScreen(game.getMainMenuScreen());
	}
}
