package com.me.geonauts.controller;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.geonauts.model.Chunk;
import com.me.geonauts.model.World;
import com.me.geonauts.model.entities.Block;
import com.me.geonauts.model.entities.Hero;
import com.me.geonauts.model.entities.Hero.State;

public class HeroController {
	enum Keys {
		FLY, FIRE
	}

	// CONSTANTS
	private static final long LONG_FLY_PRESS = 150l;
	private static final float ACCELERATION = 15f;
	private static final float GRAVITY = -12f;
	private static final float MAX_FLY_SPEED = 7f;
	private static final float DAMP = 0.90f;
	private static final float MAX_VEL = 4f;

	private Array<Block> collidable = new Array<Block>();

	// these are temporary
	private static final float WIDTH = 10f;

	// Model objects
	private World world;
	private Hero hero;

	// Keys
	static Map<Keys, Boolean> keys = new HashMap<HeroController.Keys, Boolean>();
	static {
		keys.put(Keys.FLY, false);
		keys.put(Keys.FIRE, false);
	};
	/** When the fly button was pressed */
	private long flyPressedTime;
	/** True as long as the Fly button is being pressed */
	private boolean flyingPressed;
	private Vector2 target;

	private boolean grounded = false;

	/**
	 * Constructor to make the Controller for hero
	 * 
	 * @param world
	 */
	public HeroController(World world) {
		this.world = world;
		this.hero = world.getHero();
	}

	// This is the rectangle pool used in collision detection
	// Good to avoid instantiation each frame
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};

	// ** Key presses and touches **************** //

	public void firePressed(int x, int y) {
		keys.get(keys.put(Keys.FIRE, true));
		target = new Vector2(x, y);
	}
	public void fireReleased() {
		keys.get(keys.put(Keys.FIRE, false));
	}
	
	public void flyPressed() {
		keys.get(keys.put(Keys.FLY, true));
	}
	public void flyReleased() {
		keys.get(keys.put(Keys.FLY, false));
		flyingPressed = false;
	}

	/** The main update method **/
	public void update(float delta) {
		// Processing the input - setting the states of Hero
		processInput();

		// Setting initial vertical acceleration
		hero.getAcceleration().y = GRAVITY;
		hero.getAcceleration().x = Hero.SPEED;

		// Convert acceleration to frame time
		hero.getAcceleration().scl(delta);

		// apply acceleration to change velocity
		hero.getVelocity().add(hero.getAcceleration().x, hero.getAcceleration().y);

		// Make X coord bounce left/right slowly to resemble "flying"
		
		// checking collisions with the surrounding blocks depending on Hero's
		// velocity
		checkCollisionWithBlocks(delta);

		// apply damping to halt Hero nicely
		//hero.getVelocity().x *= DAMP;

		// ensure terminal velocity is not exceeded
		if (hero.getVelocity().x > MAX_VEL) {
			hero.getVelocity().x = MAX_VEL;
		}
		if (hero.getVelocity().x < -MAX_VEL) {
			hero.getVelocity().x = -MAX_VEL;
		}

		// simply updates the state time
		hero.update(delta);

	}

	/** Collision checking **/
	private void checkCollisionWithBlocks(float delta) {
		// scale velocity to frame units
		hero.getVelocity().scl(delta);

		// Obtain the rectangle from the pool instead of instantiating it
		Rectangle heroRect = rectPool.obtain();
		// set the rectangle to hero's bounding box
		heroRect.set(hero.getBounds().x, hero.getBounds().y,
				hero.getBounds().width, hero.getBounds().height);

		// we first check the movement on the horizontal X axis
		int startX, endX;
		int startY = (int) hero.getBounds().y;
		int endY = (int) (hero.getBounds().y + hero.getBounds().height);
		// if Hero is heading left then we check if he collides with the block on
		// his left
		// we check the block on his right otherwise
		if (hero.getVelocity().x < 0) {
			startX = endX = (int) Math.floor(hero.getBounds().x + hero.getVelocity().x);
		} else {
			startX = endX = (int) Math.floor(hero.getBounds().x + hero.getVelocity().x + hero.getBounds().width );
		}
		
		// get the block(s) hero can collide with
		populateCollidableBlocks(startX, startY, endX, endY);

		// simulate hero's movement on the X
		heroRect.x += hero.getVelocity().x;

		// clear collision boxes in world for debug
		world.getCollisionRects().clear();

		// if hero collides, set state to dying
		for (Block block : collidable) {
			if (block == null)  continue;
			if (heroRect.overlaps(block.getBounds())) {
				hero.setState(State.DYING);
				world.getCollisionRects().add(block.getBounds()); // for debug
				break;
			}
		}

		// reset the x position of the collision box to check y
		heroRect.x = hero.getPosition().x;

		// the same thing but on the vertical Y axis
		startX = (int) hero.getBounds().x;
		endX = (int) (hero.getBounds().x + hero.getBounds().width);
		if (hero.getVelocity().y < 0) {
			startY = endY = (int) Math.floor(hero.getBounds().y + hero.getVelocity().y );
		} else {
			startY = endY = (int) Math.floor(hero.getBounds().y + hero.getVelocity().y + hero.getBounds().height );
		}

		//System.out.println(startX + " || " + endX);

		populateCollidableBlocks(startX, startY, endX, endY);

		heroRect.y += hero.getVelocity().y;

		for (Block block : collidable) {
			if (block == null) 	continue;
			if (heroRect.overlaps(block.getBounds())) {
				if (hero.getVelocity().y < 0) {
					// Add "landing" code
					grounded = true;
					hero.getVelocity().y = 0;
				}
				hero.setState(State.DYING);
				world.getCollisionRects().add(block.getBounds());
				break;
			}
		}
		// reset the collision box's position on Y
		heroRect.y = hero.getPosition().y;

		// update Hero's position
		hero.getPosition().add(hero.getVelocity());
		hero.getBounds().x = hero.getPosition().x;
		hero.getBounds().y = hero.getPosition().y;

		// un-scale velocity (not in frame time)
		hero.getVelocity().scl(1 / delta);

	}
	
	/** populate the collidable array with the blocks found in the enclosing coordinates **/
	private void populateCollidableBlocks(int startX, int startY, int endX,	int endY) {
		collidable.clear();
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				if (x >= 0 && y >= 0) {
					collidable.add(world.getBlock(x, y) );
				}
			}
		}
	}

	/** Change Hero's state and parameters based on input controls **/
	private boolean processInput() {
		if (keys.get(Keys.FLY)) {
			// Initial flying force
			if (!hero.getState().equals(State.FLYING)) {
				flyingPressed = true;
				flyPressedTime = System.currentTimeMillis();
				hero.setState(State.FLYING);
				hero.getVelocity().y = MAX_FLY_SPEED;
				grounded = false;
				
			// Still pressing fly key after flying
			} else {
				// Check if we need to stop upward force
				if (flyingPressed && ((System.currentTimeMillis() - flyPressedTime) >= LONG_FLY_PRESS)) {
					flyingPressed = false;
				// Max fly time reached
				} else {
					if (flyingPressed) 
						hero.getVelocity().y = MAX_FLY_SPEED;
				}
			}
		// If he's not flying, he's falling.
		} else {
			hero.setState(State.FALLING);
		}
		if (keys.get(Keys.FIRE)) {
			// CREATE NEW MISSILE w/ TARGET
			
			target = null;
		}

		return false;
	}

}
