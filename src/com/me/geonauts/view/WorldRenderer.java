package com.me.geonauts.view;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.controller.EnemyController;
import com.me.geonauts.controller.EnemyMissileController;
import com.me.geonauts.controller.MissileController;
import com.me.geonauts.model.Chunk;
import com.me.geonauts.model.ParallaxLayer;
import com.me.geonauts.model.World;
import com.me.geonauts.model.entities.Block;
import com.me.geonauts.model.entities.Entity;
import com.me.geonauts.model.entities.Target;
import com.me.geonauts.model.entities.anims.AbstractAnimation;
import com.me.geonauts.model.entities.anims.Coin;
import com.me.geonauts.model.entities.anims.Explosion06;
import com.me.geonauts.model.entities.anims.Explosion10;
import com.me.geonauts.model.entities.anims.Explosion11;
import com.me.geonauts.model.entities.anims.ExplosionHit;
import com.me.geonauts.model.entities.enemies.BlueMob;
import com.me.geonauts.model.entities.enemies.Dwain;
import com.me.geonauts.model.entities.enemies.Fiend;
import com.me.geonauts.model.entities.enemies.FireMob;
import com.me.geonauts.model.entities.heroes.Hero;
import com.me.geonauts.model.entities.heroes.Sage;
import com.me.geonauts.model.entities.missiles.GreenEnemyLaser;
import com.me.geonauts.model.entities.missiles.YellowLaser;
import com.me.geonauts.model.entities.particles.Particle;
import com.me.geonauts.model.entities.particles.ParticleLite;
import com.me.geonauts.model.entities.particles.ParticleYellow;
import com.me.geonauts.model.entities.powerups.HealthPack;
import com.me.geonauts.model.entities.powerups.Powerup;
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
	
	private static final float EXPLOSION_DURATION = 0.03f; //seconds b/t frames
	private static final float COIN_SPIN_DURATION = 0.10f;
	
	private Random randomGen = new Random();
	private World world;
	private OrthographicCamera cam;
	
	private final float CAM_SWAY_MAX = 1;
	private float CAM_SWAY = 0;
	private int CAM_SWAY_DIR = -1;

	/** for debug rendering **/
	ShapeRenderer debugRenderer;

	/** Textures **/
	public HashMap<BlockType, TextureRegion> blockTextures = new HashMap<BlockType, TextureRegion>();
	public HashMap<String, Texture> backgroundTextures = new HashMap<String, Texture> ();
	private ParallaxBackground backgroundNebula1;
	private ParallaxBackground backgroundNebula2;
	private ParallaxBackground backgroundSpace1;
	private ParallaxBackground backgroundSpace2;
	private ParallaxBackground backgroundSpace3;
	private ParallaxBackground backgroundCity;
	
	/** Animations **/
	//private Animation explosion10Anim;
	//private Animation explosion11Anim;
	
	private SpriteBatch spriteBatch;
	private boolean debug = false;
	private int width;
	private int height;
	private float ppuX;	// pixels per unit on the X axis
	private float ppuY;	// pixels per unit on the Y axis

	private int fps;
	private  boolean drawParticles = true;
	private int HERO_PARTICLE_SPAWN_THRESHOLD = 10;
	private int COIN_PARTICLE_SPAWN_THRESHOLD = 30;
	private int HERO_PARTICLE_SPAWN = 0;
	
	private int backgroundType;
	private boolean android;
	
	// Fonts
	protected BitmapFont font_fipps_small;
	protected BitmapFont font_fipps;
	protected BitmapFont font_reg;
	
	private static final int NUM_GAMES_HELP = 3;
	public float HELP_MESSAGE_TIME = 6; //seconds
	private String HELP_MESSAGE_ANDROID = "<-- TAP on LEFT to FLY\n\nTAP on ENEMIES ONCE to TARGET -- > \n\n \n\n It's a '2-thumb' game";
	private String HELP_MESSAGE_DESKTOP = "Press Z to fly\n\n Click on enemies to shoot\n";
	
	public WorldRenderer() {
		this(null);
	}
	
	public WorldRenderer(World world) {
		this.world = world;		
		this.debug = debug;
		spriteBatch = new SpriteBatch();
		debugRenderer = new ShapeRenderer();
		loadTextures();
		
		// Fonts
		this.font_fipps_small = new BitmapFont(
				Gdx.files.internal("fonts/fipps/fipps_small.fnt"),
				Gdx.files.internal("fonts/fipps/fipps_small.png"), false);
		
		this.font_fipps = new BitmapFont(
				Gdx.files.internal("fonts/fipps/fipps_big.fnt"),
				Gdx.files.internal("fonts/fipps/fipps_big.png"), false);
		
		this.font_reg = new BitmapFont(
				Gdx.files.internal("fonts/regular.fnt"),
				Gdx.files.internal("fonts/regular.png"), false);
		//this.font_fipps.setScale(0.75f);
		
		if (Gdx.app.getType().equals(ApplicationType.Android)) {
			android = true;
		}
	}

	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	private int getRandPosX() {
		return randomGen.nextInt((2048 - 0) + 0);
	}
	private int getRandPosXLowRange() {
		return randomGen.nextInt((1024 - 0) + 0);
	}
	private int getRandPosXHiRange() {
		return randomGen.nextInt((2048 - 900) + 900);
	}
	private int getRandPosY() {
		return randomGen.nextInt((512 - -60) + -60);
	}
	
	

	
	/**
	 * Load our textures
	 */
	private void loadTextures() {
		//Texture.setEnforcePotImages(false); // TO DO: remove this when using TextureAtlases
		
		// Load all Atlases
		TextureAtlas enemiesAtlas = new TextureAtlas(Gdx.files.internal("images/textures/enemies/enemies.pack"));
		TextureAtlas explosion_06Atlas = new TextureAtlas(Gdx.files.internal("images/textures/explosions/explosion_06.pack"));
		TextureAtlas explosion_10Atlas = new TextureAtlas(Gdx.files.internal("images/textures/explosions/explosion_10.pack"));
		TextureAtlas explosion_11Atlas = new TextureAtlas(Gdx.files.internal("images/textures/explosions/explosion_11.pack"));
		TextureAtlas explosionHitAtlas = new TextureAtlas(Gdx.files.internal("images/textures/explosions/hit.pack"));
		TextureAtlas coinsAtlas = new TextureAtlas(Gdx.files.internal("images/textures/misc/coins.pack"));
		TextureAtlas powerupsAtlas = new TextureAtlas(Gdx.files.internal("images/textures/misc/powerups.pack"));
		TextureAtlas miscAtlas = new TextureAtlas(Gdx.files.internal("images/textures/misc/misc.pack"));
		TextureAtlas missilesAtlas = new TextureAtlas(Gdx.files.internal("images/textures/missiles/missiles.pack"));
		TextureAtlas nautsAtlas = new TextureAtlas(Gdx.files.internal("images/textures/nauts/nauts.pack"));
		TextureAtlas tilesAtlas = new TextureAtlas(Gdx.files.internal("images/textures/tiles/tiles.pack"));
		TextureAtlas planetAtlas = new TextureAtlas(Gdx.files.internal("images/textures/planets/planets.pack"));
		
		
		// Load background images
		//backgroundTextures.put("darkblue_nebula1", new Texture(Gdx.files.internal("images/backgrounds/darkblue_nebula1.png")));
		backgroundTextures.put("stars_white",  new Texture(Gdx.files.internal("images/backgrounds/stars.png")));	
		backgroundTextures.put("stars_yellow",  new Texture(Gdx.files.internal("images/backgrounds/stars_yellow.png")));
		//Texture cityBG = new Texture(Gdx.files.internal("images/backgrounds/city_background_night.png"));
		
		
		//backgroundNebula1 = new ParallaxBackground(new ParallaxLayer[] {
	   //        new ParallaxLayer(new TextureRegion(backgroundTextures.get("darkblue_nebula1")), new Vector2(0.2f, 0), new Vector2(0, 0), true),
	    //  }, width, height, 0.5f, this);
		
		backgroundSpace1 = new ParallaxBackground(new ParallaxLayer[] {
		           new ParallaxLayer(new TextureRegion(backgroundTextures.get("stars_white")), new Vector2(0.2f, 0), new Vector2(0, 0), true),
		           //new ParallaxLayer(planetAtlas.findRegion("planet_blue"), new Vector2(0.4f, 0), new Vector2(getRandPosXHiRange(), getRandPosY()), new Vector2(2048, 0), false, true),
		           new ParallaxLayer(planetAtlas.findRegion("planet_green"), new Vector2(0.3f,0), new Vector2(getRandPosXLowRange(), getRandPosY()), new Vector2(2048, 0), false, true),
		      }, width, height, 0.5f, this);
		
		backgroundSpace2 = new ParallaxBackground(new ParallaxLayer[] {
		           new ParallaxLayer(new TextureRegion(backgroundTextures.get("stars_yellow")), new Vector2(0.22f, 0), new Vector2(0, 0), true),
		           new ParallaxLayer(planetAtlas.findRegion("planet_red"), new Vector2(0.5f, 0), new Vector2(getRandPosX(), getRandPosY()), new Vector2(2048, 0), false, true),
		           //new ParallaxLayer(planetAtlas.findRegion("planet_blue"), new Vector2(0.4f, 0), new Vector2(getRandPosX(), getRandPosY()), new Vector2(1024, 0), false, true),
		      }, width, height, 0.5f, this);
		
		backgroundSpace3 = new ParallaxBackground(new ParallaxLayer[] {
		           new ParallaxLayer(new TextureRegion(backgroundTextures.get("stars_yellow")), new Vector2(0.25f, 0), new Vector2(0, 0), true),
		           new ParallaxLayer(planetAtlas.findRegion("planet_yellow"), new Vector2(0.5f, 0), new Vector2(getRandPosX(), getRandPosY()), new Vector2(1800, 0), false, true),
		           //new ParallaxLayer(planetAtlas.findRegion("planet_blue"), new Vector2(0.4f, 0), new Vector2(getRandPosX(), getRandPosY()), new Vector2(1024, 0), false, true),		          
		      }, width, height, 0.5f, this);
		
		
		/**
		backgroundCity = new ParallaxBackground(new ParallaxLayer[] {
		           new ParallaxLayer(new TextureRegion(cityBG), new Vector2(0.8f, 0), new Vector2(0, 0), true),
		      }, width, height, 0.5f, this);
		*/
		
		
		// Load all Block Textures
		blockTextures.put(BlockType.BLANK, tilesAtlas.findRegion("blank_tile"));
		blockTextures.put(BlockType.FLOATING, tilesAtlas.findRegion("yellow_tile_floating"));
		
		blockTextures.put(BlockType.TILE_TOP, tilesAtlas.findRegion("yellow_tile_top"));
		blockTextures.put(BlockType.TILE_BOT, new TextureRegion(blockTextures.get(BlockType.TILE_TOP)));
		blockTextures.get(BlockType.TILE_BOT).flip(false, true); //flip to get bot tile
		
		blockTextures.put(BlockType.TILE_SIDE_LEFT, tilesAtlas.findRegion("yellow_tile_side_left"));
		blockTextures.put(BlockType.TILE_SIDE_RIGHT, new TextureRegion(blockTextures.get(BlockType.TILE_SIDE_LEFT)));
		blockTextures.get(BlockType.TILE_SIDE_RIGHT).flip(true, false); //flip to get bot tile
		
		
		blockTextures.put(BlockType.WALL, tilesAtlas.findRegion("yellow_tile_wall"));
		blockTextures.put(BlockType.WALL_HORIZ, tilesAtlas.findRegion("yellow_tile_wall_horiz"));
		blockTextures.put(BlockType.WALL_END_BOT, tilesAtlas.findRegion("yellow_tile_wall_bot"));
		blockTextures.put(BlockType.WALL_END_TOP, new TextureRegion(blockTextures.get(BlockType.WALL_END_BOT)));
		blockTextures.get(BlockType.WALL_END_TOP).flip(false, true); //flip to get bot tile
		
		blockTextures.put(BlockType.CORNER_RIGHT, tilesAtlas.findRegion("yellow_tile_corner"));
		blockTextures.put(BlockType.CORNER_LEFT, new TextureRegion(blockTextures.get(BlockType.CORNER_RIGHT)));
		blockTextures.get(BlockType.CORNER_LEFT).flip(true, false); //flip to get bot tile
				
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
		BlueMob.enemyFrames = new TextureRegion[frames];
		for (int i = 0; i < frames; i++) {
			BlueMob.enemyFrames[i] = enemiesAtlas.findRegion("blue_mob0" + i);
		}
		Fiend.enemyFrames = new TextureRegion[frames];
		for (int i = 0; i < frames; i++) {
			Fiend.enemyFrames[i] = enemiesAtlas.findRegion("fiend0" + i);
		}
		
		// Load powerups
		HealthPack.frame = powerupsAtlas.findRegion("health");
		
		// Load missile frames
		YellowLaser.frames = new TextureRegion[1];
		YellowLaser.frames[0] =  missilesAtlas.findRegion("laser_yellow0" + 0);
		GreenEnemyLaser.frames = new TextureRegion[1];
		GreenEnemyLaser.frames[0] =  missilesAtlas.findRegion("laser_green0" + 0);
		//Missile.frames[1] =  missilesAtlas.findRegion("laser_green0" + 0);
		
		// Load target frames
		Target.frames = new TextureRegion[1];
		Target.frames[0] =   miscAtlas.findRegion("target2");
		ParticleLite.frame = miscAtlas.findRegion("particle_lite");
		ParticleYellow.frame = miscAtlas.findRegion("particle_yellow");
		
		// Load animations
		//hit
		TextureRegion[] explosionHitFrames = new TextureRegion[8];
		for (int i = 0; i < explosionHitFrames.length; i++) {
			explosionHitFrames[i] = explosionHitAtlas.findRegion("hit", i);
		}
		ExplosionHit.anim =  new Animation(EXPLOSION_DURATION, explosionHitFrames);
		
		// Explosion 06
		TextureRegion[] explosion_06Frames = new TextureRegion[31];
		for (int i = 0; i < explosion_06Frames.length; i++) {
			explosion_06Frames[i] = explosion_06Atlas.findRegion("expl_06", i);
		}
		Explosion06.anim =  new Animation(EXPLOSION_DURATION, explosion_06Frames);
		
		// Explosion 10
		TextureRegion[] explosion_10Frames = new TextureRegion[38];
		for (int i = 0; i < explosion_10Frames.length; i++) {
			explosion_10Frames[i] = explosion_10Atlas.findRegion("expl", i);
		}
		Explosion10.anim =  new Animation(EXPLOSION_DURATION, explosion_10Frames);
		
		// explosion 11
		TextureRegion[] explosion_11Frames = new TextureRegion[30];
		for (int i = 0; i < explosion_11Frames.length; i++) {
			explosion_11Frames[i] = explosion_11Atlas.findRegion("expl_11", i);
		}
		Explosion11.anim =  new Animation(EXPLOSION_DURATION, explosion_11Frames);

		//hit
		TextureRegion[] coinFrames = new TextureRegion[9];
		for (int i = 0; i < coinFrames.length; i++) {
			coinFrames[i] = coinsAtlas.findRegion("goldCoin", i+1);
		}
		Coin.anim =  new Animation(COIN_SPIN_DURATION, coinFrames);
		//Coin.anim.setPlayMode(Animation.LOOP);
	}
	
	public void show() {
		// Check preferences
		Preferences prefs = Gdx.app.getPreferences("game-prefs");
		
		if (prefs.getInteger("games_played") <= NUM_GAMES_HELP) {
			HELP_MESSAGE_TIME = 4f; // seconds
		} else {
			HELP_MESSAGE_TIME = -1f; //-1 means off
		}
		
		/**
		for (ParallaxLayer layer : backgroundSpace1.getLayers() ) {
			layer.startPosition = new Vector2(getRandPosX(), getRandPosY());
		}
		*/
		
		backgroundType = randomGen.nextInt(4 - 0) + 0;
	}
	
	public void resize(int w, int h) {
		this.width = w;
		this.height = h;
		ppuX = (float)width / CAMERA_WIDTH;
		ppuY = (float)height / CAMERA_HEIGHT;
		cam = new OrthographicCamera((float)w, (float)h);
		
		// Set background stuff
		//backgroundNebula1.setSize((float)w, (float)h);
		backgroundSpace1.setSize((float)w, (float)h);
		backgroundSpace2.setSize((float)w, (float)h);
		backgroundSpace3.setSize((float)w, (float)h);
		//backgroundCity.setSize((float)w, (float)h);
		
		if (h < 420) {
			font_fipps_small.setScale(0.5f);
			font_fipps.setScale(0.5f);
			System.out.println("RESIZE");
		} else {
			font_fipps_small.setScale(1f);
			font_fipps.setScale(1f);
		}
	
		
	}

	/**
	 * Draw all objects to the World
	 */
	public void render(float delta) {
		// Update the camera's position to hero
		cam.position.x = (world.getHero().position.cpy().x + CAM_OFFSET + CAM_SWAY) * ppuX;
		cam.position.y = (CAMERA_HEIGHT / 2f) * ppuY;
		cam.update();
		
		fps = Gdx.graphics.getFramesPerSecond();
		
		// Enhance performance if fps drops
		// FPS above 45
		if (fps > 45) {
			drawParticles = true;
			HERO_PARTICLE_SPAWN_THRESHOLD = 8;
			COIN_PARTICLE_SPAWN_THRESHOLD = 28;
		// FPS below 45
		} else if (fps > 25  && fps < 45) {
			HERO_PARTICLE_SPAWN_THRESHOLD = 20;
			COIN_PARTICLE_SPAWN_THRESHOLD = 40;
		// FPS below 20
		} else if (fps < 25 && fps != 0) {
			drawParticles = false;
		} 
		// change cam sway
		/**
		CAM_SWAY += delta * CAM_SWAY_DIR/2f;
		if (Math.abs(CAM_SWAY) > CAM_SWAY_MAX) {
			CAM_SWAY_DIR *= -1;
		}
		*/
		
		// Set the projection matrix to the new camera matrix in the spriteBatch
		spriteBatch.setProjectionMatrix(cam.combined);
		
		// Draw the parallax scrolling background
		switch ( backgroundType ) {
			case 0:
				backgroundSpace1.render(delta);
				break;
			case 1:
				backgroundSpace2.render(delta);
				break;
			case 2:
				backgroundSpace3.render(delta);
				break;
			case 3:
				backgroundSpace3.render(delta);
					break;
				
			/** DOESN'T WORK ON ANDROID - WHITE BG
			case 4:
				backgroundCity.render(delta);
				break;
			*/
		}
		
		// Draw everything to the screen
		spriteBatch.begin();
			// Draw chunks
			drawChunk( world.getCurrentChunk() );
			drawChunk( world.getNextChunk() );
			
			drawHero(delta);
			
			// Draw & Update in SAME loop for performance improvement
			// DRAW and UPDATE enemies
			for (int i = 0; i < world.getEnemyControllers().size(); i++) {
				drawUpdateEnemy(i, delta);
			}
			
			// DRAW and UPDATE MISSILES
			for (int i = 0; i < world.getMissileControllers().size(); i++ ) {
				drawUpdateMissile(i, delta);
			}
			
			// DRAW and UPDATE ENEMY MISSILES
			for (int i = 0; i < world.getEnemyMissileControllers().size(); i++ ) {
				drawUpdateEnemyMissile(i, delta);
			}
			
			// DRAW and UPDATE TARGETS
			for (int i = 0; i < world.getHero().getTargets().size(); i++) {
				drawUpdateTarget(i, delta);
			}
			
			// DRAW and UPDATE ANIMATIONS
			for (int i = 0; i < world.getAnimations().size(); i++) {
				drawUpdateAnimation(i, delta);
			}
			
			// DRAW and UPDATE PARTICLES
			if (drawParticles) {
				for (int i = 0; i < world.getParticles().size(); i++) {
					drawUpdateParticle(i, delta);
				}
			}
	
			// DRAW and UPDATE POWERUPS
			for (int i = 0; i < world.getPowerups().size(); i++) {
				drawUpdatePowerup(i, delta);
			}
			
			
			
			
			// Draw texts
			font_fipps_small.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			font_fipps_small.draw(spriteBatch, "Score: " + world.getHero().score, 
					cam.position.x - width/2, height);
			font_fipps_small.setColor(1.0f, 0f, 0f, 1.0f);
			font_fipps_small.draw(spriteBatch, "Health: " + world.getHero().health, 
					cam.position.x - width/2, height - font_fipps_small.getLineHeight());
			
			
			// draw help message in beginning
			if (HELP_MESSAGE_TIME > 0) {
				if (android) {
					font_reg.drawMultiLine(spriteBatch, HELP_MESSAGE_ANDROID, 
							cam.position.x - width/3, height/1.5f );
				} else 
					font_reg.drawMultiLine(spriteBatch, HELP_MESSAGE_DESKTOP, 
							cam.position.x - width/3, height/1.5f );
				
				HELP_MESSAGE_TIME -= delta;
			}
			
		spriteBatch.end();
		
		if (debug)
			drawDebug();
		
	}


	private void drawChunk(Chunk c) {		
		// If the chunk's position is inside the screen
		if (c.position.x <= world.getHero().getCamOffsetPosX() + CAMERA_WIDTH ) {		
			// Get the X of the left-hand side of the screen
			int x1 = (int) (world.getHero().getCamOffsetPosX() - c.position.x) ;
			int y1 = 0; 
			if (x1 < 0) 	x1 = 0;	
			
			// get Right-hand side of screen		
			int x2 = (int) (world.getHero().getCamOffsetPosX() + WIDTH + 1 - c.position.x );
			int y2 = HEIGHT - 1;
			
			if (x2 >= Chunk.WIDTH) x2 = Chunk.WIDTH - 1;
			if (y2 >= Chunk.HEIGHT) y2 = Chunk.HEIGHT - 1;
			
			//System.out.println(x1 + " " + x2);
			
			// Make a list of all blocks within x...x2, y....y2
			Block block;
			for (int col = x1; col <= x2; col++) {
				for (int row = y1; row <= y2; row++) {
					block = c.getBlock(col, row);
					// If there's a block here, draw it
					if (block != null) {
						drawEntity(block, blockTextures.get(block.getType()));
					}
				}
			}
		}		
	}
	
	/**
	 * Draws hero in our world and updates his animation.
	 */
	private void drawHero(float delta) {
		Hero hero = world.getHero();
		if (! hero.grounded) {
			// Draw hero's frame in the proper position
			TextureRegion[] frames = hero.getFrames();
			drawEntity(hero, frames[0]);
		}
		
		// Create some particles for Hero
		//int spawn = //randomGen.nextInt(HERO_PARTICLE_SPAWN_THRESHOLD - 0) + 0;
		HERO_PARTICLE_SPAWN--;
		if (HERO_PARTICLE_SPAWN <= 0 && ! hero.grounded) {
			Vector2 pos = new Vector2(hero.position.x, hero.position.y + randomGen.nextFloat()/1.5f);
			Vector2 vel = new Vector2((randomGen.nextFloat() - 0.5f), (randomGen.nextFloat() - 0.5f));
			Particle p = new ParticleLite(pos, vel);
			world.getParticles().add(p);
			HERO_PARTICLE_SPAWN = HERO_PARTICLE_SPAWN_THRESHOLD;
		}
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
		if (e.position.x < (world.getHero().getCamOffsetPosX() - e.SIZE.x)) {
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
	
	private void drawUpdateEnemyMissile(int index, float delta) {
		// Get objects
		EnemyMissileController emc = world.getEnemyMissileControllers().get(index);
		Entity e = emc.getMissileEntity();
		
		// Check if missile is off the screen
		if (e.position.x < world.getHero().getCamOffsetPosX() - e.SIZE.x) {
			world.getEnemyMissileControllers().remove(index);
			
		// Otherwise draw and update
		} else {
			TextureRegion[] frames = emc.getFrames();
			// Update and draw objects
			emc.update(delta);
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
	
	/**
	 * Updates and Draws an animation to the screen
	 * @param i
	 * @param delta
	 */
	private void drawUpdateAnimation(int i, float delta) {
		AbstractAnimation a = world.getAnimations().get(i);
		
		// Check if animation is finished, or not alive.
		if (! a.isAlive()) {
			world.getAnimations().remove(i);
		
		// Animation still going, update
		} else {
			a.update(delta);
			drawEntity(a, a.getKeyFrame());
			
			// Dont spawn particles for coins
			if (a.isCoin() && false) {
				// Create some particles for the coin
				int spawn = randomGen.nextInt(COIN_PARTICLE_SPAWN_THRESHOLD - 0) + 0;
				if (spawn == 5) {
					Vector2 pos = new Vector2(a.position.x + a.SIZE.x/2f, a.position.y + randomGen.nextFloat()/1.5f);
					Vector2 vel = new Vector2((randomGen.nextFloat() - 0.5f)/3f, (randomGen.nextFloat() - 0.5f)/3f);
					Particle p = new ParticleYellow(pos, vel);
					world.getParticles().add(p);
				}
			}
		}
	}
	
	private void drawUpdateParticle(int i, float delta) {
		Particle p = world.getParticles().get(i);
		
		// Check if animation is finished, or not alive.
		if (p.position.x < world.getHero().getCamOffsetPosX() - Particle.SIZE.x || ! p.isAlive()) {
			world.getParticles().remove(i);
		
		// Animation still going, update
		} else {
			p.update(delta);
			drawEntity(p, p.getKeyFrame());
		}
	}
	
	private void drawUpdatePowerup(int i, float delta) {
		Powerup p = world.getPowerups().get(i);
		
		// Check if animation is finished, or not alive.
		if (p.position.x < world.getHero().getCamOffsetPosX() - p.SIZE.x || ! p.isAlive()) {
			world.getPowerups().remove(i);
		
		// Animation still going, update
		} else {
			p.update(delta);
			drawEntity(p, p.getKeyFrame());
		}
	}

	private void drawDebug() {
		// render blocks
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Line);
		
		/**
		for (Block block : world.getCurrentChunk().getDrawableBlocks())  {
			Rectangle rect = block.getBounds();
			debugRenderer.setColor(new Color(1, 0, 0, 1));
			debugRenderer.rect(rect.x * ppuX, rect.y * ppuY, rect.width * ppuY, rect.height * ppuY);
		}
		*/
		
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
	public void setWorld(World w) {
		this.world = w;
	}

}