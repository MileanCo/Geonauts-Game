package com.me.geonauts.screens.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class ShopScreen extends AbstractScreen{

	private Screen MainMenuScreen;
	
	//buttons
	private TextButton btnReload;
	private TextButton btnRinfo;
	private TextButton btnRcost;
	private TextButton btnAttack;
	private TextButton btnAinfo;
	private TextButton btnAcost;
	private TextButton btnHealth;
	private TextButton btnHinfo;
	private TextButton btnHcost;
	private TextButton btnMoney;
	private TextButton btnMinfo;
	private TextButton btnMcost;
	private TextButton btnMultiTarget;
	private TextButton btnMTinfo;
	private TextButton btnMTcost;
	private TextButton btnQuit;
	
	//load preferences
	Preferences prefs = Gdx.app.getPreferences("game-prefs");
	
	//player values
	int reload = prefs.getInteger("Reload");
	int attack = prefs.getInteger("Attack");
	int health = prefs.getInteger("Health");
	int moneyx = prefs.getInteger("Money");
	int multitarget = prefs.getInteger("Multi-Target");
	
	
	public ShopScreen(Game game){
		super(game);
		
		
		//Table
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		Skin skin = new Skin();
		
		// TextureRegions
		TextureRegion upR = new TextureRegion(new Texture(Gdx.files.internal("images/ui/red.png")));
		TextureRegion upB = new TextureRegion(new Texture(Gdx.files.internal("images/ui/blue.png")));
		TextureRegion down = new TextureRegion(new Texture(Gdx.files.internal("images/ui/black.png")));
		TextureRegion upG = new TextureRegion(new Texture(Gdx.files.internal("images/ui/green.png")));
		TextureRegion upP = new TextureRegion(new Texture(Gdx.files.internal("images/ui/purple.png")));
		TextureRegion upY = new TextureRegion(new Texture(Gdx.files.internal("images/ui/yellow.png")));

		// Styles
		TextButtonStyle styleR = new TextButtonStyle();
		styleR.up = new TextureRegionDrawable(upR);
		styleR.down = new TextureRegionDrawable(down);
		styleR.font = new BitmapFont();
		
		TextButtonStyle styleB = new TextButtonStyle();
		styleB.up = new TextureRegionDrawable(upB);
		styleB.down = new TextureRegionDrawable(down);
		styleB.font = new BitmapFont();
		
		TextButtonStyle styleG = new TextButtonStyle();
		styleG.up = new TextureRegionDrawable(upG);
		styleG.down = new TextureRegionDrawable(down);
		styleG.font = new BitmapFont();
		
		TextButtonStyle styleP = new TextButtonStyle();
		styleP.up = new TextureRegionDrawable(upP);
		styleP.down = new TextureRegionDrawable(down);
		styleP.font = new BitmapFont();
		
		TextButtonStyle styleY = new TextButtonStyle();
		styleY.up = new TextureRegionDrawable(upY);
		styleY.down = new TextureRegionDrawable(down);
		styleY.font = new BitmapFont();
		
		TextButtonStyle styleQ = new TextButtonStyle();
		styleQ.up = new TextureRegionDrawable(down);
		styleQ.down = new TextureRegionDrawable(down);
		styleQ.font = new BitmapFont();
		
		
		//Buttons
		btnReload = new TextButton("Reload", styleG);
		btnRinfo = new TextButton("Attack Speed Increase", styleG);
		btnRcost = new TextButton("*Cost*", styleG);
		btnRcost.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				reload++;
			}
		});
		btnAttack = new TextButton("Attack", styleR);
		btnAinfo = new TextButton("Attack Damage Increase", styleR);
		btnAcost = new TextButton("*Cost*", styleR);
		btnAttack.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				attack++;
			}
		});
		btnHealth = new TextButton("Health", styleB);
		btnHinfo = new TextButton("Health Increase", styleB);
		btnHcost = new TextButton("*Cost*", styleB);
		btnHealth.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				health++;
			}
		});
		btnMoney = new TextButton("Money", styleY);
		btnMinfo = new TextButton("Money Increase", styleY);
		btnMcost = new TextButton("*Cost*", styleY);
		btnMoney.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				moneyx++;
			}
		});
		btnMultiTarget = new TextButton("Multi-Target", styleP);
		btnMTinfo = new TextButton("Increase Number of Targets", styleP);
		btnMTcost = new TextButton("*Cost*", styleP);
		btnMultiTarget.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				multitarget++;
			}
		});
		btnQuit = new TextButton("Leave Shop", styleQ);
		btnQuit.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				quit();
			}
		});
		
		table.add(btnReload);
		table.add(btnRinfo);
		table.add(btnRcost);
		table.row();
		table.add(btnAttack);
		table.add(btnAinfo);
		table.add(btnAcost);
		table.row();
		table.add(btnHealth);
		table.add(btnHinfo);
		table.add(btnHcost);
		table.row();
		table.add(btnMoney);
		table.add(btnMinfo);
		table.add(btnMcost);
		table.row();
		table.add(btnMultiTarget);
		table.add(btnMTinfo);
		table.add(btnMTcost);
		table.row();
		table.add(btnQuit);
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
	
	public void quit(){
		prefs.putInteger("Reload", reload);
		prefs.putInteger("Attack", attack);
		prefs.putInteger("Health", health);
		prefs.putInteger("Moneyx", moneyx);
		prefs.putInteger("Multi-Target", multitarget);
		MainMenuScreen = new MainMenuScreen(game);
		game.setScreen(MainMenuScreen);
	}

}
