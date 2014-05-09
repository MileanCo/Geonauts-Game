package com.me.geonauts.screens.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
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
import com.me.geonauts.model.entities.anims.Coin;

public class EndGameScreen  extends AbstractScreen {
	private TextButton btnGoBack;
	
	private TextureAtlas uiAtlas;
	private TextButtonStyle style;
	
	private Label lblMoney;
	private Label lblMoneyBreakdown;
	private Label lblScore;
	private Label lblDistance;
	private Label lblEnemiesKilled;
	
	private Image imgPlanetGreen;
	
	private Preferences prefs = Gdx.app.getPreferences("game-prefs");
	
	private int moneyEarned;
	

		
		
	
	public EndGameScreen(Geonauts game) {
		super(game);	
		
		Skin skin = new Skin(Gdx.files.internal("images/ui/default-skin.json"));
		Skin fancySkin = new Skin(Gdx.files.internal("images/ui/fancy-skin.json"));
		
		// Load texture atlases
		uiAtlas = new TextureAtlas(Gdx.files.internal("images/ui/ui.pack"));
		TextureAtlas planetAtlas = new TextureAtlas(Gdx.files.internal("images/textures/planets/planets.pack"));
		
		// TextureRegions
		TextureRegion upRegion = uiAtlas.findRegion("buttonNormal");
		TextureRegion downRegion = uiAtlas.findRegion("buttonPressed");
		
		imgPlanetGreen = new Image(planetAtlas.findRegion("planet_green"));
		//imgPlanetGreen.set
		//imgPlanetGreen.setHeight(planetAtlas.findRegion("planet_green").getRegionHeight());
		
		// Styles
		style = new TextButtonStyle();
		style.up = new TextureRegionDrawable(upRegion);
		style.down = new TextureRegionDrawable(downRegion);
		style.font = new BitmapFont(
				Gdx.files.internal("fonts/regular.fnt"),
				Gdx.files.internal("fonts/regular.png"), false);
		
		
		lblMoney = new Label("Money earned: ", fancySkin, "gold");
		lblMoneyBreakdown = new Label("Money from enemies: \n money from coins: ", skin);
		lblScore = new Label("Score: ", fancySkin);
		lblDistance = new Label("Distance travelled: ", fancySkin, "purple");
		lblEnemiesKilled = new Label("Enemies killed: ", fancySkin, "red");
		

		btnGoBack = new TextButton("Continue to Shop", style);
		btnGoBack.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				goToShop();			
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
    	
    	
    	
    	//stage.addActor(imgPlanetGreen);
    	//imgPlanetGreen.toBack();
    	
		table.add(lblMoney);
		table.row();
		table.add(lblMoneyBreakdown);
		table.row();
		table.add(lblEnemiesKilled); 
		//table.add(imgPlanetGreen);//.width(imgPlanetGreen.getWidth()).height(imgPlanetGreen.getHeight());
		table.row();
		table.add(lblDistance);
		table.row();
		table.add(lblScore);
    	table.row();
		table.add(btnGoBack).width(btnWidth).height(btnHeight);//.uniform().fill();
		//table.setBackground(imgPlanetGreen);
		
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
		
		boolean music = prefs.getBoolean("play-music");
		if (music) 
			game.shopMusicOgg.play();
	}

	private void goToShop() {
		game.setScreen(game.getShopScreen());
	}


	public void setDistance(int distance) {
		lblDistance.setText("Travelled " + distance + " m");
	}

	public void setMoneyEarned(int moneyEarned) {
		this.moneyEarned = moneyEarned;
		lblMoney.setText("You earned $ " + this.moneyEarned);
	}

	public void setEnemiesKilled(int enemiesKilled) {
		lblEnemiesKilled.setText("Destroyed " + enemiesKilled + " enemies");
	}

	public void setScore(int score) {
		lblScore.setText("And your score was " + score);
	}


	public void setCoinsCollected(int coinsCollected) {
		int moneyCoins = coinsCollected * Coin.value;
		int moneyEnemies = (moneyEarned - moneyCoins);
		System.out.println(moneyCoins);
		System.out.println((moneyEarned - moneyCoins));
		lblMoneyBreakdown.setText(" - Enemies: $" + moneyEnemies + "\n"
				 + " - Coins: $" + moneyCoins);
	}
}
