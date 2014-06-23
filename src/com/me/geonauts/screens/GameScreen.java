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
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.Geonauts;
import com.me.geonauts.controller.HeroController;
import com.me.geonauts.model.World;
import com.me.geonauts.model.entities.heroes.Hero;
import com.me.geonauts.model.enums.Achievement;
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
		
	private int width, height;
	
	private final float WIDTHTOUCHRATIO = 3.0f;
	
	private Preferences prefs = Gdx.app.getPreferences("game-prefs");
	
	//private final float TOUCH_DISPLACEMENT = 4f; // pixels
	/** Number of current movement touches on the screen */
	private int numMovTouches;
	private Vector2 lastTouchStart = new Vector2();
	private Vector2 lastTouchEnd = new Vector2();
	private boolean lastTouchDragged = false;

	/**
	 * Create a new game to play!
	 * @param menu
	 */
	public GameScreen (Geonauts game) {
		// init
		this.game = game;
		
		// Create new Renderer and load graphic. Aim is to only load graphics once.
		renderer = new WorldRenderer(); // Renderer updates and draws enemies.
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
		
		boolean music = prefs.getBoolean("play-music");
		if (music) 
			game.gameMusicOgg.play();
		
		numMovTouches = 0;
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
		
		// check if hero has arrived at last touch
		//if (lastTouch.y > getHeroYPix() - TOUCH_DISPLACEMENT &&  lastTouch.y < getHeroYPix() + TOUCH_DISPLACEMENT) {
		//	this.touchUp((int)lastTouch.x, (int)lastTouch.y, 0, 0);
		//}
	}
	
	@Override
	public void resize(int width, int height) {
		renderer.resize(width, height);
		this.width = width;
		this.height = height;
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		game.gameMusicOgg.stop();
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
	
	/**
	 * Sets the game screen to main menu
	 */
	public void toMainMenu() {
		saveGame();
		game.setScreen(game.getMainMenuScreen());
	}
	
	public void toEndGame() {
		saveGame();
		
		// Set attributes in end game screen
		game.getEndScreen().setMoneyEarned(world.getHero().getMoneyEarned());
		game.getEndScreen().setDistance(world.getHero().getDistance());
		game.getEndScreen().setEnemiesKilled(world.getHero().enemiesKilled);
		game.getEndScreen().setScore(world.getHero().score);
		game.getEndScreen().setCoinsCollected(world.getHero().coinsCollected);
		
		game.setScreen(game.getEndScreen());
	}
	
	/**
	 * Saves the game by saving highscore, money, level, stats, gear, etc.
	 */
	private void saveGame() {
		Preferences prefs = Gdx.app.getPreferences("game-prefs");
		
		// Check if new score is bigger than highscore
		int highscore = prefs.getInteger("highscore");
		if (world.getHero().score > highscore) {
			// save new highscore
			prefs.putInteger("highscore", world.getHero().score);
		}
		// Check if new distance is bigger than highdistance
		int longestDist = prefs.getInteger("highdistance");
		if (world.getHero().getDistance() > longestDist) {
			prefs.putInteger("highdistance", world.getHero().getDistance());
		}
		
		// Save coins collected
		int coins_collected = prefs.getInteger("coins_collected");
		coins_collected += world.getHero().coinsCollected;
		prefs.putInteger("coins_collected", coins_collected);
		
		// Increase # of games played
		int games_played = prefs.getInteger("games_played");
		games_played += 1;
		prefs.putInteger("games_played", games_played);
		
		// Save money
		//world.getHero().money += (world.getHero().getDistance()/4); // add distance traveled to money
		prefs.putInteger("Money", world.getHero().money);
		
		prefs.putBoolean("bossMode", world.bossMode);
		
		prefs.flush();
		
		// Google Play Services stuff & achievements
		game.getActionResolver().submitScore(world.getHero().score);
		// DISTANCE
		if (world.getHero().getDistance() >= 200 && world.getHero().getDistance() < 400) {
			game.getActionResolver().unlockAchievement(Achievement.DIST_200);
		} else if (world.getHero().getDistance() >= 400) 
			game.getActionResolver().unlockAchievement(Achievement.DIST_400);
		// SCORE MORE
		if (world.getHero().score >= (15000)) {
			game.getActionResolver().unlockAchievement(Achievement.SCORE_MORE);
		}
		
		// New pilot achievement
		if (games_played <= 10) {
			game.getActionResolver().incrementAchievement(Achievement.NEW_PILOT, 1);
		}
		
		// Coin collector achievement
		if (world.getHero().coinsCollected > 0 && world.getHero().coinsCollected <= 300) {
			game.getActionResolver().incrementAchievement(Achievement.COIN_COLLECTOR, world.getHero().coinsCollected);
		}
		
		
	}
	// * InputProcessor methods ***************************//

	// 				KEYBOARD 
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.W)
			heroController.flyUpPressed();
		if (keycode == Keys.S)
			heroController.flyDownPressed();
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.W)
			heroController.flyUpReleased();
		if (keycode == Keys.S)
			heroController.flyDownReleased();
		
		// Check if escape is pressed to show the main menu
		if (keycode == Keys.ESCAPE) {
			toMainMenu();
		}
		
		return true;
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
		float heroY = getHeroYPix();
		
		if (!Gdx.app.getType().equals(ApplicationType.Android)) {
			if (button == Input.Buttons.LEFT) {

				heroController.targetPressed(x_world, y_world);
				return true;
			} else {
				return false;
			}
		}
		
		
		// Touch on Right hand-side of screen
		if (x > width / WIDTHTOUCHRATIO) {
			heroController.targetPressed(x_world, y_world);
		} else {
			// first check if heroY is too close to y for comfort
			if (Math.abs(heroY - y) < 23) return false;
			
			
			// Only move up/down by hero if is in middle of screen
			//if (heroY < height * 0.90f && heroY > (height - (height * 0.90f) )) { 
			System.out.println("touch: " + heroY);
			// Touch on Top-Left hand-side of screen
			if (y < heroY) {
				heroController.flyUpPressed();
				heroController.flyDownReleased();
				numMovTouches++;
			}
			// Touch on Bottom-Left hand-side of screen
			else if (y >= heroY) {
				heroController.flyDownPressed();
				heroController.flyUpReleased();
				numMovTouches++;
			}
			// Hero on edges of screen
			/**} else {
				 
				
				System.out.println("touch edge: " + heroY);
				// Touch on Top-Left hand-side of screen
				if (y < height / 2) {
					heroController.flyUpPressed();
					heroController.flyDownReleased();
					numMovTouches++;
				}
				// Touch on Bottom-Left hand-side of screen
				else if (y >= height / 2) {
					heroController.flyDownPressed();
					heroController.flyUpReleased();
					numMovTouches++;
				}
			}*/
		}
	
		
		
		
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (!Gdx.app.getType().equals(ApplicationType.Android))
			return false;
		
		float heroY = getHeroYPix();

		// Touch on Right hand-side of screen
		if (x > width / WIDTHTOUCHRATIO) {
			heroController.targetReleased();
		// Touch on Left-hand side
		} else {
			// THIS WORKS FOR MULTI-FINGER TOUCH
			if (numMovTouches >= 2) {
				// Touch up above hero
				if (y < heroY) 
					heroController.flyUpReleased();
				// Touch up below hero
				else if (y >= heroY) {
					heroController.flyDownReleased();
				}
			// THIS WORKS FOR 1 FINGER TOUCH AND DRAG
			} else if ( numMovTouches >= 0 && numMovTouches < 2){
				if (heroController.isFlyUpPressed()) { 
					heroController.flyUpReleased();
				} else if (heroController.isFlyDownPressed()) {
					heroController.flyDownReleased();
				}
			}
			
			numMovTouches--;
			if (numMovTouches < 0) {
				System.out.println("ERROR: NUM TOUCHES IS LESS THAN ZERO!");
				numMovTouches = 0;
			}
			
		}
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		float x_world = x / renderer.getPPUX();
		// Touch thinks Y 0 is at top, GAME thinks y 0 is at bottom. Convert to fix.
		float y_world = WorldRenderer.CAMERA_HEIGHT - y / renderer.getPPUY();
		float heroY = getHeroYPix();
		
		// New drag, record start position, 
		if (! lastTouchDragged) {
			lastTouchDragged = true;
			lastTouchStart.x = x;
			lastTouchStart.y = y;
		}
		
		// Touch on Top-Left hand-side of screen
		if (x < width / WIDTHTOUCHRATIO && y < heroY) {
			heroController.flyUpPressed();
			heroController.flyDownReleased();
		}
		// Touch on Bottom-Left hand-side of screen
		else if (x < width / WIDTHTOUCHRATIO && y >= heroY) {
			heroController.flyDownPressed();
			heroController.flyUpReleased();
		}
		lastTouchEnd.x = x;
		lastTouchEnd.y = y;
		
		return true;
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
	
	private float getHeroYPix() {
		return (WorldRenderer.CAMERA_HEIGHT - world.getHero().getCenterPosition().y) * renderer.getPPUY();
	}
	
	public WorldRenderer getWorldRenderer() {
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
	public Hero getHero() {
		return world.getHero();
	}

	public Geonauts getGame() {
		return game;
	}
	
}