package com.me.geonauts.screens.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
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


public class ShopScreen extends AbstractScreen{

	private Screen MainMenuScreen;
	
	//load preferences
	Preferences prefs = Gdx.app.getPreferences("game-prefs");
	
	//calculate item costs
	int costR = 100 * (prefs.getInteger("Reload"));
	int costA = 100 * (prefs.getInteger("Attack"));
	int costH = 100 * (prefs.getInteger("Health"));
	int costM = 100 * (prefs.getInteger("Money"));
	int costMT = 300 * (prefs.getInteger("max targets"));
	
	//buttons
	private TextButton btnReload;
	private Label lblRinfo;
	private Label lblRcost;
	
	private TextButton btnAttack;
	private Label lblAinfo;
	private Label lblAcost;
	
	private TextButton btnHealth;
	private Label lblHinfo;
	private Label lblHcost;
	
	private Label lblMoney;
	
	/**
	private TextButton btnMoney;
	private Label 		lblMinfo;
	private Label btnMcost;
	*/
	
	private TextButton btnMultiTarget;
	private Label 		lblMTinfo;
	private Label 		lblMTcost;
	
	private TextButton btnQuit;
	
	//player values
	int reload = prefs.getInteger("Reload");
	int attack = prefs.getInteger("Attack");
	int health = prefs.getInteger("Health");
	int moneyx = prefs.getInteger("Moneyx");
	int multitarget = prefs.getInteger("max targets");
	int money = prefs.getInteger("Money");
	
	
	public ShopScreen(Geonauts game) {
		super(game);
		
		
		//Table
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		Skin skin = new Skin(Gdx.files.internal("images/ui/default-skin.json"));
		
		// TextureRegions
		TextureAtlas uiAtlas = new TextureAtlas(Gdx.files.internal("images/ui/ui.pack"));
		TextureRegion upR = uiAtlas.findRegion("red"); //new TextureRegion(new Texture(Gdx.files.internal("images/ui/red.png")));
		TextureRegion upB = uiAtlas.findRegion("blue"); //new TextureRegion(new Texture(Gdx.files.internal("images/ui/blue.png")));
		TextureRegion down = uiAtlas.findRegion("black"); //new TextureRegion(new Texture(Gdx.files.internal("images/ui/black.png")));
		TextureRegion upG = uiAtlas.findRegion("green"); //new TextureRegion(new Texture(Gdx.files.internal("images/ui/green.png")));
		TextureRegion upP = uiAtlas.findRegion("purple"); //new TextureRegion(new Texture(Gdx.files.internal("images/ui/purple.png")));
		TextureRegion upY = uiAtlas.findRegion("yellow"); //new TextureRegion(new Texture(Gdx.files.internal("images/ui/yellow.png")));

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
		
		lblMoney = new Label("Money: " + money, skin);
		
		// GUI SHIT
		lblRinfo = new Label("Current reload time: " + reload, skin);
		lblRcost = new Label("Cost: " + String.valueOf(costR), skin);
		btnReload = new TextButton("Decrease Reload", styleG);
		lblRcost.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				if (money >= costR){
					reload++;
					money = money - costR;
				}
				lblRinfo.setText("Current reload time" + reload);
			}
		});
		lblAinfo = new Label("Current damage: " + attack, skin);
		lblAcost = new Label("Cost: " + String.valueOf(costA), skin);
		btnAttack = new TextButton("Increase Damage", styleR);
		btnAttack.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				if(money >= costA){
					attack++;
					money = money - costA;
				}
			}
		});
		
		lblHinfo = new Label("Current health: " + health, skin);
		lblHcost = new Label("Cost: " + String.valueOf(costH), skin);
		btnHealth = new TextButton("Increase Health", styleB);
		btnHealth.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				if(money >= costH){
					health++;
					money = money - costH;
				}
			}
		});
		/**
		btnMoney = new TextButton("Money", styleY);
		lblMinfo = new Label("Money Increase", skin);
		btnMcost = new TextButton(String.valueOf(costM), styleY);
		btnMoney.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				if(money >= costM){
					moneyx++;
					money = money - costM;
				}
			}
		});
		*/
		lblMTinfo = new Label("Max Number of Targets: " + multitarget, skin);
		lblMTcost = new Label("Cost: " + String.valueOf(costMT), skin);
		btnMultiTarget = new TextButton("Increase Max Targets", styleP);
		btnMultiTarget.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				if(money >= costMT){
					multitarget++;
					money = money - costMT;
				}
				
			}
		});
		btnQuit = new TextButton("Leave Shop", styleQ);
		btnQuit.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				quit();
			}
		});
		table.add(lblMoney);
		table.row();
		
		table.add(btnReload);
		table.add(lblRinfo);
		table.add(lblRcost);
		table.row();
		table.add(btnAttack);
		table.add(lblAinfo);
		table.add(lblAcost);
		table.row();
		table.add(btnHealth);
		table.add(lblHinfo);
		table.add(lblHcost);
		table.row();
		/**
		table.add(btnMoney);
		table.add(lblMinfo);
		table.add(btnMcost);
		table.row();
		*/
		table.add(btnMultiTarget);
		table.add(lblMTinfo);
		table.add(lblMTcost);
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
		prefs.putInteger("max targets", multitarget);
		prefs.putInteger("Money", money);
		prefs.flush();
	
		game.setScreen(game.getMainMenuScreen());
	}

}
