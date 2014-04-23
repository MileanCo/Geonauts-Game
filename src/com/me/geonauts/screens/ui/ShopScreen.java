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


public class ShopScreen extends AbstractScreen{	
	//load preferences
	Preferences prefs = Gdx.app.getPreferences("game-prefs");
	
	//
	private static final int VALUE_RELOAD = 200;
	private static final int VALUE_ATTACK = 100;
	private static final int VALUE_HEALTH = 100;
	private static final int VALUE_MULTITARGET = 300;
	
	//calculate item costs
	private int costR = VALUE_RELOAD * (prefs.getInteger("Reload"));
	private int costA = VALUE_ATTACK * (prefs.getInteger("Attack"));
	private int costH = VALUE_HEALTH * (prefs.getInteger("Health"));
	//private int costM = 100 * (prefs.getInteger("Money"));
	private int costMT = VALUE_MULTITARGET * (prefs.getInteger("max targets"));
	
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

	private Label lblDistance;
	private Label lblScore;
	
	private TextButton btnQuit;
	
	private Skin skin;
	private Skin fancySkin;
	
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
		Table tableShop = new Table();
		Table tableOther = new Table();
		tableShop.setFillParent(true);
		//tableOther.setFillParent(true);
		
		stage.addActor(tableShop);
		//stage.addActor(tableOther);
		
		skin = new Skin(Gdx.files.internal("images/ui/default-skin.json"));
		fancySkin = new Skin(Gdx.files.internal("images/ui/fancy-skin.json"));
		
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
	
		
		// GUI SHIT
		lblRinfo = new Label("Current reload time: " + reload, skin);
		lblRcost = new Label("Cost: " + String.valueOf(costR), skin);
		btnReload = new TextButton("Upgrade Reload", styleG);
		btnReload.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Decreased reload time");
				if ( money >= costR) {
					reload++;
					money = money - costR;
					costR = reload * VALUE_RELOAD;
				}
				lblRinfo.setText("Current reload time: " + Math.pow(reload, -1));
				lblRcost.setText("Cost: " + String.valueOf(costR));
				lblMoney.setText("Money: " + money);
			}
		});
		lblAinfo = new Label("Current damage: " + attack, skin);
		lblAcost = new Label("Cost: " + String.valueOf(costA), skin);
		btnAttack = new TextButton("Upgrade Damage", styleR);
		btnAttack.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Upgrade");
				if(money >= costA){
					attack++;
					money = money - costA;
					costA = VALUE_ATTACK * costA;
				}
				lblAinfo.setText("Current damage: " + attack);
				lblAcost.setText("Cost: " + String.valueOf(costA));
				lblMoney.setText("Money: " + money);
			}
		});
		
		lblHinfo = new Label("Current health: " + health, skin);
		lblHcost = new Label("Cost: " + String.valueOf(costH), skin);
		btnHealth = new TextButton("Upgrade health", styleB);
		btnHealth.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Upgrade Health");
				if(money >= costH){
					health++;
					money = money - costH;
					costH = VALUE_HEALTH * health;
				}
				lblHinfo.setText("Current health: " + health);
				lblHcost.setText("Cost: " + String.valueOf(costH));
				lblMoney.setText("Money: " + money);
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
		btnMultiTarget = new TextButton("Upgrade Targetting", styleP);
		btnMultiTarget.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Increased max targets");
				if (money >= costMT) {
					multitarget++;
					money = money - costMT;
					costMT = VALUE_MULTITARGET * multitarget;
				}
				lblMTinfo.setText("Max Number of Targets: " + multitarget);
				lblMTcost.setText("Cost: " + String.valueOf(costMT));
				lblMoney.setText("Money: " + money);
				
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
		lblMoney = new Label("Money $" + money, fancySkin, "gold");
		lblDistance = new Label("You travelled: ", skin);
		lblScore = new Label("Your score: ", skin);
		
		tableShop.add(lblMoney);
		tableShop.row();
		tableShop.add(lblDistance);
		tableShop.row();
		tableShop.add(lblScore);
		tableShop.row();
		
		tableShop.add(btnReload);
		tableShop.add(lblRinfo);
		tableShop.add(lblRcost);
		tableShop.row();
		tableShop.add(btnAttack);
		tableShop.add(lblAinfo);
		tableShop.add(lblAcost);
		tableShop.row();
		tableShop.add(btnHealth);
		tableShop.add(lblHinfo);
		tableShop.add(lblHcost);
		tableShop.row();
		/**
		table.add(btnMoney);
		table.add(lblMinfo);
		table.add(btnMcost);
		table.row();
		*/
		tableShop.add(btnMultiTarget);
		tableShop.add(lblMTinfo);
		tableShop.add(lblMTcost);
		tableShop.row();
		tableShop.add(btnQuit);
	}
	
	public void render(float delta) {
		super.render(delta);
	}

	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	public void show() {
		Gdx.input.setInputProcessor(stage);
		// Update stage with content from GameScreen
		lblMoney.setText("Money $" + money);
		lblDistance.setText("You travelled " + game.getGameScreen().getWorld().getDistance() + "m");
		lblScore.setText("Your score was " + game.getGameScreen().getWorld().getScore());
		
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
