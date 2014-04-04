package com.me.geonauts.view;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.me.geonauts.model.ParallaxLayer;
import com.me.geonauts.model.World;
import com.me.geonauts.model.entities.Block;
import com.me.geonauts.model.entities.heroes.Hero;
import com.me.geonauts.model.enums.BlockType;
import com.me.geonauts.model.enums.HeroType;


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
	
	private static final float RUNNING_FRAME_DURATION = 0.06f;
	
	private World world;
	private OrthographicCamera cam;

	/** for debug rendering **/
	ShapeRenderer debugRenderer = new ShapeRenderer();

	/** Textures **/
	//private TextureRegion heroIdleLeft;
	//private TextureRegion heroIdleRight;
	//private TextureRegion blockTexture;
	//private TextureRegion heroJumpLeft;
	public HashMap<BlockType, TextureRegion> blockTextures = new HashMap<BlockType, TextureRegion>();
	public HashMap<String, TextureRegion> backgroundTextures = new HashMap<String, TextureRegion> ();
	private HashMap<HeroType, TextureRegion> heroTextures = new HashMap<HeroType, TextureRegion> ();
	private ParallaxBackground background;
	
	
	/** Animations **/
	private Animation heroAnimation;
	
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
		Texture.setEnforcePotImages(false); // TO DO: remove this when using TextureAtlases
		
		// Load texture atlases
		//TextureAtlas backgroundAtlas = new TextureAtlas(Gdx.files.internal("images/textures/textures.pack"));
		Texture bg1 = new Texture(Gdx.files.internal("images/backgrounds/background01_0.png"));	
	
		background = new ParallaxBackground(new ParallaxLayer[] {
	            new ParallaxLayer(new TextureRegion(bg1), new Vector2(0.2f, 0), new Vector2(0, 0)),
	            
	          //  new ParallaxLayer(backgroundAtlas.findRegion("bg2"),new Vector2(1.0f,1.0f),new Vector2(0, 500)),
	           // new ParallaxLayer(backgroundAtlas.findRegion("bg3"),new Vector2(0.1f,0),new Vector2(0, Constants.HEIGHT-200), new Vector2(0, 0)),
	      }, width, height, 0.5f, this);
		
		// Load all Block Textures
		blockTextures.put(BlockType.NIGHT, new TextureRegion(new Texture(Gdx.files.internal("images/tiles/night.png"))));
		blockTextures.put(BlockType.ROCK, new TextureRegion(new Texture(Gdx.files.internal("images/tiles/rock.png"))));
		blockTextures.put(BlockType.GRASS, new TextureRegion(new Texture(Gdx.files.internal("images/tiles/grass.png"))));
		
		// Load all Hero textures
		heroTextures.put(HeroType.SAGE, new TextureRegion(new Texture(Gdx.files.internal("images/nauts/bgbattleship.png"))));
		
		
		/**
		
		bobIdleLeft = atlas.findRegion("bob-01");
		bobIdleRight = new TextureRegion(bobIdleLeft);
		bobIdleRight.flip(true, false);
		blockTexture = atlas.findRegion("block");
		TextureRegion[] walkLeftFrames = new TextureRegion[5];
		for (int i = 0; i < 5; i++) {
			walkLeftFrames[i] = atlas.findRegion("bob-0" + (i + 2));
		}
		walkLeftAnimation = new Animation(RUNNING_FRAME_DURATION, walkLeftFrames);

		TextureRegion[] walkRightFrames = new TextureRegion[5];

		for (int i = 0; i < 5; i++) {
			walkRightFrames[i] = new TextureRegion(walkLeftFrames[i]);
			walkRightFrames[i].flip(true, false);
		}
		walkRightAnimation = new Animation(RUNNING_FRAME_DURATION, walkRightFrames);
		bobJumpLeft = atlas.findRegion("bob-up");
		bobJumpRight = new TextureRegion(bobJumpLeft);
		bobJumpRight.flip(true, false);
		bobFallLeft = atlas.findRegion("bob-down");
		bobFallRight = new TextureRegion(bobFallLeft);
		bobFallRight.flip(true, false);
		*/
	}
	
	public TextureRegion getHeroTexture(HeroType type) {
		return heroTextures.get(type);
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
			drawHero();
		spriteBatch.end();
		
		if (debug)
			drawDebug();
	}

	private void drawChunks() {
		// Draw current chunk
		//System.out.println(" --- c " + world.getCurrentChunk().getDrawableBlocks().size());
		for (Block block : world.getCurrentChunk().getDrawableBlocks() ) {
			spriteBatch.draw( blockTextures.get(block.getType()), 
					block.getPosition().x * ppuX, 
					block.getPosition().y * ppuY, 
					block.SIZE.x * ppuX, 
					block.SIZE.y * ppuY);
		}
		// Try to draw part of the next chunk.
		//System.out.println(" --- n " + world.getNextChunk().getDrawableBlocks().size());
		for (Block block : world.getNextChunk().getDrawableBlocks() ) {
			spriteBatch.draw( blockTextures.get(block.getType()), 
					block.getPosition().x * ppuX, 
					block.getPosition().y * ppuY, 
					block.SIZE.x * ppuX, 
					block.SIZE.y * ppuY);
		}
		

	}
	
	
	private float toPixels(float unit) {
		return unit * ppuX;
	}

	/**
	 * Draws hero in our world and updates his animation.
	 */
	private void drawHero() {
		Hero hero = world.getHero();
		
		/**
		// Get default frame depending on if he's standing right or left
		heroFrame = hero.isFacingLeft() ? heroIdleLeft : heroIdleRight;
		
		// Get hero's current frame if he's walking
		if (hero.getState().equals(State.MOVING)) {
			heroFrame = hero.isFacingLeft() ? moveLeftAnimation.getKeyFrame(hero.getStateTime(), true) : moveRightAnimation.getKeyFrame(hero.getStateTime(), true);
		
		// Get hero's frame if he's jumping or falling
		}
		*/
		
		// Draw hero's frame in the proper position
		
		// TO DO: Figure out a way to draw objects in the world all @ the same size.
		// Dont do this in the draw method below. The bounds of the colliding box need to change as well.
		//Scaling.fillX;
		TextureRegion heroFrame = heroTextures.get(hero.getType());
		
		spriteBatch.draw(heroFrame, hero.getPosition().x * ppuX, hero.getPosition().y * ppuY, 
				heroFrame.getRegionWidth()/2, heroFrame.getRegionHeight()/2, 
				heroFrame.getRegionWidth(),  heroFrame.getRegionHeight(),// Hero.SIZE * ppuX, Hero.SIZE * ppuY, 
				1, 1,//ppuX / heroFrame.getRegionWidth(), ppuX / heroFrame.getRegionWidth(), 
				hero.getAngle());

	}

	private void drawDebug() {
		// render blocks
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Line);
		
		/**
		for (Block block : world.getDrawableBlocks((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT)) {
			Rectangle rect = block.getBounds();
			debugRenderer.setColor(new Color(1, 0, 0, 1));
			debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		}
		// render hero
		Hero hero = world.gethero();
		Rectangle rect = hero.getBounds();
		debugRenderer.setColor(new Color(0, 1, 0, 1));
		debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		*/
		debugRenderer.end();
	}
	
	/**
	 * Dispose all resources in WorldRenderer
	 */
	public void dispose() {
		spriteBatch.dispose();
		//background.dispose();
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