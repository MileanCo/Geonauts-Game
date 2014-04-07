package com.me.geonauts.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenuScreen implements Screen, InputProcessor {
	
	
	private Game game;
	private GameScreen gameScreen;
	private int width, height;
	private BitmapFont font;
	private SpriteBatch batch;
	
	// Strings for mainmenu
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	private static final String TITLE = "Geonauts";
	
	public MainMenuScreen(Game game) {
		this.game = game;
		
		batch = new SpriteBatch();
		//font = TrueTypeFontFactory.createBitmapFont(Gdx.files.internal("font.ttf"), FONT_CHARACTERS, 12.5f, 7.5f, 1.0f, 
		//		Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

				//new BitmapFont(Gdx.files.internal("fonts/Extrude.ttf"), 
				//Gdx.files.internal("data/nameOfFont.png"), false);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		
		/**
		batch.begin();
			font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			font.draw(batch, TITLE, 25, 160);
		batch.end();
		*/	
	}
	
	@Override
	public void resize(int width, int height) {
		//renderer.setSize(width, height);
		this.width = width;
		this.height = height;
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}


	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);		
		
	}
	



	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	private void newGame() {
		gameScreen = new GameScreen(game);
		game.setScreen(gameScreen);
				
	}

	// 
	// ####################  CONTROLS ///////////////////
	// 
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		newGame();
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// Make a new game!
		newGame();
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	

}
