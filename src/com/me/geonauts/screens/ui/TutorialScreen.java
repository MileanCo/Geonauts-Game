package com.me.geonauts.screens.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.geonauts.Geonauts;

public class TutorialScreen  extends AbstractScreen {
	private TextButton btnReset;
	private TextButton btnGoBack;
	
	private TextureAtlas uiAtlas;
	private TextButtonStyle style;
	
	private Image imgFly;
	private Image imgTarget;
	private Image imgWrap;
	
	private Label lblInfo;
	
	private Preferences prefs = Gdx.app.getPreferences("game-prefs");
	
	
	public TutorialScreen(Geonauts game) {
		super(game);		
		
		Skin skin = new Skin(Gdx.files.internal("images/ui/default-skin.json"));
		
		//Texture
		// Load textures
		uiAtlas = new TextureAtlas(Gdx.files.internal("images/ui/ui.pack"));
		// TextureRegions
		TextureRegion upRegion = uiAtlas.findRegion("buttonNormal");
		TextureRegion downRegion = uiAtlas.findRegion("buttonPressed");
		
		imgFly = new Image(uiAtlas.findRegion("fly"));
		imgTarget = new Image(uiAtlas.findRegion("target"));
		imgWrap = new Image(uiAtlas.findRegion("wrap"));
		
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
		
	}
		

    @Override
    public void resize( int width, int height )  {
    	super.resize(width, height);
     
    	
    	int btnWidth = uiAtlas.findRegion("buttonNormal").getRegionWidth();
    	int btnHeight = uiAtlas.findRegion("buttonNormal").getRegionHeight();
    	
    	if (height < 512) {    	
    		btnWidth /= 2f;
    		btnHeight /= 2f;
    		style.font.setScale(0.5f);
    	} else {
    		style.font.setScale(1);
    	}
    	
		
    	table.add(imgTarget);
    	table.row();
    	table.add(imgWrap);
    	
    	table.add(imgFly);
    	table.row();
		table.add(btnGoBack).width(btnWidth).height(btnHeight);//.uniform().fill();
		//table.invalidate();
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
