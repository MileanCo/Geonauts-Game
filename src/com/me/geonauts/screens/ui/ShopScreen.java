package com.me.geonauts.screens.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
import com.me.geonauts.model.entities.heroes.Hero;


public class ShopScreen extends AbstractScreen{

	//users shop values
	int reload;
	int attack;
	int health;
	int x_money;
	int targets;
	
	//buttons
	private TextButton btnReload;
	private TextButton btnAttack;
	private TextButton btnHealth;
	private TextButton btnMoney;
	private TextButton btnMultiTarget;
	
	
	public ShopScreen(Game game){
		super(game);
		
		String content;
		try {
			content = new Scanner(new File("game.dat")).next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
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
		
		
		//Buttons
		btnReload = new TextButton("Reload Time", styleG);
		btnReload.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				//action
			}
		});
		btnAttack = new TextButton("Increase Attack", styleR);
		btnAttack.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				//action
			}
		});
		btnHealth = new TextButton("Increase Max Health", styleB);
		btnHealth.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				//action
			}
		});
		btnMoney = new TextButton("Double Money", styleY);
		btnMoney.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				//action
			}
		});
		btnMultiTarget = new TextButton("Increase Multi-Target", styleP);
		btnMultiTarget.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Released");
				//action
			}
		});
		table.add(btnReload);
		table.row();
		table.add(btnAttack);
		table.row();
		table.add(btnHealth);
		table.row();
		table.add(btnMoney);
		table.row();
		table.add(btnMultiTarget);
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

}
