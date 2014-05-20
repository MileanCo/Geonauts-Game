package com.me.geonauts.screens.ui;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.geonauts.Geonauts;
import com.me.geonauts.model.enums.Achievement;


public class ShopScreen extends AbstractScreen {	
	//load preferences
	Preferences prefs = Gdx.app.getPreferences("game-prefs");
	
	// Value of upgrades
	private static final int VALUE_RELOAD = 150;
	private static final int VALUE_ATTACK = 50;
	private static final int VALUE_HEALTH = 50;
	private static final int VALUE_MULTITARGET = 200;
	
	//calculate item costs
	private int costR;
	private int costA;
	private int costH;
	private int costMT;
	
	//labels
	private Label lblRinfo;
	private Label lblAinfo;
	private Label lblHinfo;
	private Label lblMoney;
	private Label lblMTinfo;
	
	
	//buttons
	private TextButton btnReload;
	private TextButton btnAttack;
	private TextButton btnHealth;
	private TextButton btnMultiTarget;
	private TextButton btnQuit;
	
	// Skins
	private Skin skin;
	private Skin fancySkin;
	
	private TextureAtlas uiAtlas;
	
	// Num ugrades bought
	int reload;
	int attack;
	int health;
	int moneyx;
	int multitarget;
	int money;
	
	
	
	public ShopScreen(Geonauts game) {
		super(game);
		
		
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
		styleDam.font = skin.getFont("default-font");
		styleDam.fontColor = skin.getColor("grey");
		
		
		TextButtonStyle styleHealth = new TextButtonStyle();
		styleHealth.up = new TextureRegionDrawable(upHealth);
		styleHealth.down = new TextureRegionDrawable(down);
		styleHealth.font = skin.getFont("default-font");
		styleHealth.fontColor = skin.getColor("grey");
		
		
		TextButtonStyle styleReload = new TextButtonStyle();
		styleReload.up = new TextureRegionDrawable(upReload);
		styleReload.down = new TextureRegionDrawable(down);
		styleReload.font = skin.getFont("default-font");
		styleReload.fontColor = skin.getColor("grey");
		
		
		TextButtonStyle styleMT = new TextButtonStyle();
		styleMT.up = new TextureRegionDrawable(upMultitarget);
		styleMT.down = new TextureRegionDrawable(down);
		styleMT.font = skin.getFont("default-font");
		styleMT.font = skin.getFont("default-font");
		styleMT.fontColor = skin.getColor("grey");
		
		TextButtonStyle styleDown = new TextButtonStyle();
		styleDown.up = new TextureRegionDrawable(down);
		styleDown.down = new TextureRegionDrawable(down);
		styleDown.font = skin.getFont("default-font");
		styleDown.font = skin.getFont("default-font");
		styleDown.fontColor = skin.getColor("grey");
		
		// Get preferences. Updates cost, attributes (attack, damage, health, etc).
		getPreferences();
		
		// GUI SHIT
		lblRinfo = new Label("", skin, "gold");
		btnReload = new TextButton("$"+costR, styleReload);
		btnReload.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Decreased reload time");
				if ( money >= costR) {
					reload++;
					money = money - costR;
					
					if (reload >= 5) 
						costR = reload * VALUE_RELOAD * 3; // make reload more expensive after 4 upgrades
					else
						costR = reload * VALUE_RELOAD;
					updateLabels();
				}
			}
		});
		lblAinfo = new Label("", skin, "red");
		btnAttack = new TextButton("$"+costA, styleDam);
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
		
		lblHinfo = new Label("", skin, "green");
		btnHealth = new TextButton("$"+costH, styleHealth);
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
		
		lblMTinfo = new Label("", skin, "purple");
		btnMultiTarget = new TextButton("$"+costMT, styleMT);
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
		btnQuit = new TextButton("Leave", styleDown);
		btnQuit.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				quitAndSave();
			}
		});
		lblMoney = new Label("", fancySkin, "gold");
	}
	
	public void render(float delta) {
		super.render(delta);
	}

	public void hide() {
		Gdx.input.setInputProcessor(null);
		
		game.shopMusicOgg.stop();
	}
	
    @Override
    public void resize( int width, int height )  {
    	super.resize(width, height);
     
    	// UI Buttons for upgrade should all be same size
    	int btnWidth = uiAtlas.findRegion("damage").getRegionWidth();
    	int btnHeight = uiAtlas.findRegion("damage").getRegionHeight();
    	
    	if (height <= 500) {    	
    		btnWidth /= 2f;
    		btnHeight /= 2f;
    		lblMoney = new Label("", skin);
    	} else if (height <= 720) {
    		btnWidth /= 1.5f;
    		btnHeight /= 1.5f;
    		lblMoney = new Label("", fancySkin, "gold");
    	} else if (height <= 1080) {
    		btnWidth /= 1.25f;
    		btnHeight /= 1.25f;
    	}
    	updateLabels();

    		
    	
    	table.add(lblMoney);//.height(btnHeight).center();
		table.row();
		
		table.add(btnReload).width(btnWidth).height(btnHeight);
		table.add(lblRinfo).left();
		table.row();
		table.add(btnAttack).width(btnWidth).height(btnHeight);
		table.add(lblAinfo).left();;
		table.row();
		table.add(btnHealth).width(btnWidth).height(btnHeight);
		table.add(lblHinfo).left();;
		table.row();
		table.add(btnMultiTarget).width(btnWidth).height(btnHeight);;
		table.add(lblMTinfo).left();;
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
		lblHinfo.setText("Health: " + (health * 50 + 200));
		btnHealth.setText("$" + costH);
		
		lblAinfo.setText("Damage: " + (30 + attack * 10));
		btnAttack.setText("$" + costA);
		
		// Calculate reloadTime

		
		lblRinfo.setText("Reload Time: " + (Math.round(game.getReloadTime(reload)*100.0)/100.0) + "s ");
		btnReload.setText("$" + costR);
		
		lblMTinfo.setText("Max Targets: " + multitarget);
		btnMultiTarget.setText("$" + costMT);
		
		lblMoney.setText("Money $" + money);
		//lblScore.setText("Your score was " + game.getGameScreen().getWorld().getHero().getScore());
	}

	public void show() {
		Gdx.input.setInputProcessor(stage);
		
		getPreferences();
		updateLabels();
		
		// Show an 50% of the time
		if ((game.randomGen.nextInt(3 - 0 ) + 0) == 0) 
			game.getActionResolver().showAd();
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
