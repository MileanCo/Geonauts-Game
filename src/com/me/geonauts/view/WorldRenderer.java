package com.me.geonauts.view;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.controller.EnemyController;
import com.me.geonauts.controller.MissileController;
import com.me.geonauts.model.ParallaxLayer;
import com.me.geonauts.model.World;
import com.me.geonauts.model.entities.Block;
import com.me.geonauts.model.entities.Entity;
import com.me.geonauts.model.entities.Missile;
import com.me.geonauts.model.entities.Target;
import com.me.geonauts.model.entities.anims.AbstractAnimation;
import com.me.geonauts.model.entities.anims.Explosion10;
import com.me.geonauts.model.entities.enemies.Dwain;
import com.me.geonauts.model.entities.enemies.FireMob;
import com.me.geonauts.model.entities.heroes.Hero;
import com.me.geonauts.model.entities.heroes.Sage;
import com.me.geonauts.model.enums.BlockType;


/**
 * @author joel
 *
 */
public class WorldRenderer {
	// CONSTANTS
	public static final float CAMERA_WIDTH = 20f;
	public static final float CAMERA_HEIGHT = 12f;
	/** Offset from Hero to Left-hand side of screen. */
	public static final float CAM_OFFSET = CAMERA_WIDTH / 3f - 1;
	
	public static final int WIDTH = (int) CAMERA_WIDTH;
	public static final int HEIGHT = (int) CAMERA_HEIGHT;
	
	private static final float EXPLOSION_DURATION = 0.5f;
	
	private World world;
	private OrthographicCamera cam;

	/** for debug rendering **/
	ShapeRenderer debugRenderer;

	/** Textures **/
	public HashMap<BlockType, TextureRegion> blockTextures = new HashMap<BlockType, TextureRegion>();
	public HashMap<String, TextureRegion> backgroundTextures = new HashMap<String, TextureRegion> ();
	private ParallaxBackground background;
	
	/** Animations **/
	//private Animation explosion10Anim;
	//private Animation explosion11Anim;
	
	private SpriteBatch spriteBatch;
	private boolean debug = false;
	private int width;
	private int height;
	private float ppuX;	// pixels per unit on the X axis
	private float ppuY;	// pixels per unit on the Y axis
	
	public WorldRenderer(World world) {
		this.world = world;		
		this.debug = debug;
		spriteBatch = new SpriteBatch();
		debugRenderer = new ShapeRenderer();
		loadTextures();
	}
	
	/**
	 * Sets the size of the world, or screen
	 * @param w width (int)
	 * @param h height (int)
	 */
	public void setSize (int w, int h) {
		this.width = w;
		this.height = h;
		ppuX = (float)width / CAMERA_WIDTH;
		ppuY = (float)height / CAMERA_HEIGHT;
		cam = new OrthographicCamera((float)w, (float)h);
		background.setSize((float)w, (float)h);
		System.out.println(w + " " + h);
		
	}
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	/**
	 * Load our textures
	 */
	private void loadTextures() {
		//Texture.setEnforcePotImages(false); // TO DO: remove this when using TextureAtlases
		
		// Load all Atlases
		TextureAtlas enemiesAtlas = new TextureAtlas(Gdx.files.internal("images/textures/enemies/enemies.pack"));
		TextureAtlas explosion_10Atlas = new TextureAtlas(Gdx.files.internal("images/textures/explosion_10/explosion_10.pack"));
		TextureAtlas explosion_11Atlas = new TextureAtlas(Gdx.files.internal("images/textures/explosion_11/explosion_11.pack"));
		TextureAtlas miscAtlas = new TextureAtlas(Gdx.files.internal("images/textures/misc/misc.pack"));
		TextureAtlas missilesAtlas = new TextureAtlas(Gdx.files.internal("images/textures/missiles/missiles.pack"));
		TextureAtlas nautsAtlas = new TextureAtlas(Gdx.files.internal("images/textures/nauts/nauts.pack"));
		TextureAtlas tilesAtlas = new TextureAtlas(Gdx.files.internal("images/textures/tiles/tiles.pack"));
		
		
		// Load background images
		Texture bg1 = new Texture(Gdx.files.internal("images/backgrounds/background01_0.png"));	
		//Texture planet_blue = new Texture(Gdx.files.internal("images/planets/planet-8.png"));	
	
		background = new ParallaxBackground(new ParallaxLayer[] {
	           new ParallaxLayer(new TextureRegion(bg1), new Vector2(0.1f, 0), new Vector2(0, 0)),
	            
	           //new ParallaxLayer(new TextureRegion(planet_blue), new Vector2(0.25f, 0), new Vector2(1000, 0)),
	           // new ParallaxLayer(backgroundAtlas.findRegion("bg3"),new Vector2(0.1f,0),new Vector2(0, Constants.HEIGHT-200), new Vector2(0, 0)),
	      }, width, height, 0.5f, this);
		
		
		
		// Load all Block Textures
		blockTextures.put(BlockType.NIGHT, tilesAtlas.findRegion("night"));
		blockTextures.put(BlockType.ROCK, tilesAtlas.findRegion("rock"));
		blockTextures.put(BlockType.GRASS, tilesAtlas.findRegion("grass"));
		
		// Load all Hero textures
		int frames = 1;
		Sage.heroFrames = new TextureRegion[frames];
		for (int i = 0; i < frames; i++) {
			Sage.heroFrames[i] = nautsAtlas.findRegion("sage0" + i);
		}
		
		// Load all Enemy textures
		Dwain.enemyFrames = new TextureRegion[frames];
		for (int i = 0; i < frames; i++) {
			Dwain.enemyFrames[i] = enemiesAtlas.findRegion("dwain0" + i);
		}
		FireMob.enemyFrames = new TextureRegion[frames];
		for (int i = 0; i < frames; i++) {
			FireMob.enemyFrames[i] = enemiesAtlas.findRegion("fire_mob0" + i);
		}
		
		// Load missile frames
		Missile.frames = new TextureRegion[1];
		Missile.frames[0] =  missilesAtlas.findRegion("laser_yellow0" + 0);
		
		// Load target frames
		Target.frames = new TextureRegion[1];
		Target.frames[0] =   miscAtlas.findRegion("target2");

		// Load animations
		// Explosion 10
		TextureRegion[] explosion_10Frames = new TextureRegion[32];
		for (int i = 0; i < 32; i++) {
			String s = "expl-0" + i;
			System.out.println(s);
			if (i < 10) {
				explosion_10Frames[i] = explosion_10Atlas.findRegion(s);
			} else {
				explosion_10Frames[i] = explosion_10Atlas.findRegion(s);
			}
			System.out.println(explosion_10Frames[i].toString());
		}
		Explosion10.anim =  new Animation(EXPLOSION_DURATION, explosion_10Frames);
		
		// explosion 11
		TextureRegion[] explosion_11Frames = new TextureRegion[24];
		for (int i = 0; i < 24; i++) {
			if (i < 10) {
				explosion_11Frames[i] = explosion_11Atlas.findRegion("expl_10_000" + i);
			} else {
				explosion_11Frames[i] = explosion_11Atlas.findRegion("expl_10_00" + i);
			}
		}
		//explosion11Anim = new Animation(EXPLOSION_DURATION, explosion_11Frames);

	}

	/**
	 * Draw all objects to the World
	 */
	public void render(float delta) {
		// Update the camera's position to hero
		cam.position.x = (world.getHero().position.cpy().x + CAM_OFFSET) * ppuX;
		cam.position.y = (CAMERA_HEIGHT / 2f) * ppuY;
		cam.update();
		
		// Set the projection matrix to the new camera matrix in the spriteBatch
		spriteBatch.setProjectionMatrix(cam.combined);
		
		// Draw the parallax scrolling background
		background.render(delta);
		
		// Draw everything to the screen
		spriteBatch.begin();
			drawChunks();
			drawHero(delta);
			
			// DRAW and UPDATE enemies in same loop for performance improvement
			for (int i = 0; i < world.getEnemyControllers().size(); i++) {
				drawUpdateEnemy(i, delta);
			}
			
			// DRAW and UPDATE MISSILES
			for (int i = 0; i < world.getMissileControllers().size(); i++ ) {
				drawUpdateMissile(i, delta);
			}
			
			// DRAW and UPDATE TARGETS
			for (int i = 0; i < world.getHero().getTargets().size(); i++) {
				drawUpdateTarget(i, delta);
			}
			
			for (int i = 0; i < world.getAnimations().size(); i++) {
				drawUpdateAnimation(i, delta);
			}

			
		spriteBatch.end();
		
		if (debug)
			drawDebug();
		
	}


	private void drawChunks() {
		// Draw current chunk
		//System.out.println(" --- c " + world.getCurrentChunk().getDrawableBlocks().size());
		for (Block block : world.getCurrentChunk().getDrawableBlocks() ) {
			drawEntity(block, blockTextures.get(block.getType()));
		}
		
		// Try to draw part of the next chunk.
		//System.out.println(" --- n " + world.getNextChunk().getDrawableBlocks().size());
		for (Block block : world.getNextChunk().getDrawableBlocks() ) {
			drawEntity(block, blockTextures.get(block.getType()));
		}
		

	}
	
	/**
	 * Draws hero in our world and updates his animation.
	 */
	private void drawHero(float delta) {
		Hero hero = world.getHero();
		

		// Get hero's current frame if he's walking
		//if (hero.getState().equals(State.MOVING)) {
		//	heroFrame = hero.isFacingLeft() ? moveLeftAnimation.getKeyFrame(hero.getStateTime(), true) : moveRightAnimation.getKeyFrame(hero.getStateTime(), true);
		
		// Get hero's frame if he's jumping or falling
		//}
		
		// Draw hero's frame in the proper position
		TextureRegion[] frames = hero.getFrames();
		drawEntity(hero, frames[0]);
		


	}
	
	/**
	 * DRAW and UPDATE enemies in same loop for performance improvement
	 * @param index: Index in the enemyControllers list.
	 * @param delta: Time since last loop
	 */
	private void drawUpdateEnemy(int index, float delta) {
		// Get objects
		EnemyController ec = world.getEnemyControllers().get(index);
		Entity e = ec.getEnemyEntity();

		// Check if enemy is off the screen
		if (e.position.x < world.getHero().getCamOffsetPosX() - e.SIZE.x) {
			world.getEnemyControllers().remove(index);
			
		// Otherwise draw and update
		} else {
			TextureRegion[] frames = ec.getFrames();
			// Update and draw objects
			ec.update(delta);
			drawEntity(e, frames[0]);
		}
	}
	
	private void drawUpdateMissile(int index, float delta) {
		// Get objects
		MissileController mc = world.getMissileControllers().get(index);
		Entity e = mc.getMissileEntity();

		// Check if missile is off the screen
		if (e.position.x < world.getHero().getCamOffsetPosX() - e.SIZE.x) {
			world.getMissileControllers().remove(index);
			
		// Otherwise draw and update
		} else {
			TextureRegion[] frames = mc.getFrames();
			// Update and draw objects
			mc.update(delta);
			drawEntity(e, frames[0]);
			
		}	
	}
	
	private void drawUpdateTarget(int i, float delta) {
		Hero hero = world.getHero();
		
		// Draw targets 
		Target t = hero.getTargets().get(i);
		
		// Check if target is past hero, then delete it
		if (hero.position.x > t.position.x || ! t.getEnemy().alive) {
			hero.getTargets().remove(i);
		} else {
			t.update(delta);
			drawEntity(t, t.getFrames()[0]);
		}
		
	}
	
	private void drawUpdateAnimation(int i, float delta) {
		AbstractAnimation a = world.getAnimations().get(i);
		a.update(delta);
		//System.out.println(a.getAnimation().toString());
		
		TextureRegion frame = a.getAnimation().getKeyFrame(a.getStateTime(), true);
		System.out.println(frame);
		drawEntity(a, frame);
	}
	

	private void drawDebug() {
		// render blocks
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Line);
		
		for (Block block : world.getCurrentChunk().getDrawableBlocks())  {
			Rectangle rect = block.getBounds();
			debugRenderer.setColor(new Color(1, 0, 0, 1));
			debugRenderer.rect(rect.x * ppuX, rect.y * ppuY, rect.width * ppuY, rect.height * ppuY);
		}
		
		// render hero
		Hero hero = world.getHero();
		Rectangle rect = hero.getBounds();
		debugRenderer.setColor(new Color(0, 1, 0, 1));
		debugRenderer.rect(rect.x * ppuX, rect.y * ppuY, rect.width * ppuY, rect.height * ppuY);
		
		
		// render enemies
		for (int i = 0; i < world.getEnemyControllers().size(); i++) {
			// Get objects
			EnemyController ec = world.getEnemyControllers().get(i);
			Entity e = ec.getEnemyEntity();
			
			Rectangle eRect = e.getBounds();
			debugRenderer.setColor(new Color(0, 0, 1, 1));
			debugRenderer.rect(eRect.x * ppuX, eRect.y * ppuY, eRect.width * ppuY, eRect.height * ppuY);
		}
		debugRenderer.end();


		
	}
	
	/**
	 * Dispose all resources in WorldRenderer
	 */
	public void dispose() {
		spriteBatch.dispose();
		//background.dispose();
	}
	
	
	/**
	 * Helper method to draw entities to the screen.
	 * @param e Entity
	 * @param frame TextureRegion
	 */
	private void drawEntity(Entity e, TextureRegion frame) {
		spriteBatch.draw(frame, e.position.x * ppuX, e.position.y * ppuY, 
				e.SIZE.x * ppuX / 2, e.SIZE.y * ppuY / 2,  
				e.SIZE.x * ppuX, e.SIZE.y * ppuY,  
				1, 1,  
				e.getAngle());
	}
	

	
	public float getPPUX() { 
		return ppuX; 
	}
	public float getPPUY() {
		return ppuY;
	}
	public World getWorld() {
		return world;
	}

}