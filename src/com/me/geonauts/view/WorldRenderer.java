package com.me.geonauts.view;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.ParallaxLayer;
import com.me.geonauts.model.World;
import com.me.geonauts.model.entities.Block;
import com.me.geonauts.model.entities.Hero;
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
	public HashMap<BlockType, Texture> blockTextures = new HashMap<BlockType, Texture>();
	public HashMap<String, Texture> backgroundTextures = new HashMap<String, Texture> ();
	private Texture heroFrame;
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
		// Load texture atlases
		//TextureAtlas backgroundAtlas = new TextureAtlas(Gdx.files.internal("images/textures/textures.pack"));
		Texture bg1 = new Texture(Gdx.files.internal("images/backgrounds/rednebula3.jpg"));	
	
		background = new ParallaxBackground(new ParallaxLayer[] {
	            new ParallaxLayer(new TextureRegion(bg1), new Vector2(0.2f, 0), new Vector2(0, 0)),
	            
	          //  new ParallaxLayer(backgroundAtlas.findRegion("bg2"),new Vector2(1.0f,1.0f),new Vector2(0, 500)),
	           // new ParallaxLayer(backgroundAtlas.findRegion("bg3"),new Vector2(0.1f,0),new Vector2(0, Constants.HEIGHT-200), new Vector2(0, 0)),
	      }, 800, 480,new Vector2(150,0));
		
		// Load all Block Textures
		blockTextures.put(BlockType.GROUND, new Texture(Gdx.files.internal("images/block.png")));
		
		heroFrame = new Texture(Gdx.files.internal("images/player.png"));
		
		
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
		
		background.render(delta);
		
		// Draw everything to the screen
		spriteBatch.begin();
			//, spriteBatch);
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
					Block.SIZE * ppuX, 
					Block.SIZE * ppuY);
		}
		// Try to draw part of the next chunk.
		//System.out.println(" --- n " + world.getNextChunk().getDrawableBlocks().size());
		for (Block block : world.getNextChunk().getDrawableBlocks() ) {
			spriteBatch.draw( blockTextures.get(block.getType()), 
					block.getPosition().x * ppuX, 
					block.getPosition().y * ppuY, 
					Block.SIZE * ppuX, 
					Block.SIZE * ppuY);
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
		spriteBatch.draw(heroFrame, 
				hero.getPosition().x * ppuX, 
				hero.getPosition().y * ppuY, 
				Hero.SIZE * ppuX, 
				Hero.SIZE * ppuY);
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
	

}