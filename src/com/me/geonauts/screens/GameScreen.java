/**
 * 
 */
package com.me.geonauts.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.me.geonauts.Geonauts;
import com.me.geonauts.controller.HeroController;
import com.me.geonauts.model.World;
import com.me.geonauts.view.WorldRenderer;

/**
 * @author joel
 *
 */
public class GameScreen implements Screen, InputProcessor {

	private World 			world;
	private WorldRenderer 	renderer;
	//private WorldController worldController;
	private HeroController	heroController;

	// Screens 
	private Geonauts game;
	
	// Sound
	private Music gameMusicOgg;
	
	private int width, height;

	/**
	 * Create a new game to play!
	 * @param menu
	 */
	public GameScreen (Geonauts game) {
		// init
		this.game = game;
		
		// Create new Renderer and load graphic. Aim is to only load graphics once.
		renderer = new WorldRenderer(); // Renderer updates and draws enemies.
		
		//oggDrop = Gdx.audio.newSound(Gdx.files.internal("audio/40Ringz_Drop.ogg"));
		
		gameMusicOgg = Gdx.audio.newMusic(Gdx.files.internal("audio/game_music.ogg"));
		gameMusicOgg.setLooping(true);
	}
	
	/**
	 * Start a new game AKA CONSTRUCTOR
	*/
	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
		
		// Create new game objects
		world = new World(this);
		renderer.setWorld(world); // Tell the renderer about the new World.
		renderer.show();
		heroController = new HeroController(world);
			
		gameMusicOgg.play();
	}

	/**
	 * Draw everything to the screen
	*/
	@Override
	public void render(float delta) {
		//Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Only control the game if help message isn't displayed
		if (renderer.HELP_MESSAGE_TIME <= 0) {
			// Update the world, hero controller, and render the screen
			heroController.update(delta); // updates model Hero as well.
			world.update(delta);
		}
		renderer.render(delta);
	}
	
	@Override
	public void resize(int width, int height) {
		renderer.setSize(width, height);
		this.width = width;
		this.height = height;
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		gameMusicOgg.stop();
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
		Gdx.input.setInputProcessor(null);
		renderer.dispose();
	}

	// * InputProcessor methods ***************************//

	// 				KEYBOARD 
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.Z)
			heroController.flyPressed();
			
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.Z)
			heroController.flyReleased();
		
		// Check if escape is pressed to show the main menu
		if (keycode == Keys.ESCAPE) {
			toMainMenu();
		}
		
		return true;
	}
	
	/**
	 * Sets the game screen to main menu
	 */
	public void toMainMenu() {
		saveGame();
		game.setScreen(game.getMainMenuScreen());
	}
	
	public void toShopMenu() {
		saveGame();
		game.setScreen(game.getShopScreen());
	}
	
	/**
	 * Saves the game by saving highscore, money, level, stats, gear, etc.
	 */
	private void saveGame() {
		Preferences prefs = Gdx.app.getPreferences("game-prefs");
		
		// Check if new score is bigger than highscore
		int highscore = prefs.getInteger("highscore");
		if (world.score > highscore) {
			// save new highscore
			prefs.putInteger("highscore", world.score);
		}
		// Check if new distance is bigger than highdistance
		int longestDist = prefs.getInteger("highdistance");
		if (world.getDistance() > longestDist) {
			prefs.putInteger("highdistance", world.getDistance());
		}
		
		// Increase # of games played
		int games_played = prefs.getInteger("games_played");
		games_played += 1;
		prefs.putInteger("games_played", games_played);
		
		// Save money
		//int currMoney = prefs.getInteger("Money");
		prefs.putInteger("Money", world.money);
		
		prefs.flush();
		
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	
	// 				TOUCH CONTROLS
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		float x_world = x / renderer.getPPUX();
		// Touch thinks Y 0 is at top, GAME thinks y 0 is at bottom. Convert to fix.
		float y_world = WorldRenderer.CAMERA_HEIGHT - y / renderer.getPPUY();
		
		if (!Gdx.app.getType().equals(ApplicationType.Android)) {
			if (button == Input.Buttons.LEFT) {

				heroController.targetPressed(x_world, y_world);
				return true;
			} else {
				return false;
			}
		}
		
		// Touch on Left hand-side of screen
		if (x < width / 3) {
			heroController.flyPressed();
		}
		// Touch on Right hand-side of screen
		if (x > width / 3) {
			heroController.targetPressed(x_world, y_world);
		}
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (!Gdx.app.getType().equals(ApplicationType.Android))
			return false;
		// Touch on Left hand-side of screen
		if (x < width / 3) {
			heroController.flyReleased();
		}
		// Touch on Right hand-side of screen
		if (x > width / 3) {
			heroController.targetReleased();
		}
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
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
	
	public WorldRenderer getRenderer() {
		return renderer;
	}

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public HeroController getHeroController() {
		return heroController;
	}
	public World getWorld(){
		return world;
	}
}