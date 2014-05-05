package com.me.geonauts.screens.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
import com.me.geonauts.Geonauts;
import com.me.geonauts.model.Achievement;


public class ShopScreen extends AbstractScreen{	
	//load preferences
	Preferences prefs = Gdx.app.getPreferences("game-prefs");
	
	//
	private static final int VALUE_RELOAD = 150;
	private static final int VALUE_ATTACK = 50;
	private static final int VALUE_HEALTH = 50;
	private static final int VALUE_MULTITARGET = 200;
	
	//calculate item costs
	private int costR;
	private int costA;
	private int costH;
	//private int costM = 100 * (prefs.getInteger("Money"));
	private int costMT;
	
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
	
	private TextureAtlas uiAtlas;
	
	//player values
	int reload;
	int attack;
	int health;
	int moneyx;
	int multitarget;
	int money;
	
	// Sound
	private Music shopMusicOgg;
	
	
	public ShopScreen(Geonauts game) {
		super(game);
		
		//oggShop = Gdx.audio.newSound(Gdx.files.internal("audio/shop_music.ogg"));
		shopMusicOgg = Gdx.audio.newMusic(Gdx.files.internal("audio/shop_music.ogg"));
		shopMusicOgg.setLooping(true);
		
		
		skin = new Skin(Gdx.files.internal("images/ui/default-skin.json"));
		fancySkin = new Skin(Gdx.files.internal("images/ui/fancy-skin.json"));
		
		// TextureRegions
		uiAtlas = new TextureAtlas(Gdx.files.internal("images/ui/ui.pack"));
		TextureRegion upDam = uiAtlas.findRegion("damage"); 
		TextureRegion upHealth = uiAtlas.findRegion("health");  
		TextureRegion down = uiAtlas.findRegion("down"); 
		TextureRegion upMultitarget = uiAtlas.findRegion("multitarget");
		TextureRegion upReload = uiAtlas.findRegion("reload");  

		// Styles
		TextButtonStyle styleDam = new TextButtonStyle();
		styleDam.up = new TextureRegionDrawable(upDam);
		styleDam.down = new TextureRegionDrawable(down);
		styleDam.font = new BitmapFont();
		
		TextButtonStyle styleHealth = new TextButtonStyle();
		styleHealth.up = new TextureRegionDrawable(upHealth);
		styleHealth.down = new TextureRegionDrawable(down);
		styleHealth.font = new BitmapFont();
		
		TextButtonStyle styleReload = new TextButtonStyle();
		styleReload.up = new TextureRegionDrawable(upReload);
		styleReload.down = new TextureRegionDrawable(down);
		styleReload.font = new BitmapFont();
		
		TextButtonStyle styleMT = new TextButtonStyle();
		styleMT.up = new TextureRegionDrawable(upMultitarget);
		styleMT.down = new TextureRegionDrawable(down);
		styleMT.font = new BitmapFont();
		
		TextButtonStyle styleDown = new TextButtonStyle();
		styleDown.up = new TextureRegionDrawable(down);
		styleDown.down = new TextureRegionDrawable(down);
		styleDown.font = new BitmapFont();
		
		// Get preferences. Updates cost, attributes (attack, damage, health, etc).
		getPreferences();
		
		// GUI SHIT
		lblRinfo = new Label("", skin);
		lblRcost = new Label("", skin);
		btnReload = new TextButton("Upgrade Reload", styleReload);
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
					updateLabels();
				}
			}
		});
		lblAinfo = new Label("", skin);
		lblAcost = new Label("", skin);
		btnAttack = new TextButton("Upgrade Damage", styleDam);
		btnAttack.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Upgrade");
				if(money >= costA){
					attack++;
					money = money - costA;
					costA = VALUE_ATTACK * attack;
					updateLabels();
				}
			}
		});
		
		lblHinfo = new Label("", skin);
		lblHcost = new Label("", skin);
		btnHealth = new TextButton("Upgrade health", styleHealth);
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
					updateLabels();
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
		lblMTinfo = new Label("", skin);
		lblMTcost = new Label("", skin);
		btnMultiTarget = new TextButton("Upgrade Targetting", styleMT);
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
					updateLabels();
				}
			}
		});
		btnQuit = new TextButton("Leave Shop", styleDown);
		btnQuit.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				quitAndSave();
			}
		});
		lblMoney = new Label("", fancySkin, "gold");
		lblDistance = new Label("", skin);
		lblScore = new Label("", skin);
	}
	
	public void render(float delta) {
		super.render(delta);
	}

	public void hide() {
		Gdx.input.setInputProcessor(null);
		
		shopMusicOgg.stop();
	}
	
    @Override
    public void resize( int width, int height )  {
    	super.resize(width, height);
     
    	// UI Buttons for upgrade should all be same size
    	int btnWidth = uiAtlas.findRegion("damage").getRegionWidth();
    	int btnHeight = uiAtlas.findRegion("damage").getRegionHeight();
    	
    	if (height < 570) {    	
    		btnWidth /= 2f;
    		btnHeight /= 2f;
    		lblMoney = new Label("", skin);
    	} else {
    		lblMoney = new Label("", fancySkin, "gold");
    	}
    	updateLabels();

    		
    	
    	table.add(lblMoney);//.height(btnHeight).center();
    	table.row();
    	table.add(lblDistance);
    	table.row();
		table.add(lblScore);
		table.row();
		
		table.add(btnReload).width(btnWidth).height(btnHeight);;
		table.add(lblRinfo);
		table.add(lblRcost);
		table.row();
		table.add(btnAttack).width(btnWidth).height(btnHeight);;
		table.add(lblAinfo);
		table.add(lblAcost);
		table.row();
		table.add(btnHealth).width(btnWidth).height(btnHeight);;
		table.add(lblHinfo);
		table.add(lblHcost);
		table.row();
		/**
		table.add(btnMoney);
		table.add(lblMinfo);
		table.add(btnMcost);
		table.row();
		*/
		table.add(btnMultiTarget).width(btnWidth).height(btnHeight);;
		table.add(lblMTinfo);
		table.add(lblMTcost);
		table.row();
		table.add(btnQuit).width(btnWidth).height(btnHeight);;
		
		table.invalidate();
    }
	
	/**
	 * Updates cost, attributes (attack, damage, health, etc.) with values from preferences
	 */
	private void getPreferences() {
		// Get current attributes
		reload = prefs.getInteger("Reload");
		attack = prefs.getInteger("Attack");
		health = prefs.getInteger("Health");
		moneyx = prefs.getInteger("Moneyx");
		multitarget = prefs.getInteger("max targets");
		money = prefs.getInteger("Money");
		
		// Recalculate costs
		costR = VALUE_RELOAD * (prefs.getInteger("Reload"));
		costA = VALUE_ATTACK * (prefs.getInteger("Attack"));
		costH = VALUE_HEALTH * (prefs.getInteger("Health"));
		//private int costM = 100 * (prefs.getInteger("Money"));
		costMT = VALUE_MULTITARGET * (prefs.getInteger("max targets"));
	}
	
	private void updateLabels() {		
		lblHinfo.setText("Current health: " + (health * 50 + 200));
		lblHcost.setText("Cost: " + costH);
		
		lblAinfo.setText("Current damage: " + (30 + attack * 10));
		lblAcost.setText("Cost: " + costA);
		
		// Calculate reloadTime
		float reloadTime;
		if (reload <= 4) 
			reloadTime = (1.2f - reload * 0.2f);
		else 
			reloadTime = (1f / reload);
		
		lblRinfo.setText("Current reload time: " + (Math.round(reloadTime*100.0)/100.0) + "s ");
		lblRcost.setText("Cost: " + costR);
		
		lblMTinfo.setText("Max Targets: " + multitarget);
		lblMTcost.setText("Cost: " + costMT);
		
		lblMoney.setText("Money $" + money);
		lblDistance.setText("You travelled " + game.getGameScreen().getWorld().getDistance() + "m");
		lblScore.setText("Your score was " + game.getGameScreen().getWorld().getScore());
	}

	public void show() {
		Gdx.input.setInputProcessor(stage);
		
		getPreferences();
		updateLabels();
		
		shopMusicOgg.play();
	}
	
	public void quitAndSave() {
		// Save prefs
		prefs.putInteger("Reload", reload);
		prefs.putInteger("Attack", attack);
		prefs.putInteger("Health", health);
		prefs.putInteger("Moneyx", moneyx);
		prefs.putInteger("max targets", multitarget);
		prefs.putInteger("Money", money);
		
		int total = reload + attack + health + multitarget;
		prefs.putInteger("total upgrades", total);
		
		// Achievements
		if (total-5 >= 15) {
			game.getActionResolver().unlockAchievement(Achievement.UPGRADE_FRENZY);
		}
		if (multitarget >= 5) {
			game.getActionResolver().unlockAchievement(Achievement.ALL_THE_TARGETS);
		}
		
		prefs.flush();
	
		game.setScreen(game.getMainMenuScreen());
	}

}
